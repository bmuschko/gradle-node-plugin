/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.plugins.node.base.tasks;

import org.gradle.api.Action;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.FileVisitDetails;
import org.gradle.api.file.FileVisitor;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.incremental.IncrementalTaskInputs;
import org.gradle.api.tasks.incremental.InputFileDetails;
import org.gradle.api.tasks.util.PatternSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class IncrementalNodeExec extends NodeExec {
    @OutputDirectory
    public File dest;

    // Allow flexible user input
    public void setDest(final String input) {
        this.dest = getProject().file(input);
    }

    public void dest(final String input) {
        setDest(input);
    }

    public void dest(final File input) {
        this.dest = input;
    }

    @TaskAction
    void executeIncremental(IncrementalTaskInputs inputs) {
        if (!inputs.isIncremental()) {
            getProject().delete((Object) dest);
        }

        final PatternSet patternSet = new PatternSet();

        inputs.outOfDate(new Action<InputFileDetails>() {
            @Override
            public void execute(final InputFileDetails inputFileDetails) {
                if (inputFileDetails.isModified() || inputFileDetails.isRemoved()) {
                    deleteOutputsFor(inputFileDetails.getFile());
                }
                if (inputFileDetails.isAdded() || inputFileDetails.isModified()) {
                    // TODO: Figure out what to include here such that we include the right pattern for exactly this input file
                    patternSet.include(Paths.get(inputFileDetails.getFile().toURI()).toFile().getAbsolutePath());
                }
            }
        });

        inputs.removed(new Action<InputFileDetails>() {
            public void execute(InputFileDetails inputFileDetails) {
                deleteOutputsFor(inputFileDetails.getFile());
            }
        });

        processChanges(getSource().matching(patternSet));
    }

    private void processChanges(final FileTree sourceTree) {
        sourceTree.visit(new FileVisitor() {
            @Override
            public void visitDir(FileVisitDetails visitDetails) {
                visitDetails.getRelativePath().getFile(dest).mkdir();
            }

            @Override
            public void visitFile(FileVisitDetails visitDetails) {
                try {
//                    final File destFile = visitDetails.getRelativePath().getFile(dest);
                    // TODO: do work
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void deleteOutputsFor(final File file) {
        file.delete();
    }
}
