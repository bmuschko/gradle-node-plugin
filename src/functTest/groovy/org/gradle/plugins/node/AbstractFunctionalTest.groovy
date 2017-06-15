package org.gradle.plugins.node

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

abstract class AbstractFunctionalTest extends Specification {
    @Rule
    TemporaryFolder temporaryFolder = new TemporaryFolder()
    File projectDir
    File buildFile

    def setup() {
        projectDir = temporaryFolder.root
        buildFile = temporaryFolder.newFile('build.gradle')
    }

    protected BuildResult build(String... arguments) {
        createAndConfigureGradleRunner(arguments).forwardOutput().build()
    }

    protected BuildResult buildAndFail(String... arguments) {
        createAndConfigureGradleRunner(arguments).buildAndFail()
    }

    private GradleRunner createAndConfigureGradleRunner(String... arguments) {
        GradleRunner.create().withProjectDir(projectDir).withArguments(arguments).withPluginClasspath()
    }

    File createDir(String... paths) {
        temporaryFolder.newFolder(paths)
    }

    File createFile(String path) {
        temporaryFolder.newFile(path)
    }

    File file(String path) {
        new File(temporaryFolder.root, path)
    }
}