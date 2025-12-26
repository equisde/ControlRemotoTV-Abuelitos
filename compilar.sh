#!/bin/bash

# Script para compilar la app Control Remoto TV

echo "🚀 Iniciando compilación de Control Remoto TV..."
echo ""

# Verificar que Gradle esté disponible
if ! command -v gradle &> /dev/null && [ ! -f "gradlew" ]; then
    echo "❌ Error: Gradle no está instalado"
    echo "Instala Gradle o usa el gradle wrapper (gradlew)"
    exit 1
fi

# Usar gradlew si existe, si no, usar gradle
if [ -f "gradlew" ]; then
    GRADLE_CMD="./gradlew"
else
    GRADLE_CMD="gradle"
fi

# Hacer el archivo ejecutable
if [ -f "gradlew" ]; then
    chmod +x gradlew
fi

echo "📦 Compilando APK de release..."
$GRADLE_CMD clean assembleRelease

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ ¡Compilación exitosa!"
    echo "📱 APK guardado en: app/build/outputs/apk/release/app-release-unsigned.apk"
    echo ""
    echo "Para firmar el APK:"
    echo "  jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \\"
    echo "    -keystore tu_keystore.keystore \\"
    echo "    app/build/outputs/apk/release/app-release-unsigned.apk \\"
    echo "    tu_alias"
else
    echo ""
    echo "❌ Error durante la compilación"
    exit 1
fi
