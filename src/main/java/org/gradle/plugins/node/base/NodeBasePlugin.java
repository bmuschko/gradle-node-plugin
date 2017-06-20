package org.gradle.plugins.node.base;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.plugins.node.base.tasks.npm.NpmCommand;
import org.gradle.plugins.node.base.tasks.npm.NpmInit;

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

        // TODO: generateTasksFromPackageJson(project, project.file("package.json"));
        // TODO: generateTasksFromNodeModules(project, getNodeModulesDirectory(project));

        // TODO: Download and install Node.js automatically
        // TODO: Fails the build if Node.js cannot be detected on the machine
        // TODO: Maybe detect information about the Node environment via some sort of reporting task?
    }

    // Generate tasks from package.json scripts
//    private void generateTasksFromPackageJson(Project project, File packageJsonFile) {
//        def packageJson = packageJsonFile.withReader { r -> new groovy.json.JsonSlurper().parse(r) }
//        if (packageJson.scripts) {
//            for (Map.Entry<String, String> script : packageJson.scripts) {
//                // TODO: use value for arguments, use key for configuration
//                project.tasks.create(name: NODE_RUN_PREFIX + script.key, type:NodeExec);
//            }
//        }
//    }
//
//    // Generate tasks from node_modules/.bin/
//    private void generateTasksFromNodeModules(Project project, File nodeModulesDir) {
//        // List files in node_modules/.bin
//        for (File file : new File(nodeModulesDir, ".bin").listFiles()) {
//            if (file.isFile()) {
//                project.tasks.create(name: NODE_BIN_PREFIX + file.getName(), type: NodeExec);
//            }
//        }
//    }
}
