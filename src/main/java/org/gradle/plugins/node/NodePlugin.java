package org.gradle.plugins.node;

import org.gradle.plugins.node.base.NodeBasePlugin;
import org.gradle.plugins.node.base.tasks.NodeExec;
import org.gradle.plugins.node.webpack.tasks.WebpackExec;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.plugins.node.webpack.NodeWebpackPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NodePlugin implements Plugin<Project> {

    public static final String EXTENSION_NAME = "node";
    private static final Map<String, NodeToolPluginImplementation> REGISTERED_TOOLS;

    static {
        Map<String, NodeToolPluginImplementation> tools = new HashMap<String, NodeToolPluginImplementation>();
        tools.put("webpack", new NodeToolPluginImplementation(NodeWebpackPlugin.class, WebpackExec.class));
        REGISTERED_TOOLS = Collections.unmodifiableMap(tools);
    }

    @Override
    public void apply(Project project) {
        project.getPlugins().apply(NodeBasePlugin.class);

        NamedDomainObjectContainer<NodeToolContainer> nodeToolContainer = project.container(NodeToolContainer.class);
        final NodePluginExtension extension = project.getExtensions().create(EXTENSION_NAME, NodePluginExtension.class, nodeToolContainer);

        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                for (final NodeToolContainer tool : extension.getTools()) {
                    if (!REGISTERED_TOOLS.containsKey(tool.getName())) {
                        throw new IllegalArgumentException("Unsupported tool " + tool.getName());
                    }

                    NodeToolPluginImplementation nodeToolPluginImplementation = REGISTERED_TOOLS.get(tool.getName());
                    project.getPlugins().apply(nodeToolPluginImplementation.getPluginClass());

                    project.getTasks().create(tool.getName(), nodeToolPluginImplementation.getTaskClass(), new Action<NodeExec>() {
                        @Override
                        public void execute(NodeExec nodeExec) {
                            if (!tool.getSources().isEmpty()) {
                                String[] sources = tool.getSources().toArray(new String[tool.getSources().size()]);
                                nodeExec.setSource(sources);
                            }
                            if (!tool.getArgs().isEmpty()) {
                                String[] args = tool.getArgs().toArray(new String[tool.getArgs().size()]);
                                nodeExec.setArgs(args);
                            }
                        }
                    });
                }
            }
        });
    }

    private static class NodeToolPluginImplementation {
        private final Class<? extends Plugin<Project>> pluginClass;
        private final Class<? extends NodeExec> taskClass;

        public NodeToolPluginImplementation(Class<? extends Plugin<Project>> pluginClass, Class<? extends NodeExec> taskClass) {
            this.pluginClass = pluginClass;
            this.taskClass = taskClass;
        }

        public Class<? extends Plugin<Project>> getPluginClass() {
            return pluginClass;
        }

        public Class<? extends NodeExec> getTaskClass() {
            return taskClass;
        }
    }
}
