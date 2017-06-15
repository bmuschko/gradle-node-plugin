package org.gradle.plugins.node.webpack;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.plugins.node.base.NodeBasePlugin;
import org.gradle.plugins.node.base.tasks.NodeExec;
import org.gradle.plugins.node.webpack.tasks.WebpackExec;

import java.io.File;

/**
 * A plugin that supports bundling web assets with <a href="https://webpack.js.org/">Webpack</a>.
 */
public class NodeWebpackPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        project.getPlugins().apply(NodeBasePlugin.class);

        project.getTasks().withType(WebpackExec.class, new Action<NodeExec>() {
            @Override
            public void execute(NodeExec nodeExec) {
                nodeExec.setDescription("Bundles web assets with Webpack.");
                nodeExec.setExecutable(new File(project.getProjectDir(), "node_modules/.bin/webpack").getAbsolutePath());
                nodeExec.setSource(new String[] { "app", "index.html" });
            }
        });
    }
}
