package org.gradle.plugins.node.webpack.tasks;

import org.gradle.api.tasks.OutputFile;
import org.gradle.plugins.node.base.tasks.NodeExec;

import java.io.File;

/**
 * A task type for executing Webpack.
 */
public class WebpackExec extends NodeExec {

    @OutputFile
    public File getBundleFile() {
        String[] args = getArgs();
        return new File(args[args.length - 1]);
    }
}
