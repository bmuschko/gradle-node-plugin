package org.gradle.plugins.node.webpack

import org.gradle.plugins.node.AbstractFunctionalTest

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS
import static org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE

class NodeWebpackPluginFunctionalTest extends AbstractFunctionalTest {

    def setup() {
        buildFile << nodeWebpackPlugin()
    }

    def "can create custom node exec task"() {
        given:
        buildFile << WebpackTestFixture.npmInstallLodash()
        buildFile << WebpackTestFixture.npmInstallWebpack()
        buildFile << """
            import org.gradle.plugins.node.webpack.tasks.WebpackExec

            tasks.withType(NpmInstall) {
                mustRunAfter npmInit
            }

            task npmSetup {
                dependsOn npmInit, npmInstallLodash, npmInstallWebpack
            }

            task webpack(type: WebpackExec) {
                args 'app/index.js', "\$buildDir/js/bundle.js"
            }
        """
        createDir('app')
        createFile('app/index.js') << WebpackTestFixture.indexJs()
        createFile('index.html') << WebpackTestFixture.indexHtml()

        when:
        def result = build('npmSetup', 'webpack')

        then:
        file('package.json').exists()
        file('package-lock.json').exists()
        file('build/js/bundle.js').exists()
        result.output.contains('+ lodash@4')
        result.output.contains('+ webpack@2')
        result.task(":npmSetup").outcome == SUCCESS
        result.task(":webpack").outcome == SUCCESS

        when:
        result = build('webpack')

        then:
        file('package.json').exists()
        file('package-lock.json').exists()
        file('build/js/bundle.js').exists()
        !result.output.contains('+ lodash@4')
        !result.output.contains('+ webpack@2')
        result.task(":webpack").outcome == UP_TO_DATE
    }

    static String nodeWebpackPlugin() {
        """
            plugins {
                id 'org.gradle.plugins.node-webpack'
            }
        """
    }
}
