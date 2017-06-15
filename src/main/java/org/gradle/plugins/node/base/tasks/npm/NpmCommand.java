package org.gradle.plugins.node.base.tasks.npm;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecSpec;

import java.util.List;

/**
 * An abstract class for defining NPM commands.
 */
public abstract class NpmCommand extends DefaultTask {

    @TaskAction
    public void exec() {
        getProject().exec(new Action<ExecSpec>() {
            @Override
            public void execute(ExecSpec execSpec) {
                execSpec.executable("npm");
                execSpec.args(getArguments());
            }
        });
    }

    @Input
    abstract List<String> getArguments();
}
