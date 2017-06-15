package org.gradle.plugins.node.typescript.tasks;

import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.OutputFiles;
import org.gradle.plugins.node.base.tasks.NodeExec;

import java.util.ArrayList;
import java.util.List;

public class TypeScriptExec extends NodeExec {

    @OutputFiles
    public FileCollection getCompiledFiles() {
        String[] args = getArgs();
        List<String> compiledFiles = new ArrayList<String>();

        for (String arg : args) {
            if (arg.endsWith(".ts")) {
                compiledFiles.add(arg.replace(".ts", ".js"));
            }
        }

        return getProject().files(compiledFiles);
    }
}
