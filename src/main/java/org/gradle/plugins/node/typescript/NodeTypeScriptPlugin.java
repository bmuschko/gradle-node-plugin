package org.gradle.plugins.node.typescript;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.plugins.node.base.NodeBasePlugin;
import org.gradle.plugins.node.base.tasks.NodeExec;
import org.gradle.plugins.node.typescript.tasks.TypeScriptExec;

import java.io.File;

public class NodeTypeScriptPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        project.getPlugins().apply(NodeBasePlugin.class);

        project.getTasks().withType(TypeScriptExec.class, new Action<NodeExec>() {
            @Override
            public void execute(NodeExec nodeEx) {
                nodeEx.setDescription("Compiles TypeScript to JavaScript.");
                nodeEx.setExecutable(new File(project.getProjectDir(), "node_modules/.bin/tsc").getAbsolutePath());
            }
        });
    }
}
