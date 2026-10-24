#!/bin/bash
# MediCart Run Script for macOS / Linux

cd "$(dirname "$0")"

echo "=========================================="
echo "   MediCart - Medicine Ordering App (macOS)"
echo "=========================================="
echo ""

# 1. Check Java
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH!"
    echo "Please install JDK from https://adoptium.net or via Homebrew: brew install openjdk"
    exit 1
fi

echo "[1/2] Compiling Java files..."
javac -cp ".:lib/mysql-connector-j-26.7.0.jar" Main.java ui/*.java dao/*.java model/*.java service/*.java database/*.java
if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed! Check the errors above."
    exit 1
fi
echo "     Compiled successfully."

echo "[2/2] Launching MediCart..."
echo ""
java -cp ".:lib/mysql-connector-j-26.7.0.jar" -Xdock:name="MediCart" Main
