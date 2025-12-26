# 🚀 Guía Rápida: GitHub Actions + Keystores

## ¿Qué es GitHub Actions?
Herramienta automática de GitHub que compila tu app cada vez que haces push.

## ¿Qué es un Keystore?
Archivo que firma (certifica) tu APK para que no de "advertencia de desconocido" al instalarlo.

---

## PASO 1️⃣: Generar Keystore en tu Computadora

Si tienes Java instalado:

```bash
cd ControlRemotoTV-Abuelitos
./generate_keystores.sh
```

Te pedirá contraseña dos veces. Anota todo:

```
Keystore Password: _________ (ejemplo: mi_password_123)
Key Password: _________ (ejemplo: mi_key_456)
Key Alias: release-key (es el nombre, déjalo así)
```

El script creará un archivo con contenido Base64 en:
```
keystores/release_keystore_base64.txt
```

---

## PASO 2️⃣: Copiar Contenido a GitHub Secrets

1. **Copia TODO el contenido** de `release_keystore_base64.txt`
   - Es un texto muy largo, ok si es largo

2. Ve a tu repositorio en GitHub
   - https://github.com/equisde/ControlRemotoTV-Abuelitos

3. Click en: **Settings** (arriba a la derecha)

4. Click en: **Secrets and variables** → **Actions** (menú izquierdo)

5. Click en: **New repository secret**

6. Agrega 4 secrets así:

### Secret 1: RELEASE_KEYSTORE
- Name: `RELEASE_KEYSTORE`
- Value: **Pega aquí TODO el contenido** de `release_keystore_base64.txt`
- Click: **Add secret**

### Secret 2: RELEASE_KEYSTORE_PASSWORD
- Name: `RELEASE_KEYSTORE_PASSWORD`
- Value: La contraseña que anotaste (ej: mi_password_123)
- Click: **Add secret**

### Secret 3: RELEASE_KEY_ALIAS
- Name: `RELEASE_KEY_ALIAS`
- Value: `release-key` (exacto así)
- Click: **Add secret**

### Secret 4: RELEASE_KEY_PASSWORD
- Name: `RELEASE_KEY_PASSWORD`
- Value: La otra contraseña que anotaste (ej: mi_key_456)
- Click: **Add secret**

---

## PASO 3️⃣: Ver si Funcionó

1. Ve a la pestaña **Actions** en tu repositorio

2. Deberías ver un workflow ejecutándose

3. Si ve ✅ VERDE = ¡Éxito!

4. Si ve ❌ ROJO = Hay error (verifica los secrets)

---

## PASO 4️⃣: Descargar APKs Compilados

Una vez que el workflow termine (ves ✅):

1. Click en el workflow ejecutado (vés el nombre del commit)

2. Scroll hasta **Artifacts** (abajo)

3. Descarga:
   - `debug-apk` = APK sin firma (para probar)
   - `release-apk` = APK firmado (listo para publicar)

---

## ¿Qué Pasa Después?

**Cada vez que hagas `git push`:**

```bash
git add .
git commit -m "Mi cambio"
git push origin main
```

GitHub Actions automáticamente:
1. Descarga tu código
2. Compila la app (Debug y Release)
3. Firma el Release con tu keystore
4. Sube los APKs como Artifacts
5. **¡Listos para descargar!**

---

## ⚠️ IMPORTANTE - SEGURIDAD

❌ **NUNCA hagas esto:**
- No subas archivo `release.keystore` al repositorio
- No pegues contraseñas en el código
- Los secrets son automáticamente privados ✅

✅ **SÍ haz esto:**
- Guarda `release.keystore` en lugar seguro (tu PC)
- Anota las contraseñas en archivo seguro local
- Usa GitHub Secrets para las contraseñas

---

## 🆘 Si Algo Falla

### Error: "Keystore not found"
→ Verifica que copiaste TODO el contenido de `release_keystore_base64.txt`

### Error: "Invalid password"
→ Verifica que la contraseña sea EXACTA (mayúsculas/minúsculas cuentan)

### APK no descarga
→ Espera a que el workflow termine (dice "completed" en verde)

### No veo Artifacts
→ El workflow debe estar COMPLETADO (no "in progress")

---

## 📱 Instalar el APK

### En tu Teléfono:
```bash
adb install app-release.apk
```

O simplemente descarga y toca el APK en tu teléfono.

---

**¡Listo! Ahora tu app se compila automáticamente en GitHub 🎉**
