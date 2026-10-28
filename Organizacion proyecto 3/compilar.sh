#!/usr/bin/env bash
# Compila ensamblador/ y desensamblador/ (si existe) en bin/.
set -e
DIR="$(cd "$(dirname "$0")" && pwd)"
mkdir -p "$DIR/bin"
FUENTES=("$DIR"/ensamblador/*.java)
if ls "$DIR"/desensamblador/*.java >/dev/null 2>&1; then
    FUENTES+=("$DIR"/desensamblador/*.java)
fi
javac -encoding UTF-8 -d "$DIR/bin" "${FUENTES[@]}"
