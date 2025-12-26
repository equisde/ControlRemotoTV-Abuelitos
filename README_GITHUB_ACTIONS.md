# Control Remoto TV para Abuelos 📺

Una aplicación Android accesible y fácil de usar para controlar televisores Android TV antiguos sin ADB inalámbrico.

## 🎯 Características

### Diseño para Personas Mayores
- **Botones GRANDES** - Fáciles de ver y tocar
- **Texto GRANDE** - Claridad visual máxima
- **Colores Contrastantes** - Alto contraste para mejor legibilidad
- **Menos opciones** - Interfaz simplificada sin distracciones
- **Retroalimentación Visual** - Confirmación de cada acción

### Funciones Principales
- 🎮 Control remoto completo (arriba, abajo, izquierda, derecha, OK)
- 📱 Lanzador de aplicaciones del TV
- 📺 Control de volumen
- ⏻️ Encender/apagar
- 🔙 Botón atrás
- 🏠 Botón inicio
- 📜 Listar y abrir apps instaladas en el TV
- 🔊 Control de sonido
- 💡 Luz de pantalla accesible

## 📋 Requisitos

- Android 5.0 (API 21) o superior
- Conexión WiFi en la misma red que el TV
- Android TV con soporte para HDMI CEC o ADB WiFi (para TVs más modernos)

## 🚀 Instalación

### Opción 1: Descargar APK Compilado
Los APKs compilados están disponibles en la sección **Releases** del repositorio.

### Opción 2: Compilar Desde Código

```bash
# Clonar el repositorio
git clone https://github.com/equisde/ControlRemotoTV-Abuelitos.git
cd ControlRemotoTV-Abuelitos

# Compilar Debug APK
./gradlew assembleDebug

# Compilar Release APK (requiere keystore configurado)
./gradlew assembleRelease
```

## 🔐 Configuración de GitHub Actions + GitHub Secrets

Este repositorio está configurado para compilar automáticamente APKs en cada push.

### Paso 1: Generar Keystores

```bash
./generate_keystores.sh
```

### Paso 2: Configurar Secrets en GitHub

1. Ve a **Settings** → **Secrets and variables** → **Actions**
2. Agrega los siguientes secrets:

| Secret | Descripción |
|--------|-------------|
| `RELEASE_KEYSTORE` | Keystore en Base64 |
| `RELEASE_KEYSTORE_PASSWORD` | Contraseña del keystore |
| `RELEASE_KEY_ALIAS` | Alias de la clave (ej: release-key) |
| `RELEASE_KEY_PASSWORD` | Contraseña de la clave privada |

### Paso 3: Hacer Push

El workflow compilará automáticamente:

```bash
git add .
git commit -m "Configurar GitHub Actions"
git push origin main
```

### Paso 4: Descargar APKs

1. Ve a la pestaña **Actions** del repositorio
2. Haz clic en el último workflow ejecutado
3. En **Artifacts**, descarga:
   - `debug-apk` (sin firma, para testing)
   - `release-apk` (firmado, listo para publicar)

## 🛠️ Estructura del Proyecto

```
ControlRemotoTV-Abuelitos/
├── .github/workflows/
│   └── build.yml              # GitHub Actions workflow
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/          # Código Kotlin/Java
│   │   │   └── res/           # Recursos (layouts, strings, etc)
│   │   └── test/              # Tests
│   └── build.gradle           # Configuración Gradle
├── gradle/                    # Wrapper de Gradle
├── generate_keystores.sh      # Script para generar keystores
├── GITHUB_ACTIONS_SETUP.md    # Guía de configuración
└── README.md                  # Este archivo
```

## 📝 Notas Importantes

### Seguridad
- ⚠️ **NUNCA** commits los archivos `.keystore`
- Los secrets son privados y no aparecen en logs
- Guarda las contraseñas en lugar seguro

### Consistencia de Firma
- Usa el mismo keystore para todas las compilaciones
- Esto previene conflictos al actualizar la app
- El workflow almacena automáticamente la firma

### Debug vs Release
- **Debug APK**: Para testing, sin firma requerida
- **Release APK**: Firmado con tu keystore, listo para publicar

## 🐛 Solución de Problemas

### "Build failed: Keystore not found"
```bash
# Regenera los secrets en GitHub
./generate_keystores.sh
```

### "Invalid keystore password"
- Verifica que la contraseña sea exacta
- Recuerde: mayúsculas/minúsculas importan

### APK no se instala
- Asegúrate de desinstalar versiones anteriores
- Usa el mismo keystore para firmar

## 📞 Contacto y Soporte

Para reportar problemas o sugerencias, abre un **Issue** en GitHub.

## 📄 Licencia

Este proyecto está bajo licencia MIT. Ver `LICENSE` para más detalles.

---

**Creado con ❤️ para hacer la tecnología accesible a todos**
