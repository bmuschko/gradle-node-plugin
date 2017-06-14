package com.org.gradle.plugins.node;

import com.org.gradle.plugins.node.tasks.npm.NpmCommand;
import com.org.gradle.plugins.node.tasks.npm.NpmInit;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * A plugin providing basic infrastructure for managing <a href="https://nodejs.org">Node.js</a> applications.
 */
public class NodeBasePlugin implements Plugin<Project> {

    public static final String NODE_GROUP = "Node";
    public static final String NPM_INIT_TASK_NAME = "npmInit";

    @Override
    public void apply(Project project) {
        project.getTasks().withType(NpmCommand.class, new Action<NpmCommand>() {
            @Override
            public void execute(NpmCommand npmCommand) {
                npmCommand.setGroup(NODE_GROUP);
            }
        });

        project.getTasks().create(NPM_INIT_TASK_NAME, NpmInit.class, new Action<NpmInit>() {
            @Override
            public void execute(NpmInit npmInit) {
                npmInit.setDescription("Creates a package.json file with NPM.");
            }
        });

        // TODO: Download and install Node.js automatically
        // TODO: Fails the build if Node.js cannot be detected on the machine
        // TODO: Maybe detect information about the Node environment via some sort of reporting task?
    }
}
