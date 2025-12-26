#!/bin/bash

# Script de compilación DEBUG para Android TV Remote
# Compilación con arquitectura debug

set -e

echo "========================================="
echo "   TV Remote - Debug Build Script"
echo "========================================="

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

# Verificar gradlew
if [ ! -f "gradlew" ]; then
    echo "❌ gradlew no encontrado"
    exit 1
fi

# Compilar APK debug
echo "✓ Compilando APK debug..."
./gradlew assembleDebug

# Resultado
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo ""
    echo "✅ ¡Compilación DEBUG exitosa!"
else
    echo ""
    echo "❌ Error en compilación"
    exit 1
fi
