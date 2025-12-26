# Configuración de GitHub Secrets para Firma de APKs

Este documento contiene los valores que debes configurar como **GitHub Secrets** en tu repositorio para que GitHub Actions pueda compilar y firmar los APKs automáticamente.

## Paso 1: Acceder a GitHub Secrets

1. Ve a tu repositorio: https://github.com/equisde/ControlRemotoTV-Abuelitos
2. Click en **Settings** (Configuración)
3. En el menú lateral, click en **Secrets and variables** → **Actions**
4. Click en **New repository secret** para cada secreto

## Paso 2: Crear los siguientes secrets

### Para Debug Build:

**Secret Name:** `DEBUG_KEYSTORE`  
**Value:** Ver archivo `debug_keystore_base64.txt`

**Secret Name:** `DEBUG_KEYSTORE_PASSWORD`  
**Value:** `android`

**Secret Name:** `DEBUG_KEY_ALIAS`  
**Value:** `androiddebugkey`

**Secret Name:** `DEBUG_KEY_PASSWORD`  
**Value:** `android`

---

### Para Release Build:

**Secret Name:** `RELEASE_KEYSTORE`  
**Value:** Ver archivo `release_keystore_base64.txt`

**Secret Name:** `RELEASE_KEYSTORE_PASSWORD`  
**Value:** `ControlRemotoAbuelitos2024!`

**Secret Name:** `RELEASE_KEY_ALIAS`  
**Value:** `controlremoto`

**Secret Name:** `RELEASE_KEY_PASSWORD`  
**Value:** `ControlRemotoAbuelitos2024!`

## Paso 3: Verificar

Una vez configurados todos los secrets, puedes hacer push al repositorio y GitHub Actions compilará automáticamente los APKs debug y release.

## Notas Importantes:

- Los keystores están codificados en base64 en los archivos de texto
- Los keystores son persistentes entre builds, evitando conflictos de paquete
- El workflow se ejecuta automáticamente en push a main/master/develop
- Los APKs se guardan como artifacts en cada build
  - Debug APK: 30 días de retención
  - Release APK: 90 días de retención

