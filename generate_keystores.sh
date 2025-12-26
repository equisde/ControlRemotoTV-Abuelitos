#!/bin/bash

# Script para generar keystores para firma de APKs
# Control Remoto TV Abuelitos

echo "=== Generador de Keystores para APK ==="
echo ""

# Verificar si keytool está disponible
if ! command -v keytool &> /dev/null; then
    echo "❌ Error: keytool no encontrado. Instala Java Development Kit (JDK)"
    exit 1
fi

# Crear directorio para keystores
mkdir -p keystores
cd keystores

# Generar Release Keystore
echo "Generando Release Keystore..."
echo "Responde las preguntas siguientes:"
echo ""

keytool -genkey -v -keystore release.keystore \
  -alias release-key \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Release Keystore generado exitosamente"
    echo ""
    echo "Codificando a Base64 para GitHub Secrets..."
    cat release.keystore | base64 -w 0 > release_keystore_base64.txt
    echo ""
    echo "Contenido a copiar en RELEASE_KEYSTORE secret:"
    echo "================================================"
    cat release_keystore_base64.txt
    echo ""
    echo "================================================"
    echo ""
    echo "Archivo guardado en: keystores/release_keystore_base64.txt"
else
    echo "❌ Error al generar Release Keystore"
    exit 1
fi

echo ""
echo "Próximos pasos:"
echo "1. Copia el contenido anterior"
echo "2. Ve a GitHub → Settings → Secrets and variables → Actions"
echo "3. Crea un nuevo secret llamado 'RELEASE_KEYSTORE' y pega el contenido"
echo "4. Agrega los demás secrets (contraseñas, alias, etc.)"
echo ""
