package org.gradle.plugins.node.webpack

class WebpackTestFixture {

    static String indexJs() {
        """
            import _ from 'lodash';

            function component () {
              var element = document.createElement('div');
            
              /* lodash is used here for bundling demonstration purposes */
              element.innerHTML = _.join(['Build', 'together;', 'not', 'alone'], ' ');
            
              return element;
            }
            
            document.body.appendChild(component());
        """
    }

    static String indexHtml() {
        """
            <html>
              <head>
                <title>Gradle + Webpack Demo</title>
              </head>
              <body>
                <script src="build/js/bundle.js"></script> 
              </body>
            </html>
        """
    }

    static String npmInstallLodash() {
        """
            import org.gradle.plugins.node.base.tasks.npm.NpmInstall
            import org.gradle.plugins.node.base.tasks.npm.NpmInstall.SaveType

            task npmInstallLodash(type: NpmInstall) {
                saveType = SaveType.PROD
                packageName = 'lodash@~4'
            }
        """
    }

    static String npmInstallWebpack() {
        """
            import org.gradle.plugins.node.base.tasks.npm.NpmInstall
            import org.gradle.plugins.node.base.tasks.npm.NpmInstall.SaveType

            task npmInstallWebpack(type: NpmInstall) {
                saveType = SaveType.DEV
                packageName = 'webpack@~2'
            }
        """
    }
}
