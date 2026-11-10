#!/usr/bin/env bash
set -euo pipefail
cd -- "$(dirname -- "$0")"
mkdir -p bin
javac --release 17 -encoding UTF-8 -d bin src/*.java
java -Dfile.encoding=UTF-8 -cp bin Aplicacion
