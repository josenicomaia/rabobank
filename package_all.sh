#!/bin/bash
cd "$(dirname "$0")"

export JAVA_HOME=~/.jdks/openjdk-21.0.1

for d in */ ; do
    echo "Building $d..." &&
      cd $d &&
      ./mvnw package -Dmaven.test.skip=true &&
      cd ..
done
