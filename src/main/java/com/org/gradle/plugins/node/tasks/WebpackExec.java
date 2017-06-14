package com.org.gradle.plugins.node.tasks;

import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;

import java.io.File;

/**
 * A task type for executing Webpack.
 */
public class WebpackExec extends NodeExec {

    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getPackageLockFile() {
        return getProject().file("package-lock.json");
    }

    @OutputFile
    public File getBundleFile() {
        String[] args = getArgs();
        return new File(args[args.length - 1]);
    }
}
