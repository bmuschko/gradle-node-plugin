package org.gradle.plugins.node

import org.gradle.plugins.node.typescript.TypeScriptTestFixture
import org.gradle.plugins.node.webpack.WebpackTestFixture

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class NodePluginFunctionalTest extends AbstractFunctionalTest {

    def setup() {
        buildFile << nodePlugin()
    }

    def "fails build for unknown tool configured through DSL"() {
        given:
        buildFile << """
            node {
                tools {
                    unknown {
                        sources = ['myapp', 'index/index.html']
                        args = ['myapp/index.js', 'build/js/bundle.js']
                    }
                }
            }
        """

        when:
        def result = buildAndFail('tasks')

        then:
        result.output.contains("Unsupported tool 'unknown'")
    }

    def "can configure multiple tools through DSL"() {
        given:
        buildFile << """
            node {
                tools {
                    webpack {
                        sources = ['myapp', 'index/index.html']
                        args = ['myapp/index.js', 'build/js/bundle.js']
                    }

                    typescript {
                        sources = ['scripts']
                        args = ['scripts/greeter.ts']
                    }
                }
            }
        """
        buildFile << WebpackTestFixture.npmInstallLodash()
        buildFile << WebpackTestFixture.npmInstallWebpack()
        buildFile << TypeScriptTestFixture.npmInstallTypeScript()
        buildFile << """
            tasks.withType(NpmInstall) {
                mustRunAfter npmInit
            }

            task npmSetup {
                dependsOn npmInit, npmInstallLodash, npmInstallWebpack, npmInstallTypeScript
            }
        """
        createDir('myapp')
        createFile('myapp/index.js') << WebpackTestFixture.indexJs()
        createDir('index')
        createFile('index/index.html') << WebpackTestFixture.indexHtml()
        createDir('scripts')
        createFile('scripts/greeter.ts') << TypeScriptTestFixture.greeterTs()

        when:
        def result = build('npmSetup', 'webpack', 'typescript')

        then:
        file('package.json').exists()
        file('package-lock.json').exists()
        file('build/js/bundle.js').exists()
        file('scripts/greeter.js').exists()
        result.output.contains('+ lodash@4')
        result.output.contains('+ webpack@2')
        result.output.contains('+ typescript@2')
        result.task(":npmSetup").outcome == SUCCESS
        result.task(":webpack").outcome == SUCCESS
        result.task(":typescript").outcome == SUCCESS
    }

    static String nodePlugin() {
        """
            plugins {
                id 'org.gradle.plugins.node'
            }
        """
    }
}
