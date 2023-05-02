/**
 * Deploys a local p2 site to download.eclipse.org
 * Parameters:
 *  name: Name on the p2 site
 *  path: Path to local p2 site
 */
def call(Map args) {
    container('jnlp') {
        sshagent (['projects-storage.eclipse.org-bot-ssh']) {
            sh "ssh -o BatchMode=yes genie.set@projects-storage.eclipse.org rm -rf /home/data/httpd/download.eclipse.org/set/nightly/${args.branch}/p2/${args.name}"
            sh "ssh -o BatchMode=yes genie.set@projects-storage.eclipse.org mkdir -p /home/data/httpd/download.eclipse.org/set/nightly/${args.branch}/p2/${args.name}"
            sh "scp -o BatchMode=yes -r ${args.path}/* genie.set@projects-storage.eclipse.org:/home/data/httpd/download.eclipse.org/set/nightly/${args.branch}/p2/${args.name}"
        }
    }
}
