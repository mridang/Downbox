#!/bin/bash
java -Dunixfs=false -Xmx256m -Dapplication.deployment=deb -Djna.library.path=/usr/share/downbox -Djava.library.path=/usr/share/downbox -jar /usr/share/downbox/Downbox.jar "$@"
