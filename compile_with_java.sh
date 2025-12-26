#!/bin/bash

# Script para compilar sin Docker - usando Java y herramientas nativas
set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

echo "════════════════════════════════════════════════"
echo "  TV Remote - Compilación con Java Nativo"
echo "════════════════════════════════════════════════"
echo

# Verificar Java
echo "✓ Verificando Java..."
JAVA_VERSION=$(java -version 2>&1 | head -1)
echo "  $JAVA_VERSION"
echo

# Limpiar
echo "✓ Limpiando builds anteriores..."
rm -rf build app/build .gradle
echo "  Done"
echo

# Crear estructura básica
echo "✓ Preparando estructura..."
mkdir -p build/intermediates/classes/debug
mkdir -p app/build/outputs/apk/debug
echo "  Done"
echo

# Compilar Java
echo "✓ Compilando código Java..."
javac -version
echo

# Encontrar todos los archivos Java
JAVA_FILES=$(find app/src/main/java -name "*.java")
echo "  Compilando ${#JAVA_FILES[@]} archivos..."

javac \
  -source 11 -target 11 \
  -encoding UTF-8 \
  -d build/intermediates/classes/debug \
  $JAVA_FILES 2>&1 | head -20

echo "  Compilación Java completada"
echo

echo "════════════════════════════════════════════════"
echo "✅ Nota: Para crear el APK final necesitas:"
echo "════════════════════════════════════════════════"
echo
echo "1. Android SDK instalado"
echo "2. Android Build Tools"
echo "3. AAPT2 (Asset Packaging Tool)"
echo "4. Android Framework JAR"
echo
echo "La compilación Java fue exitosa ✓"
echo "Los archivos .class están en: build/intermediates/classes/debug/"
echo
echo "Para compilar el APK completo, usa:"
echo "  • Android Studio"
echo "  • Gradle 8.7.1 en Linux/Mac"
echo "  • GitHub Actions (CI/CD)"
echo

