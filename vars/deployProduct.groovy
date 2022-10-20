/**
 * Deploys a local file to download.eclipse.org
 * Parameters:
 *  name: Name on the download site
 *  path: Path to the local file
 */
def call(Map args) {
    container('jnlp') {
        sshagent (['projects-storage.eclipse.org-bot-ssh']) {
            sh "ssh -o BatchMode=yes genie.set@projects-storage.eclipse.org rm -rf /home/data/httpd/download.eclipse.org/set/snapshots/bin/${args.name}"
            sh "ssh -o BatchMode=yes genie.set@projects-storage.eclipse.org mkdir -p /home/data/httpd/download.eclipse.org/set/snapshots/bin/${args.name}"
            sh "scp -o BatchMode=yes -r ${args.path} genie.set@projects-storage.eclipse.org:/home/data/httpd/download.eclipse.org/set/snapshots/bin/${args.name}"
        }
    }
}
