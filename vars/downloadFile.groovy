/**
 * Downloads a file into a target file name
 *
 * @param url Url to download
 * @param file Output file
 */
def call(Map args) {
    sh "curl -L -o '${args.file}' ${args.url}"
}
