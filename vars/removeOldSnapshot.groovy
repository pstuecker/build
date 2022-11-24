/**
 * Remove old snapshot
 *
 * @param repo repository of snapshot
 */
def call(Map args) {
    container('jnlp') {
        sshagent (['projects-storage.eclipse.org-bot-ssh']) {
            sh (
                """#!/bin/bash
                    readarray -t snapshots < <(ssh -o BatchMode=yes genie.set@projects-storage.eclipse.org ls /home/data/httpd/download.eclipse.org/set/snapshots/${args.repo}/feature)
                    readarray -t branches < <(git branch -a | sed 's/[A-Za-z * \/]\+\/feature\///g')
                    for snapshot in \${snapshots[@]}
                    do
                        if [[ ! \${branches[@]} =~ ${snapshot} ]]
                        then
                            echo Delete
                            ssh -o BatchMode=yes genie.set@projects-storage.eclipse.org rm -rf /home/data/httpd/download.eclipse.org/set/snapshots/${args.repo}/feature/\${snapshot}
                        fi
                    done
                """
            )
        }
    }
}
