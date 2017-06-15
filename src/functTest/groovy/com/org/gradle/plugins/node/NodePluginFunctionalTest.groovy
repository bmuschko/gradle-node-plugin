package com.org.gradle.plugins.node

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class NodePluginFunctionalTest extends AbstractFunctionalTest {

    def setup() {
        buildFile << nodePlugin()
    }

    def "can configure single tool through DSL"() {
        buildFile << """
            node {
                tools {
                    webpack {
                        sources = ['myapp', 'index/index.html']
                        args = ['myapp/index.js', 'build/js/bundle.js']
                    }
                }
            }
        """
        buildFile << WebpackTestFixture.npmInstallLodash()
        buildFile << WebpackTestFixture.npmInstallWebpack()
        buildFile << """
            import com.org.gradle.plugins.node.tasks.WebpackExec

            tasks.withType(NpmInstall) {
                mustRunAfter npmInit
            }

            task npmSetup {
                dependsOn npmInit, npmInstallLodash, npmInstallWebpack
            }
        """
        createDir('myapp')
        createFile('myapp/index.js') << WebpackTestFixture.indexJs()
        createDir('index')
        createFile('index/index.html') << WebpackTestFixture.indexHtml()

        when:
        def result = build('npmSetup', 'webpack', '-s')

        then:
        file('package.json').exists()
        file('package-lock.json').exists()
        file('build/js/bundle.js').exists()
        result.output.contains('+ lodash@4')
        result.output.contains('+ webpack@2')
        result.task(":npmSetup").outcome == SUCCESS
        result.task(":webpack").outcome == SUCCESS
    }

    static String nodePlugin() {
        """
            plugins {
                id 'org.gradle.plugins.node'
            }
        """
    }
}
