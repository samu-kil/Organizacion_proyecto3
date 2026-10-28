#!/usr/bin/env bash
# Uso: ./desensamblador.sh ruta/Prog.hack   (genera ruta/ProgDis.asm)
DIR="$(cd "$(dirname "$0")" && pwd)"
[ -d "$DIR/bin" ] || "$DIR/compilar.sh" || exit 1
java -cp "$DIR/bin" desensamblador.Main "$1"
