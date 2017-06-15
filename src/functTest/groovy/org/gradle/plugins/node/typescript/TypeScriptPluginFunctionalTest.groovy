package org.gradle.plugins.node.typescript

import org.gradle.plugins.node.AbstractFunctionalTest

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class TypeScriptPluginFunctionalTest extends AbstractFunctionalTest {

    def setup() {
        buildFile << nodeTypeScriptPlugin()
    }

    def "can create custom node exec task"() {
        given:
        buildFile << TypeScriptTestFixture.npmInstallTypeScript()
        buildFile << """
            import org.gradle.plugins.node.typescript.tasks.TypeScriptExec

            tasks.withType(NpmInstall) {
                mustRunAfter npmInit
            }

            task npmSetup {
                dependsOn npmInit, npmInstallTypeScript
            }

            task typescript(type: TypeScriptExec) {
                source = ['scripts']
                args 'scripts/greeter.ts'
            }
        """
        createDir('scripts')
        createFile('scripts/greeter.ts') << TypeScriptTestFixture.greeterTs()

        when:
        def result = build('npmSetup', 'typescript')

        then:
        file('package.json').exists()
        file('package-lock.json').exists()
        file('scripts/greeter.js').exists()
        result.output.contains('+ typescript@2')
        result.task(":npmSetup").outcome == SUCCESS
        result.task(":typescript").outcome == SUCCESS
    }

    static String nodeTypeScriptPlugin() {
        """
            plugins {
                id 'org.gradle.plugins.node-typescript'
            }
        """
    }
}
