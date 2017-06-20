package org.gradle.plugins.node.base.tasks;

import org.gradle.api.Action;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileTree;
import org.gradle.api.provider.PropertyState;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.*;
import org.gradle.process.ExecSpec;

import java.io.File;

/**
 * A task type for executing Node commands.
 */
@CacheableTask
public class NodeExec extends SourceTask {

    private ConfigurableFileCollection dest;
    private PropertyState<String> executable;
    private PropertyState<String[]> args;

    public NodeExec() {
        dest = getProject().files();
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

    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getPackageLockFile() {
        return getProject().file("package-lock.json");
    }

    @OutputFiles
    @Optional
    public FileCollection getDest() {
        return dest;
    }

    public void setDest(FileCollection outputFiles) {
        this.dest.setFrom(outputFiles);
    }

    public void setDest(String... args) {
        setDest(getProject().files((Object) args));
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
