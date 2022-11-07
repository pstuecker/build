# 
# Copyright (c) 2022 DB Netz AG and others.
#  
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v20.html
#
import zipfile
import sys
import subprocess
import os

if len(sys.argv) < 3:
    print("Usage: process.py in.zip outfile")
    sys.exit(-1)

with zipfile.ZipFile(sys.argv[1], 'r') as zf:
    for filename in zf.namelist():
        print("Processing", filename)

        data = zf.read(filename)
        # npm lock files must be called exactly package-lock.json for Dash to process
        fname = 'package-lock.json' if filename.endswith('.json') else filename
        with open(fname, 'wb') as f:
            f.write(data)
        args = 'java -Djava.net.useSystemProxies=true -jar dash.jar -summary DEPENDENCIES ' + fname
        subprocess.run(args, shell=True, check=False)
        os.remove(fname)
        
        # append to outfile 
        with open('DEPENDENCIES', 'r') as infile:
            with open(sys.argv[2], 'a+') as outfile:
                outfile.write(infile.read())
        os.remove("DEPENDENCIES")