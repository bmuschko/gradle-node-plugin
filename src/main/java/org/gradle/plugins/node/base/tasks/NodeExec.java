package org.gradle.plugins.node.base.tasks;

import org.gradle.api.Action;
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

    private PropertyState<File> destFile;
    private PropertyState<File> destDirectory;
    private PropertyState<String> executable;
    private PropertyState<String[]> args;

    public NodeExec() {
        destFile = getProject().property(File.class);
        destDirectory = getProject().property(File.class);
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

//    @InputFile
//    @PathSensitive(PathSensitivity.RELATIVE)
//    public File getPackageLockFile() {
//        return getProject().file("package-lock.json");
//    }

    @OutputFile
    @Optional
    public File getDestFile() {
        return destFile.getOrNull();
    }

    public void setDestFile(File outputFile) {
        this.destFile.set(outputFile);
    }

    public void setDestFile(String path) {
        setDestFile(getProject().file(path));
    }

    @OutputDirectory
    @Optional
    public File getDestDirectory() {
        return destDirectory.getOrNull();
    }

    public void setDestDirectory(File outputFile) {
        this.destDirectory.set(outputFile);
    }

    public void setDestDirectory(String path) {
        setDestDirectory(getProject().file(path));
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
