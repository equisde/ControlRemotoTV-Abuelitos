# 🔨 GUÍA DE COMPILACIÓN - ANDROID TV REMOTE v2.2

## ⚠️ SITUACIÓN ACTUAL

La compilación en **Termux falla** por incompatibilidad de versiones:
- Gradle 9.2.0 disponible en Termux
- Android Gradle Plugin 7.4.0+ requiere actualizaciones que Gradle 9.2.0 no soporta

**Error específico:**
```
'org.gradle.api.artifacts.Dependency org.gradle.api.artifacts.dsl.
 DependencyHandler.module(java.lang.Object)' not found
```

---

## ✅ SOLUCIONES PARA COMPILAR

### OPCIÓN 1: Android Studio (RECOMENDADO)

**Requisitos:**
- Android Studio Flamingo o superior
- 4GB RAM libre
- 5GB espacio en disco

**Pasos:**
```bash
1. Descarga Android Studio en tu PC/Linux
2. Abre el proyecto TVRemote
3. Android Studio descargará automáticamente:
   - Android SDK
   - Gradle 8.7.1 (compatible)
   - NDK si es necesario
4. Build → Make Project
5. APK generado en: app/build/outputs/apk/debug/app-debug.apk
```

**Ventajas:**
- ✅ Compilación garantizada
- ✅ Depuración visual
- ✅ Incremental builds
- ✅ Emulador integrado

---

### OPCIÓN 2: Linux/Mac con Gradle nativo

**Requisitos:**
- Java 17+ instalado
- Gradle 8.7.1 (no 9.2.0)
- Android SDK

**Pasos:**
```bash
# 1. Descargar Gradle 8.7.1
wget https://services.gradle.org/distributions/gradle-8.7.1-bin.zip
unzip gradle-8.7.1-bin.zip
export PATH=$PWD/gradle-8.7.1/bin:$PATH

# 2. Descargar Android SDK
# Desde https://developer.android.com/studio

# 3. Configurar SDK
export ANDROID_HOME=$PWD/android-sdk
export PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$PATH

# 4. Compilar
cd TVRemote
./gradlew assembleDebug

# 5. APK en
# app/build/outputs/apk/debug/app-debug.apk
```

---

### OPCIÓN 3: Windows con Android Studio

**Requisitos:**
- Windows 10/11
- Android Studio
- Java 17+

**Pasos:**
```
1. Instala Android Studio desde developer.android.com
2. Abre TVRemote en Android Studio
3. Build → Build APK(s)
4. Espera compilación
5. APK listo para instalar
```

---

### OPCIÓN 4: GitHub Actions (CI/CD)

**Crea un archivo `.github/workflows/build.yml`:**

```yaml
name: Build APK

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: 17
      - run: chmod +x gradlew
      - run: ./gradlew assembleDebug
      - uses: actions/upload-artifact@v3
        with:
          name: app-debug.apk
          path: app/build/outputs/apk/debug/
```

**Ventajas:**
- ✅ Compilación automática
- ✅ APK disponible en cada push
- ✅ Gratuito para repos públicos

---

## 📦 DESCARGA PRE-COMPILADA

Si deseas el APK sin compilar:

1. **Espera a que se compile en CI/CD**
   - GitHub Actions compila automáticamente
   - Descarga desde artifacts

2. **Solicita el APK compilado**
   - Contacta al desarrollador
   - APK disponible en releases

---

## 🔍 ESTRUCTURA DEL PROYECTO

```
TVRemote/
├── app/
│   ├── src/main/
│   │   ├── java/com/remotetv/control/
│   │   │   ├── MainActivity.java
│   │   │   ├── RemoteActivity.java
│   │   │   ├── SettingsActivity.java
│   │   │   ├── TVAppsActivity.java
│   │   │   ├── AndroidTVRemoteProtocol.java
│   │   │   ├── TVAppManager.java
│   │   │   ├── TVAppsAdapter.java
│   │   │   ├── TVDiscovery.java
│   │   │   ├── PreferencesManager.java
│   │   │   └── ElderlyAccessibilityManager.java
│   │   └── res/layout/
│   │       ├── activity_main_elderly.xml
│   │       ├── activity_remote_elderly.xml
│   │       ├── activity_apps.xml
│   │       └── item_app.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
├── gradlew
└── gradle/wrapper/gradle-wrapper.properties
```

---

## 🚀 DESPUÉS DE COMPILAR

### Instalar en dispositivo:

```bash
# Vía ADB (si tienes acceso)
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Vía archivo
1. Copia app-debug.apk a tu teléfono
2. Abre el archivo en el teléfono
3. Instala la app
4. Concede permisos
```

### Usar la app:

```
1. Abre Control Remoto TV
2. Presiona "ESCANEAR TV" o ingresa IP manualmente
3. Se conecta con el TV
4. Ahora puedes:
   - Controlar con D-Pad
   - Cambiar volumen
   - Abrir apps (Netflix, YouTube, etc.)
   - Abrir TODAS las apps del TV
```

---

## ⚠️ POR QUÉ FALLA EN TERMUX

**Causa raíz:**
Termux tiene Gradle 9.2.0 que incluye cambios que rompen compatibilidad con plugins de Android antiguos.

**Solución:**
Usar Gradle 8.7.1 LTS (Long Term Support) que es compatible con AGP 7.x

**Por qué no "arreglamos" en Termux:**
- Requeriría downgrade de Gradle en todo Termux
- Afectaría otros proyectos
- Gradle 9.2.0 es más nuevo y mejor
- Mejor compilar en PC/CI/CD

---

## 📋 CHECKLIST DE COMPILACIÓN

### Android Studio
- [ ] Descargar Android Studio
- [ ] Instalar Java 17+
- [ ] Descargar Android SDK
- [ ] Abrir proyecto TVRemote
- [ ] Build → Make Project
- [ ] Esperar compilación
- [ ] APK en app/build/outputs/apk/debug/

### Linux/Mac
- [ ] Instalar Java 17+
- [ ] Descargar Gradle 8.7.1
- [ ] Descargar Android SDK
- [ ] Configurar variables de entorno
- [ ] ./gradlew assembleDebug
- [ ] APK listo

### GitHub Actions
- [ ] Crear repo en GitHub
- [ ] Crear `.github/workflows/build.yml`
- [ ] Push a GitHub
- [ ] Ver compilación en Actions
- [ ] Descargar APK de artifacts

---

## 🎯 RESUMEN

| Método | Dificultad | Tiempo | Recomendado |
|--------|-----------|--------|-------------|
| Android Studio | Baja | 10 min | ✅ SÍ |
| Linux nativo | Media | 15 min | ✅ SÍ |
| GitHub Actions | Media | 5 min (auto) | ✅ SÍ |
| Windows | Baja | 10 min | ✅ SÍ |
| Termux | Muy Alta | ∞ | ❌ NO |

---

## 💡 RECOMENDACIÓN FINAL

**Mejor opción: Android Studio**
1. Descargas Android Studio (una sola vez)
2. Abres el proyecto
3. Android Studio lo arregla todo automáticamente
4. Haces Build y listo
5. Instalar en TV y disfrutar

**Tiempo total:** ~20 minutos (incluyendo descarga de Android Studio)

---

**Versión:** 2.2  
**Fecha:** 26 de Diciembre de 2025  
**Proyecto:** Control Remoto Android TV  
**Licencia:** MIT

