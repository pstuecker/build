/**
 * Downloads CEF and extracts some files
 *
 * @param versionFile Path to a file that contains the version to download
 * @param files a list of files to extract
 * @param wildcards a list of wildcards to extract
 */
def call(Map args) {
    def version = getVersion(args.versionFile)
    def files = args.files ?: []
    def fileargs = ''
    for (entry in files) {
        fileargs += "${version}/${entry} "
    }
    def wildcards = args.wildcards ?: []
    for (entry in wildcards) {
        fileargs += "--wildcards ${version}/${entry} "
    }

    downloadFile file: 'cef.tar.bz2', url: "https://cef-builds.spotifycdn.com/${version}.tar.bz2"
    sh "tar xf cef.tar.bz2 ${fileargs} --strip-components 2"
    sh 'rm -rf cef.tar.bz2'
}

def getVersion(String file) {
    return sh (script: "grep -E -v ^# ${file}", returnStdout: true).trim()
}
