package com.org.gradle.plugins.node.tasks.npm;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a package.json file with NPM.
 * <p>
 * For more information see <a href="https://docs.npmjs.com/cli/init">npm-init</a>.
 */
public class NpmInit extends NpmCommand {

    private boolean force;

    @Input
    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    @OutputFile
    public File getPackageFile() {
        return getProject().file("package.json");
    }

    @Override
    List<String> getArguments() {
        List<String> args = new ArrayList<String>();
        args.add("init");
        args.add("-y");

        if (force) {
            args.add("-f");
        }

        return args;
    }
}
