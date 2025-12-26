#!/bin/bash

# Script para generar keystores y secretos para GitHub Actions
# NO contiene contraseñas - las genera de forma segura

echo "======================================"
echo "GitHub Secrets Generator"
echo "======================================"
echo ""

# Generar contraseñas seguras
DEBUG_PASS="android"
RELEASE_PASS=$(openssl rand -base64 32 | tr -dc 'a-zA-Z0-9' | head -c 24)

echo "🔑 Generando keystores..."

# Debug keystore
keytool -genkey -v -keystore debug.keystore \
  -alias androiddebugkey \
  -keyalg RSA -keysize 2048 \
  -validity 10000 \
  -storepass "$DEBUG_PASS" \
  -keypass "$DEBUG_PASS" \
  -dname "CN=Android Debug,O=ControlTV,C=US"

# Release keystore
keytool -genkey -v -keystore release.keystore \
  -alias release \
  -keyalg RSA -keysize 2048 \
  -validity 10000 \
  -storepass "$RELEASE_PASS" \
  -keypass "$RELEASE_PASS" \
  -dname "CN=Control TV Abuelitos,O=ControlTV,C=US"

echo ""
echo "✅ Keystores generados"
echo ""
echo "📦 Convirtiendo a Base64..."

DEBUG_B64=$(base64 -w 0 debug.keystore)
RELEASE_B64=$(base64 -w 0 release.keystore)

echo ""
echo "🔐 Configurando GitHub Secrets..."

gh secret set DEBUG_KEYSTORE_BASE64 -b "$DEBUG_B64"
gh secret set DEBUG_KEYSTORE_PASSWORD -b "$DEBUG_PASS"
gh secret set DEBUG_KEY_ALIAS -b "androiddebugkey"
gh secret set DEBUG_KEY_PASSWORD -b "$DEBUG_PASS"

gh secret set RELEASE_KEYSTORE_BASE64 -b "$RELEASE_B64"
gh secret set RELEASE_KEYSTORE_PASSWORD -b "$RELEASE_PASS"
gh secret set RELEASE_KEY_ALIAS -b "release"
gh secret set RELEASE_KEY_PASSWORD -b "$RELEASE_PASS"

echo ""
echo "✅ GitHub Secrets configurados"
echo ""
echo "⚠️  IMPORTANTE: Guarda la contraseña del release keystore:"
echo "   Release Password: $RELEASE_PASS"
echo ""
echo "🔒 Haz backup del release.keystore - lo necesitarás para actualizaciones"

# Limpiar
rm -f debug.keystore release.keystore

echo ""
echo "======================================"
echo "✅ Configuración completada"
echo "======================================"
