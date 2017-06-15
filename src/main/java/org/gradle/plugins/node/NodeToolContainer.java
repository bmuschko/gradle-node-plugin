package org.gradle.plugins.node;

import java.util.ArrayList;
import java.util.List;

public class NodeToolContainer {

    private final String name;
    private List<String> sources = new ArrayList<String>();
    private List<String> args = new ArrayList<String>();

    public NodeToolContainer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
