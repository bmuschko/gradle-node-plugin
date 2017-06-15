package org.gradle.plugins.node.webpack;

import org.gradle.plugins.node.base.NodeBasePlugin;
import org.gradle.plugins.node.webpack.tasks.WebpackExec;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;

/**
 * A plugin that supports bundling web assets with <a href="https://webpack.js.org/">Webpack</a>.
 */
public class NodeWebpackPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        project.getPlugins().apply(NodeBasePlugin.class);

        project.getTasks().withType(WebpackExec.class, new Action<WebpackExec>() {
            @Override
            public void execute(WebpackExec webpackExec) {
                webpackExec.setDescription("Bundles web assets with Webpack.");
                webpackExec.setExecutable(new File(project.getProjectDir(), "node_modules/.bin/webpack").getAbsolutePath());
                webpackExec.setSource(new String[] { "app", "index.html" });
            }
        });
    }
}
