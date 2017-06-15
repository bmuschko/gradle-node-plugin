package org.gradle.plugins.node.typescript

class TypeScriptTestFixture {

    static String greeterTs() {
        """
            function greeter(person) {
                return "Hello, " + person;
            }
            
            var user = "Jane User";
            
            document.body.innerHTML = greeter(user);
        """
    }

    static String npmInstallTypeScript() {
        """
            import org.gradle.plugins.node.base.tasks.npm.NpmInstall
            import org.gradle.plugins.node.base.tasks.npm.NpmInstall.SaveType

            task npmInstallTypeScript(type: NpmInstall) {
                saveType = SaveType.PROD
                packageName = 'typescript@~2'
            }
        """
    }
}
