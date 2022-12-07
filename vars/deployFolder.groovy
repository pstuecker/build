/**
 * Clears a folder and copies contents from another folder into it
 *
 * Example: depolyFolder(src: 'source', dest: 'target')
 * Removes everything in target/ and copies the contents from soruce/ into it
 *
 * @param src Folder to deploy contents from
 * @param dest Folder to deploy into
 *
 * @author Stuecker
 */
def call(Map args) {
    sh "rm -rf ${args.dest}/*"
    sh "mkdir -p ${args.dest}/"
    sh "cp -r ${args.src}/* ${args.dest}"
}
