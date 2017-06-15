package org.gradle.plugins.node;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;

public class NodePluginExtension {

    private final NamedDomainObjectContainer<NodeToolContainer> nodeToolContainer;

    public NodePluginExtension(NamedDomainObjectContainer<NodeToolContainer> nodeToolContainer) {
        this.nodeToolContainer = nodeToolContainer;
    }

    public void tools(Action<? super NamedDomainObjectContainer<NodeToolContainer>> action) {
        action.execute(nodeToolContainer);
    }

    public NamedDomainObjectContainer<NodeToolContainer> getTools() {
        return nodeToolContainer;
    }
}
