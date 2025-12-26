# GitHub Secrets Setup para ControlRemotoTV-Abuelitos

## Instrucciones para configurar los keystores en GitHub Actions

Este guía te explica cómo configurar los secrets necesarios para que GitHub Actions pueda compilar y firmar los APK.

### 1. Acceder a GitHub Repository Settings

1. Ve a tu repositorio: https://github.com/equisde/ControlRemotoTV-Abuelitos
2. Haz clic en **Settings** (Configuración)
3. En el menú lateral izquierdo, ve a **Secrets and variables** → **Actions**

### 2. Crear los Secrets

Necesitas crear los siguientes 8 secrets:

#### Para DEBUG Build:

**a) DEBUG_KEYSTORE**
- Copia el contenido completo del archivo `release_keystore_base64.txt`
- Nombre del secret: `DEBUG_KEYSTORE`
- Pégalo en el valor

**b) DEBUG_KEYSTORE_PASSWORD**
- Valor: `android`
- Nombre del secret: `DEBUG_KEYSTORE_PASSWORD`

**c) DEBUG_KEY_ALIAS**
- Valor: `debug`
- Nombre del secret: `DEBUG_KEY_ALIAS`

**d) DEBUG_KEY_PASSWORD**
- Valor: `android`
- Nombre del secret: `DEBUG_KEY_PASSWORD`

#### Para RELEASE Build:

**e) RELEASE_KEYSTORE**
- Copia el contenido completo del archivo `release_keystore_base64.txt` que está en el repositorio local
- Nombre del secret: `RELEASE_KEYSTORE`
- Pégalo en el valor

**f) RELEASE_KEYSTORE_PASSWORD**
- Valor: `android123`
- Nombre del secret: `RELEASE_KEYSTORE_PASSWORD`

**g) RELEASE_KEY_ALIAS**
- Valor: `release`
- Nombre del secret: `RELEASE_KEY_ALIAS`

**h) RELEASE_KEY_PASSWORD**
- Valor: `android123`
- Nombre del secret: `RELEASE_KEY_PASSWORD`

### 3. Pasos para agregar cada Secret

Para cada uno de los secrets:

1. Haz clic en **New repository secret** (Nuevo secret del repositorio)
2. En el campo **Name**, ingresa el nombre del secret (ej: `DEBUG_KEYSTORE`)
3. En el campo **Secret**, pega el contenido
4. Haz clic en **Add secret**

### 4. Verificar que los secrets están configurados

Una vez que hayas agregado todos los secrets, deberías ver una lista como esta:

```
✓ DEBUG_KEY_ALIAS
✓ DEBUG_KEY_PASSWORD
✓ DEBUG_KEYSTORE
✓ DEBUG_KEYSTORE_PASSWORD
✓ RELEASE_KEY_ALIAS
✓ RELEASE_KEY_PASSWORD
✓ RELEASE_KEYSTORE
✓ RELEASE_KEYSTORE_PASSWORD
```

### 5. Probar la compilación

Después de configurar los secrets:

1. Ve a la pestaña **Actions** en tu repositorio
2. Selecciona el workflow "Build APK"
3. Haz clic en **Run workflow** para probar manualmente
4. O simplemente haz un `git push` para disparar el workflow automáticamente

### 6. Ver los resultados

1. Vuelve a la pestaña **Actions**
2. Haz clic en el workflow más reciente
3. Espera a que se complete (normalmente toma 5-10 minutos)
4. Si tiene éxito, podrás descargar los APK desde los artifacts

## Notas importantes

- **Seguridad**: Los secrets están encriptados y solo se usan dentro del CI/CD
- **Persistencia**: Los keystores son consistentes entre builds, lo que garantiza que las actualizaciones de la app no tengan conflictos de firma
- **Archivo base64**: El archivo `release_keystore_base64.txt` contiene el keystore en base64. Solo el contenido de este archivo debe copiarse como secret
- **Contraseñas**: Las contraseñas usadas son:
  - Debug: `android` / `android`
  - Release: `android123` / `android123`

## Solución de problemas

### Error: "Build failed - Signing key not found"

- Verifica que todos los 8 secrets están correctamente creados
- Asegúrate de no tener espacios en blanco antes/después de los valores

### Error: "Invalid keystore"

- Verifica que el contenido de `RELEASE_KEYSTORE` es el archivo base64 completo sin cambios

### Error: "Key alias not found"

- Verifica que `RELEASE_KEY_ALIAS` es exactamente `release` (minúsculas)
- Verifica que `DEBUG_KEY_ALIAS` es exactamente `debug` (minúsculas)

## Más información

Para más detalles sobre cómo funcionan los secrets en GitHub Actions, consulta:
https://docs.github.com/en/actions/security-guides/using-secrets-in-github-actions
