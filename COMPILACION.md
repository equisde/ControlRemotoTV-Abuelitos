# Guía Detallada de Compilación

## 📦 Requisitos Previos

### Hardware
- Computadora con 4GB+ RAM
- 2GB espacio libre

### Software Instalado
```bash
# Verificar Java
java -version
# Debe ser Java 11 o superior

# Verificar Gradle (opcional, usamos wrapper)
gradle -version
```

---

## 🔧 Método 1: Android Studio (Recomendado)

### Paso 1: Abrir Proyecto

1. **Abre Android Studio**
2. **Selecciona**: File → Open
3. **Navega a**: TVRemote
4. **Presiona**: Open

Android Studio detectará automáticamente que es un proyecto Gradle.

### Paso 2: Esperar Indexación

```
Android Studio indexará los archivos...
(Puede tomar 1-2 minutos la primera vez)
```

### Paso 3: Compilar

```
Opción A: Menú
  Build → Make Project (Ctrl+F9 en Windows, Cmd+F9 en Mac)

Opción B: Shortcut
  Presiona: Ctrl+F9 (Windows) o Cmd+F9 (Mac)
```

### Paso 4: Ver Resultado

```
En la consola inferior aparecerá:
✓ BUILD SUCCESSFUL
```

El APK estará en:
```
TVRemote/app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔧 Método 2: Línea de Comandos (Linux/Mac)

### Paso 1: Navega al Directorio

```bash
cd ~/TVRemote
```

### Paso 2: Dale Permisos al Script

```bash
chmod +x gradlew
chmod +x build.sh
```

### Paso 3: Compilar Opción A (Automático)

```bash
./build.sh
```

Este script hace todo automáticamente:
- Limpia builds anteriores
- Compila el proyecto
- Genera APK

### Paso 3 Alternativo: Compilar Opción B (Manual)

```bash
# Limpiar (opcional)
./gradlew clean

# Compilar
./gradlew assembleDebug

# La salida será:
# BUILD SUCCESSFUL in XXs
```

### Paso 4: Localizar APK

```bash
ls -lh app/build/outputs/apk/debug/
```

APK ubicado en:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔧 Método 3: Línea de Comandos (Windows)

### Paso 1: Abre PowerShell

```powershell
# O CMD si prefieres
```

### Paso 2: Navega al Directorio

```powershell
cd C:\Users\TuUsuario\TVRemote
```

### Paso 3: Compilar

```powershell
# Opción A: Automático
.\build.sh

# Opción B: Manual
.\gradlew.bat assembleDebug
```

### Paso 4: Resultado

```
BUILD SUCCESSFUL
APK: TVRemote\app\build\outputs\apk\debug\app-debug.apk
```

---

## 📱 Instalar en el Teléfono

### Método 1: Con Android Studio

```
1. En Android Studio:
   Run → Run 'app' (Shift+F10)

2. Selecciona tu dispositivo

3. La app se instala automáticamente
```

### Método 2: Con ADB

```bash
# Asegúrate que tu teléfono está conectado
adb devices

# Instalar APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Resultado esperado:
# Success
```

### Método 3: Manualmente

```bash
# 1. Copiar APK a teléfono
adb push app/build/outputs/apk/debug/app-debug.apk /sdcard/Download/

# 2. En el teléfono:
#    - Abre Archivos
#    - Ve a Downloads
#    - Abre app-debug.apk
#    - Instala
```

### Método 4: Sin ADB

```
1. Conecta teléfono a PC por USB (como disco)

2. Copia app-debug.apk a: /sdcard/Download/

3. En teléfono:
   - Abre Archivos/Gestor de Archivos
   - Ve a Downloads
   - Abre app-debug.apk
   - Presiona "Instalar"

4. Permite la instalación

5. ¡Listo!
```

---

## ⚠️ Solución de Problemas de Compilación

### Error: "Gradle not found"

```bash
# Solución: Asegúrate de tener el wrapper
ls -la gradlew

# Si no existe, descargalo:
# (Usualmente no es necesario, viene con el proyecto)
```

### Error: "SDK not found"

```bash
# En Android Studio:
# File → Project Structure → SDK Location
# Configura la ruta del SDK

# O:
export ANDROID_SDK_ROOT=/ruta/al/sdk
```

### Error: "Java version mismatch"

```bash
# Verificar versión Java
java -version

# Debe ser 11 o superior
# Si es muy antigua, actualiza Java

# Alternativamente, en Android Studio:
# File → Project Structure → JDK Location
```

### Error: "Out of Memory"

```bash
# Aumentar memoria para Gradle
export GRADLE_OPTS="-Xmx2048m"

# Luego compilar de nuevo
./gradlew build
```

### Error: "Network issues"

```bash
# A veces las dependencias no descargan bien

# Solución 1: Limpiar caché
./gradlew clean

# Solución 2: Reintentar
./gradlew build

# Solución 3: Caché offline
./gradlew build --offline
```

---

## 🔍 Verificar Compilación

### Después de compilar, verifica:

```bash
# Comprobar que APK existe
ls -lh app/build/outputs/apk/debug/app-debug.apk

# Comprobar tamaño (debe ser ~5-10 MB)
# Si es mayor, algo no está bien

# Ver contenido del APK
unzip -l app/build/outputs/apk/debug/app-debug.apk | head
```

---

## 📊 Compilación Optimizada (Release)

### Para una versión de producción:

```bash
# Compilar versión release (optimizada)
./gradlew assembleRelease

# APK estará en:
# app/build/outputs/apk/release/app-release-unsigned.apk
```

**Nota**: Versión release requiere firma con keystroke para instalar.

Para desarrollo, usa **debug** (que no requiere firma).

---

## 🔄 Compilación Incremental

### Para hacer más rápida la compilación:

```bash
# Estas opciones hacen la compilación más rápida:
./gradlew build --parallel --max-workers=4

# O habilitar daemon de Gradle:
echo "org.gradle.daemon=true" >> gradle.properties
```

---

## 📋 Checklist de Compilación

- [ ] Java 11+ instalado
- [ ] Android SDK descargado
- [ ] Proyecto abierto en Android Studio O línea de comandos
- [ ] Dependencias descargadas (primera compilación)
- [ ] Compilación completada sin errores
- [ ] APK generado en: `app/build/outputs/apk/debug/`
- [ ] APK instalado en teléfono
- [ ] Aplicación abre correctamente
- [ ] Puedes conectar con un TV

---

## 💻 Comandos Útiles

```bash
# Ver estado del proyecto
./gradlew tasks

# Limpiar todo
./gradlew clean

# Compilar sin instalar
./gradlew assemble

# Compilar e instalar en dispositivo conectado
./gradlew installDebug

# Ver tamaño del APK
ls -lh app/build/outputs/apk/debug/app-debug.apk

# Ver logs de compilación
./gradlew build --stacktrace

# Compilación paralela (más rápida)
./gradlew build --parallel
```

---

## 🎯 Proximos Pasos

Una vez compilada la aplicación:

1. **Instala** en tu teléfono
2. **Abre** la aplicación
3. **Escanea** la red para encontrar tu TV
4. **Conecta** y **controla** tu TV

Ver instrucciones en: QUICK_START.md

---

## 📚 Referencias

- Documentación oficial Gradle: https://gradle.org/
- Android Developers: https://developer.android.com/
- Android Studio: https://developer.android.com/studio

---

**Última actualización**: 26 de Diciembre de 2025
