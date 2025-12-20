#!/bin/bash

echo "════════════════════════════════════════════════"
echo "  COMPILADOR DE CONTROL REMOTO PARA ABUELITOS"
echo "════════════════════════════════════════════════"
echo ""

cd ~/ControlRemotoTV

# Verificar si existe gradle wrapper
if [ ! -f "gradlew" ]; then
    echo "⚙️  Generando Gradle Wrapper..."
    gradle wrapper
fi

# Dar permisos de ejecución
chmod +x gradlew

echo "📦 Compilando la aplicación..."
echo "   Por favor espera, esto puede tomar unos minutos..."
echo ""

./gradlew clean assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ ¡COMPILACIÓN EXITOSA!"
    echo ""
    echo "📱 El APK se encuentra en:"
    echo "   app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    
    # Intentar copiar a almacenamiento accesible
    if [ -d "$HOME/storage/downloads" ]; then
        cp app/build/outputs/apk/debug/app-debug.apk $HOME/storage/downloads/ControlRemotoTV.apk
        echo "📥 También copiado a:"
        echo "   ~/storage/downloads/ControlRemotoTV.apk"
        echo ""
    fi
    
    echo "🎉 ¡Listo para instalar en tu teléfono!"
    echo ""
    echo "📋 INSTRUCCIONES DE INSTALACIÓN:"
    echo "   1. Transfiere el APK a tu teléfono"
    echo "   2. Abre el archivo APK"
    echo "   3. Permite instalación de fuentes desconocidas"
    echo "   4. Instala la app"
    echo "   5. ¡Disfruta del control remoto!"
    echo ""
else
    echo ""
    echo "❌ Error en la compilación"
    echo "   Revisa los mensajes de error arriba"
    echo ""
fi
