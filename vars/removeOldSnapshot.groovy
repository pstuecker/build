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
                    readarray -t snapshots < <(ssh -o BatchMode=yes genie.set@projects-storage.eclipse.org "cd /home/data/httpd/download.eclipse.org/set/snapshots/${args.repo}; find | grep p2.index | cut -c 3- | sed 's%/[^/]*\$%%'")
                    readarray -t branches < <(git ls-remote --heads https://gitlab.eclipse.org/eclipse/set/${args.repo}.git/ | grep -o 'refs/heads/.*' | cut -b 12-)
                    readarray -t tags < <(git ls-remote --tags https://gitlab.eclipse.org/eclipse/set/${args.repo}.git/ | grep -o 'refs/.*' | cut -b 11-)
                    for snapshot in \${snapshots[@]} 
                    do
                        if [[ ! \${branches[@]} =~ \${snapshot} ]]
                        then
                            if [[ ! \${tags[@]} =~ \${snapshot} ]]
                            then
                                echo Delete \${snapshot}
                                ssh -o BatchMode=yes genie.set@projects-storage.eclipse.org "rm -rf /home/data/httpd/download.eclipse.org/set/snapshots/${args.repo}/\${snapshot}"
                            fi
                        fi
                    done
                """
            )
        }
    }
}
