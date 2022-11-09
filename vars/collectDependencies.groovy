/**
 * Archives all files in the workspace called 
 *  *.deps (dependency lists)
 *  package-lock.json (npm dependencies)
 * And archives the results as a build artifact 'dependencies.zip'
 */
def call() {
    dir("$WORKSPACE/__dependencies")
    {
        sh 'find .. -name \'package-lock.json\' | awk \'{ f = substr($0, 4); gsub("/", "_", f); print $0 " " f}\' | xargs -L1 cp'
        sh 'find .. -name \'*.deps\' -exec cp {} . \\;'
        sh 'jar -cfM dependencies.zip ./*'
        archiveArtifacts artifacts: 'dependencies.zip'
    }
}
