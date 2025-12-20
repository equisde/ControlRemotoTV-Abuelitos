# Changelog

## v1.1 - 2025-12-20

### 🚀 Actualizaciones Mayores

**Gradle y Herramientas de Build:**
- ✅ Gradle actualizado de 8.2 a **8.11.1** (última versión estable)
- ✅ Android Gradle Plugin actualizado de 8.1.4 a **8.7.3**
- ✅ JDK actualizado de 17 a **21** en GitHub Actions
- ✅ Kotlin agregado con versión **2.1.0**

**Android SDK:**
- ✅ compileSdk actualizado de 34 a **35** (Android 15)
- ✅ targetSdk actualizado de 34 a **35**
- ✅ minSdk permanece en 21 (Android 5.0) para compatibilidad

**Dependencias de AndroidX (todas actualizadas a últimas versiones):**
- ✅ androidx.core:core-ktx: **1.15.0** (nueva)
- ✅ androidx.appcompat: 1.6.1 → **1.7.0**
- ✅ material: 1.9.0 → **1.12.0**
- ✅ constraintlayout: 2.1.4 → **2.2.0**
- ✅ lifecycle-runtime-ktx: **2.8.7** (nueva)

**Optimizaciones de Gradle:**
- ✅ Parallel builds habilitados
- ✅ Build cache habilitado
- ✅ Configure on demand habilitado
- ✅ Memoria aumentada a 4GB
- ✅ nonTransitiveRClass habilitado para builds más rápidos

**Java:**
- ✅ sourceCompatibility y targetCompatibility actualizados a **Java 17**

### 📝 Mejoras

- ✅ GitHub Actions optimizado con `--no-daemon` para CI
- ✅ Artifact retention configurado a 30 días
- ✅ BuildConfig habilitado explícitamente
- ✅ Kotlin code style configurado

### 🎯 Compatibilidad

- ✅ **Android 5.0 (API 21) hasta Android 15 (API 35)**
- ✅ Compatible con todas las versiones modernas de Android
- ✅ Optimizado para dispositivos de 2015 en adelante

---

## v1.0 - 2025-12-20

### 🎉 Lanzamiento Inicial

- ✅ Control remoto para Android TV
- ✅ Interfaz diseñada para personas mayores
- ✅ Botones grandes (80-90dp)
- ✅ Texto grande (20-28sp)
- ✅ Búsqueda automática de TV
- ✅ Documentación completa en español
