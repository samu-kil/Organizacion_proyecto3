#!/usr/bin/env bash
# Uso: ./ensamblador.sh ruta/Prog.asm   (genera ruta/Prog.hack)
DIR="$(cd "$(dirname "$0")" && pwd)"
[ -d "$DIR/bin" ] || "$DIR/compilar.sh" || exit 1
java -cp "$DIR/bin" ensamblador.Main "$1"
