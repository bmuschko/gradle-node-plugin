package org.gradle.plugins.node.tasks;

import org.gradle.api.Action;
import org.gradle.api.file.FileTree;
import org.gradle.api.provider.PropertyState;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.*;
import org.gradle.process.ExecSpec;

/**
 * A task type for executing Node commands.
 */
@CacheableTask
public class NodeExec extends SourceTask {

    private PropertyState<String> executable;
    private PropertyState<String[]> args;

    public NodeExec() {
        executable = getProject().property(String.class);
        args = getProject().property(String[].class);
    }

    @Input
    public String getExecutable() {
        return executable.get();
    }

    public void setExecutable(Provider<String> executable) {
        this.executable.set(executable);
    }

    public void setExecutable(String executable) {
        this.executable.set(executable);
    }

    @Input
    @Optional
    public String[] getArgs() {
        return args.getOrNull();
    }

    public void setArgs(PropertyState<String[]> args) {
        this.args.set(args);
    }

    public void setArgs(String... args) {
        this.args.set(args);
    }

    @PathSensitive(PathSensitivity.RELATIVE)
    @Override
    public FileTree getSource() {
        return super.getSource();
    }

    @TaskAction
    public void exec() {
        getProject().exec(new Action<ExecSpec>() {
            @Override
            public void execute(ExecSpec execSpec) {
                execSpec.executable(getExecutable());

                if (args.isPresent() && getArgs().length > 0) {
                    execSpec.args((Object[]) getArgs());
                }
            }
        });
    }
}