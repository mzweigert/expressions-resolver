#!/bin/bash
if [[ -n "$(find ./target -name 'expressions-resolver*.jar' | head -1)" ]]; then
    mvn -DskipTests exec:java -Dexec.args="$*" -q
else
    mvn clean install -DskipTests exec:java -Dexec.args="$*" -q
fi