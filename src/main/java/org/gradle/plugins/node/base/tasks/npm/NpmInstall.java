package org.gradle.plugins.node.base.tasks.npm;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Installs a package, and any packages that it depends on with NPM.
 * <p>
 * For more information see <a href="https://docs.npmjs.com/cli/install">npm-install</a>.
 */
public class NpmInstall extends NpmCommand {

    private SaveType saveType;
    private String packageName;

    @Input
    public SaveType getSaveType() {
        return saveType;
    }

    public void setSaveType(SaveType saveType) {
        this.saveType = saveType;
    }

    @Input
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @OutputFile
    public File getPackageLockFile() {
        return getProject().file("package-lock.json");
    }

    @Override
    List<String> getArguments() {
        List<String> args = new ArrayList<String>();
        args.add("install");
        args.add(saveType.getFlag());
        args.add(packageName);
        return args;
    }

    public enum SaveType {
        PROD("-P"), DEV("-D"), OPTIONAL("-O"), PREVENT("--no-save");

        private final String flag;

        SaveType(String flag) {
            this.flag = flag;
        }

        public String getFlag() {
            return flag;
        }
    }
}
