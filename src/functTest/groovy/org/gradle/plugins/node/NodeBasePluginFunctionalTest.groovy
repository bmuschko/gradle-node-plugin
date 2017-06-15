package org.gradle.plugins.node

import static org.gradle.plugins.node.NodeBasePlugin.NODE_GROUP
import static org.gradle.plugins.node.NodeBasePlugin.NPM_INIT_TASK_NAME
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS
import static org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE

class NodeBasePluginFunctionalTest extends AbstractFunctionalTest {

    def setup() {
        buildFile << nodeBasePlugin()
    }

    def "creates NPM init task out-of-the-box"() {
        when:
        def buildResult = build('tasks', '--all')

        then:
        buildResult.output.contains("""$NODE_GROUP tasks
----------
$NPM_INIT_TASK_NAME - Creates a package.json file with NPM.""")
    }

    def "init package.json file task is incremental"() {
        when:
        def result = build(NPM_INIT_TASK_NAME)

        then:
        file('package.json').exists()
        result.task(":$NPM_INIT_TASK_NAME").outcome == SUCCESS

        when:
        result = build('npmInit')
        then:
        file('package.json').exists()
        result.task(":$NPM_INIT_TASK_NAME").outcome == UP_TO_DATE
    }

    def "can install package without initializing package.json"() {
        given:
        buildFile << WebpackTestFixture.npmInstallLodash()

        when:
        def result = build('npmInstallLodash')

        then:
        !file('package.json').exists()
        file('package-lock.json').exists()
        result.output.contains('added 1 package')
    }

    def "install package task is incremental"() {
        given:
        buildFile << WebpackTestFixture.npmInstallLodash()

        when:
        def result = build('npmInstallLodash')

        then:
        !file('package.json').exists()
        file('package-lock.json').exists()
        result.task(":npmInstallLodash").outcome == SUCCESS

        when:
        result = build('npmInstallLodash')

        then:
        !file('package.json').exists()
        file('package-lock.json').exists()
        result.task(":npmInstallLodash").outcome == UP_TO_DATE
    }

    def "can init package.json file and install package"() {
        given:
        buildFile << WebpackTestFixture.npmInstallLodash()

        when:
        def result = build('npmInit', 'npmInstallLodash')

        then:
        file('package.json').exists()
        file('package-lock.json').exists()
        result.output.contains('added 1 package')
        result.task(":npmInit").outcome == SUCCESS
        result.task(":npmInstallLodash").outcome == SUCCESS
    }

    def "can create custom node exec task"() {
        given:
        buildFile << WebpackTestFixture.npmInstallLodash()
        buildFile << WebpackTestFixture.npmInstallWebpack()
        buildFile << """
            import org.gradle.plugins.node.tasks.NodeExec

            tasks.withType(NpmInstall) {
                mustRunAfter npmInit
            }

            task npmSetup {
                dependsOn npmInit, npmInstallLodash, npmInstallWebpack
            }

            task webpack(type: NodeExec) {
                source = ['app', 'index.html']
                executable = "\$projectDir/node_modules/.bin/webpack"
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
        result.output.contains('+ lodash@4')
        result.output.contains('+ webpack@2')
        file('build/js/bundle.js').exists()
        result.task(":npmSetup").outcome == SUCCESS
        result.task(":webpack").outcome == SUCCESS
    }

    static String nodeBasePlugin() {
        """
            plugins {
                id 'org.gradle.plugins.node-base'
            }
        """
    }
}
