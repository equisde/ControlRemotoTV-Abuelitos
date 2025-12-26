# Guía Rápida de Inicio - TV Remote Control

## ⚡ Instalación Express (5 minutos)

### 1️⃣ Requisitos Previos

```bash
# En tu Android:
- Android 5.0+ (API 21+)
- WiFi conectada
- Espacio libre ~50MB

# En tu PC (solo para compilar):
- Android Studio 8.0+
- Android SDK API 34
- Gradle 8.0+
```

### 2️⃣ Obtener el Código

```bash
# Descargar proyecto
cd TVRemote

# Ver archivos
ls -la
```

### 3️⃣ Compilar APK

#### Opción A: Con Android Studio

```
1. File → Open → TVRemote
2. Build → Make Project
3. Run → Run App
```

#### Opción B: Con Gradle (línea de comandos)

```bash
cd TVRemote
chmod +x build.sh
./build.sh
```

APK estará en: `app/build/outputs/apk/debug/app-debug.apk`

### 4️⃣ Instalar en tu Teléfono

#### Opción A: Con ADB (PC)

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### Opción B: Copiar APK manualmente

1. Transfiere el APK a tu teléfono
2. Abre el APK
3. Instala la aplicación

### 5️⃣ Primeros Pasos

1. **Abre la aplicación**
2. **Asegúrate que:**
   - Tu TV está encendido
   - TV y teléfono están en la **misma WiFi**
3. **Opción A - Escaneo automático:**
   - Presiona "Escanear Red"
   - Espera resultados
   - Selecciona tu TV
4. **Opción B - IP manual:**
   - Obtén IP del TV (Ajustes → Red)
   - Ingresa en la app
   - Presiona "Conectar"
5. **¡Listo!** Usa los botones para controlar

---

## 🔍 Encontrar IP del TV Rápidamente

### Método 1: Desde el TV Directamente

```
Presiona en el TV:
Ajustes → Red → Estado de red
Busca "Dirección IP" o "IP"
```

### Método 2: Desde tu Router

```
1. Abre navegador
2. Ve a: 192.168.1.1
3. Busca "Dispositivos conectados" o "Connected devices"
4. Identifica tu TV por nombre
```

### Método 3: Con la App (lo más fácil)

```
Presiona "Escanear Red" en la app
Espera 30-60 segundos
La app encontrará el TV automáticamente
```

---

## 🆘 Troubleshooting Rápido

| Problema | Solución |
|----------|----------|
| No encuentra TV | Verifica que TV está encendido |
| No conecta | Verifica IP correcta |
| Conexión inestable | Acércate al router WiFi |
| Botones no funcionan | Reinicia la app |
| TV no responde | Reinicia el TV |

---

## 📊 Especificaciones Técnicas (resumidas)

```
Protocolo: Android TV Remote v2
Puerto: 6466 (SSL/TLS)
Transporte: WiFi TCP/IP
Keycodes: DPAD, Volume, Play, Media, Home
No requiere: ADB, USB, Emparejamiento manual
```

---

## ✅ Verificación de Funcionamiento

1. ¿Aparece "¡Conectado!" en la app?
   → ✅ Conexión OK

2. ¿Responden los botones en el TV?
   → ✅ Todo funciona

3. ¿Se puede cambiar volumen?
   → ✅ Keycodes OK

---

## 📚 Documentación Completa

Para más detalles, consulta:
- `README.md` - Guía completa
- `PROTOCOL.md` - Especificación técnica
- `INTEGRATION.md` - Ejemplos avanzados
- `STRUCTURE.md` - Estructura del proyecto

---

**¿Necesitas ayuda?**

Revisa los pasos en orden:
1. TV encendido ✓
2. WiFi conectada ✓
3. IP correcta ✓
4. Conexión exitosa ✓
5. Usar remoto ✓

**¡Listo!** Disfruta controlando tu TV 📺
