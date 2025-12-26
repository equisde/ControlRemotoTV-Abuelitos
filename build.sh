#!/bin/bash

# Script de compilación para Android TV Remote
# Compila APK sin necesidad de Android Studio

set -e

echo "========================================="
echo "   TV Remote - Build Script"
echo "========================================="

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

# Verificar dependencias
echo "✓ Verificando dependencias..."
if ! command -v gradle &> /dev/null && ! command -v ./gradlew &> /dev/null; then
    echo "❌ Gradle no encontrado. Instala Android SDK."
    exit 1
fi

# Limpiar build anterior
echo "✓ Limpiando builds anteriores..."
./gradlew clean

# Compilar APK
echo "✓ Compilando aplicación..."
./gradlew assembleDebug

# Resultado
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo ""
    echo "✅ ¡Compilación exitosa!"
    echo ""
    echo "APK generado en:"
    echo "  app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "Instala con:"
    echo "  adb install app/build/outputs/apk/debug/app-debug.apk"
    echo "  o copia el APK a tu Android y abre"
else
    echo ""
    echo "❌ Error en compilación"
    exit 1
fi
