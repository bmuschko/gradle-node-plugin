package org.gradle.plugins.node.webpack.tasks;

import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Optional;
import org.gradle.plugins.node.base.tasks.NodeExec;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * A task type for executing Webpack.
 */
@CacheableTask
public class WebpackExec extends NodeExec {
    private static final String DEFAULT_WEBPACK_CONFIG_PATH = "webpack.config.js";
    private static final String ALTERNATE_WEBPACK_CONFIG_PATH = "webpackfile.js";

    // TODO: infer inputs and outputs from webpack CLI: (e.g. webpack index=./src/index.js entry2=./src/index2.js dist/bundle.js)

    @InputFile
    @Optional
    public File getWebpackConfigFile() {
        if (getArgs() != null) {
            final List<String> argsList = Arrays.asList(getArgs());
            final int configFileArgIndex = argsList.indexOf("--config");
            if (configFileArgIndex != -1 && argsList.size() > configFileArgIndex + 2) {
                return getProject().file(argsList.get(configFileArgIndex + 1));
            }
        }

        // Use webpack defaults. See https://webpack.js.org/api/cli/
        if (getProject().file(DEFAULT_WEBPACK_CONFIG_PATH).isFile()) {
            return getProject().file(DEFAULT_WEBPACK_CONFIG_PATH);
        } else if (getProject().file(ALTERNATE_WEBPACK_CONFIG_PATH).isFile()) {
            return getProject().file(ALTERNATE_WEBPACK_CONFIG_PATH);
        }

        return null;
    }

    // TODO: infer outputs from args/flags https://webpack.js.org/api/cli/#output-options
}
