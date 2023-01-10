# 
# Copyright (c) 2022 DB Netz AG and others.
#  
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v20.html
#
import gitlab
import sys 
import os

if len(sys.argv) < 3:
    print("Usage: gitlabCommit.py projectId remotefile localfile")
    sys.exit(-1)

content = open(sys.argv[3]).read()
# Refuse to commit empty files
if len(content) < 10: 
  sys.exit(-1)

gl = gitlab.Gitlab(url='https://gitlab.eclipse.org', private_token=os.getenv('GITLAB_TOKEN'))
project = gl.projects.get(int(sys.argv[1]))

oldFile = project.files.get(sys.argv[2], "main")
if oldFile is not None:
    oldContent = oldFile.decode().decode("utf-8")
    if oldContent == content:
        print("New content matches existing file. Skipping commit.")
        sys.exit(0)
    else: 
        print("New content does not match existing file. Updating...")
else:
    print("File does not exist. Creating...")

data = {
    'branch': 'main',
    'commit_message': 'Update dependency information',
    'actions': [
        {
            'action': 'update',
            'file_path': sys.argv[2],
            'content': content,
        }
    ]
}

commit = project.commits.create(data)