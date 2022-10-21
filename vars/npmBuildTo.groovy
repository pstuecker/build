/**
 * Builds a nodejs project in the local directory and copies the contents of the dist folder
 * to the specified location
 *
 * Example: npmBuildTo target: '../web'
 * Builds the local npm project to ../web
 *
 * @param target the target path to copy the result to
 */
def call(Map args) {
    sh 'npm ci'
    sh 'npm run build-prod'
    sh "rm -rf ${args.target}"
    sh "mkdir -p ${args.target}"
    sh "cp -r dist/* ${args.target}"
}
