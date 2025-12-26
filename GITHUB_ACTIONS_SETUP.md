# GitHub Actions Setup - Control Remoto TV Abuelitos

Esta guía te ayudará a configurar los GitHub Secrets necesarios para que el workflow de compilación automática funcione correctamente.

## Paso 1: Generar o usar keystores existentes

### Opción A: Si ya tienes keystores

Si tienes archivos `.keystore` existentes (debug y release), salta al Paso 2.

### Opción B: Generar nuevos keystores

#### Generar Debug Keystore (si no existe):
```bash
keytool -genkey -v -keystore debug.keystore \
  -alias debug-key \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass android \
  -keypass android
```

#### Generar Release Keystore:
```bash
keytool -genkey -v -keystore release.keystore \
  -alias release-key \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass tu_password_keystore \
  -keypass tu_password_key
```

## Paso 2: Codificar keystores en Base64

Los keystores deben ser convertidos a Base64 para almacenarlos en GitHub Secrets.

```bash
# Para el Release Keystore
cat release.keystore | base64 -w 0 > release_keystore_base64.txt

# Copiar el contenido del archivo release_keystore_base64.txt
```

## Paso 3: Agregar GitHub Secrets

1. Ve a tu repositorio en GitHub
2. Haz clic en **Settings** → **Secrets and variables** → **Actions**
3. Haz clic en **New repository secret** y agrega los siguientes:

### Secrets Requeridos:

| Secret Name | Valor | Descripción |
|-------------|-------|-------------|
| `RELEASE_KEYSTORE` | Contenido Base64 del keystore | El archivo release.keystore en Base64 |
| `RELEASE_KEYSTORE_PASSWORD` | Password del keystore | La contraseña de tu keystore release |
| `RELEASE_KEY_ALIAS` | release-key | El alias de la clave (o el que uses) |
| `RELEASE_KEY_PASSWORD` | Password de la clave | La contraseña de la clave privada |

## Paso 4: Verificar configuración

Una vez agregados los secrets:

1. Ve a la pestaña **Actions** en tu repositorio
2. El workflow debe ejecutarse automáticamente en cada push
3. Los APKs compilados estarán disponibles en **Artifacts**

## Estructura del Workflow

El workflow `build.yml` realiza:

1. **Build Debug**: Compila automáticamente sin necesidad de keystore
   - Descarga automáticamente con el debug keystore de Android
   - APK disponible sin firma para testing

2. **Build Release**: Compila y firma con tu keystore release
   - Requiere los secrets configurados
   - APK firmado listo para publicar

## Descarga de APKs

Después de cada build:

1. Ve a la ejecución del workflow
2. En la sección **Summary**, encontrarás los artifacts
3. Descarga `debug-apk` o `release-apk` según necesites

## Solución de problemas

### "Build failed: Keystore not found"
- Verifica que los secrets estén correctamente configurados
- Asegúrate de que `RELEASE_KEYSTORE` esté en Base64

### "Invalid keystore password"
- Comprueba que `RELEASE_KEYSTORE_PASSWORD` sea correcto
- Verifica que el keystore esté correctamente codificado en Base64

### "Key alias not found"
- Confirma que `RELEASE_KEY_ALIAS` coincida con el alias real en el keystore
- Por defecto es `release-key` o `android`

## Seguridad

⚠️ **IMPORTANTE**:
- Nunca commits los archivos `.keystore`
- Los secrets son privados y no se muestran en los logs
- Guarda tus contraseñas de keystore en un lugar seguro

---

**Nota**: Después de configurar los secrets, el workflow compilará automáticamente en cada push a las ramas `main` o `develop`.
