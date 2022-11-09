/**
 * Archives all files in the workspace called 
 *  *.deps (dependency lists)
 *  package-lock.json (npm dependencies)
 * And archives the results as a build artifact 'dependencies.zip'
 */
def call() {
    sh "rm -rf $WORKSPACE/__dependencies"
    sh "mkdir -p $WORKSPACE/__dependencies"
    sh "find $WORKSPACE -name 'package-lock.json' -exec cp {} __dependencies \;"
    sh "find $WORKSPACE -name '*.deps' -exec cp {} __dependencies \;"

    dir("$WORKSPACE/__dependencies")
    {
        sh 'jar -cfM dependencies.zip ./*'
        archiveArtifacts artifacts: 'dependencies.zip'
    }
}
