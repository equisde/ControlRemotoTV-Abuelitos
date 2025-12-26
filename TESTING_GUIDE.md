# 🧪 Guía de Testing: GitHub Actions

Esta guía te ayuda a verificar que GitHub Actions funciona correctamente sin necesidad de configurar un keystore.

## Test 1: Verificar que el Workflow se Ejecuta

### Opción A: Desencadenar manualmente (sin cambios de código)

1. Ve a GitHub → **Actions** tab
2. Selecciona el workflow **"Build APK"**
3. Click en **"Run workflow"** → **Run workflow**

El workflow debería empezar a compilar en ~10 segundos.

### Opción B: Con un cambio simple

```bash
cd ControlRemotoTV-Abuelitos

# Haz un cambio trivial
echo "# Test" >> README.md

# Push
git add README.md
git commit -m "Test: Trigger workflow"
git push origin master
```

## Test 2: Verificar Debug Build (sin keystore)

El Debug build no requiere secretos configurados.

1. Espera 2-5 minutos para que el workflow termine
2. Ve a la ejecución del workflow
3. Verifica que **build-debug** esté en ✅

**Output esperado**: `debug-apk` artifact

Si ves:
- ✅ Verde = OK, Debug compile correctamente
- ❌ Rojo = Error, lee los logs

## Test 3: Verificar Release Build (requiere secrets)

El Release build necesita los 4 secrets configurados.

1. Primero genera y agrega los secrets (ver QUICK_SETUP_GITHUB_ACTIONS.md)
2. Espera el siguiente push
3. Verifica que **build-release** esté en ✅

**Output esperado**: `release-apk` artifact

## Test 4: Descargar y Verificar APKs

Después de un build exitoso:

```
GitHub Repo → Actions Tab
    ↓
Haz click en el workflow (commit message)
    ↓
Baja hasta "Artifacts"
    ↓
Descarga "debug-apk" o "release-apk"
    ↓
Descomprime el .zip
    ↓
Tendrás archivo .apk
```

### Verificar el APK descargado

```bash
# Listar el contenido
unzip -l app-debug.apk | head -20

# Ver tamaño
ls -lh app-debug.apk

# Instalar (si tienes adb)
adb install app-debug.apk
```

## 🔴 Solución de Problemas en Testing

### "build-debug tiene ❌ (error)"

Mira los logs del workflow:

1. Ve a Actions → Click en la ejecución
2. Click en "build-debug" job
3. Abre "Build Debug APK" step
4. Lee el error

**Errores comunes**:
- Falta Gradle Wrapper: Resuelve `./gradlew` permissions
- Falta dependencias: Ejecuta `./gradlew assemble --offline` localmente
- Java version: Verifica que sea Java 11+

### "build-release tiene ❌ (error) pero build-debug está OK"

Significa que los secretos no están configurados correctamente:

1. Verifica que los 4 secrets existan:
   - `RELEASE_KEYSTORE`
   - `RELEASE_KEYSTORE_PASSWORD`
   - `RELEASE_KEY_ALIAS`
   - `RELEASE_KEY_PASSWORD`

2. Lee el error en los logs (busca "keystore" o "password")

3. Regenera los secretos con `./generate_keystores.sh`

### "build-release no comienza"

Posibles razones:

```
1. Secrets no configurados
   → Agrega los 4 secrets en Settings

2. Workflow no incluye master
   → Verifica que .github/workflows/build.yml tenga "master" en "branches:"

3. Branch name diferente
   → Comprueba si usas "master" o "main"
```

## ✅ Checklist de Funcionamiento

- [ ] Workflow "Build APK" aparece en Actions tab
- [ ] build-debug compila exitosamente (sin secrets)
- [ ] build-debug genera artifact "debug-apk"
- [ ] El debug-apk es un archivo .apk válido
- [ ] Puedes descargar los artifacts
- [ ] build-release compila exitosamente (con secrets)
- [ ] build-release genera artifact "release-apk"
- [ ] El release-apk está firmado (puede instalarse múltiples veces)

## 📊 Tiempos Esperados

| Paso | Tiempo |
|------|--------|
| Checkout código | ~5 seg |
| Setup Java | ~20 seg |
| Build Debug | ~60-90 seg |
| Build Release | ~60-90 seg |
| Upload artifacts | ~10 seg |
| **Total** | **2-5 minutos** |

## 🎯 Test Final: Instalación

```bash
# Descarga el release-apk desde Actions

# Descomprime
unzip release-apk.zip

# Instala (opción 1: con adb)
adb install app-release.apk

# Instala (opción 2: sin adb)
# Copia app-release.apk a tu teléfono
# Abre con File Manager y toca para instalar
```

---

**Notas**:
- Los tests de debug son rápidos y no requieren secretos
- Los tests de release requieren secrets configurados
- Puedes repetir estos tests tantas veces quieras
- No hay límite de compilaciones en GitHub Actions (para públicos)

¡Listo para testear! 🚀
