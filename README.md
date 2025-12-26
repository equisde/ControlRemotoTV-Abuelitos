# Control Remoto Android TV - Guía Completa

## 📱 Descripción General

Esta es una **aplicación Android que controla TVs Android TV antiguos sin necesidad de ADB inalámbrico o USB**. Funciona completamente a través de WiFi utilizando el protocolo Android TV Remote Protocol v2.

## 🔧 Características Principales

- ✅ **Sin ADB requerido** - Funciona sin Debug Bridge
- ✅ **Conexión WiFi** - Control a través de la red local
- ✅ **Escaneo automático** - Detecta TVs en la red
- ✅ **Controles navegación** - D-Pad arriba/abajo/izquierda/derecha
- ✅ **Controles media** - Play/Pausa, Volumen, Silencio
- ✅ **Emparejamiento seguro** - SSL/TLS encriptado
- ✅ **Interfaz simple** - Fácil de usar

## 📋 Requisitos

### Hardware
- **Smartphone Android**: Mínimo API 21 (Android 5.0+)
- **Android TV**: Cualquier TV con Android TV (incluso antiguo)
- **WiFi**: Ambos dispositivos en la misma red

### Software
- Android Studio 8.0+
- SDK mínimo: API 21
- Gradle 8.0+

## 🚀 Instalación y Compilación

### Paso 1: Estructura de carpetas

```
TVRemote/
├── build.gradle
├── AndroidManifest.xml
├── AndroidTVRemoteProtocol.java
├── TVDiscovery.java
├── PreferencesManager.java
├── MainActivity.java
├── RemoteActivity.java
├── activity_main.xml
├── activity_remote.xml
└── README.md
```

### Paso 2: Compilar con Android Studio

```bash
# Desde Android Studio:
# 1. File → Open → TVRemote
# 2. Build → Make Project (Ctrl+F9)
# 3. Run → Run 'app' (Shift+F10)
```

### Paso 3: Compilar con Gradle (línea de comandos)

```bash
cd TVRemote
./gradlew build
./gradlew installDebug
```

## 💻 Cómo Funciona Técnicamente

### Protocolo Android TV Remote Protocol v2

```
Puerto: 6466 (SSL/TLS)
Servicio: _androidtvremote._tcp

Estructura de conexión:
1. Cliente conecta a TV:6466
2. Handshake SSL/TLS
3. Envía comandos de teclado
4. Recibe confirmaciones
5. Desconecta

Comandos (Keycodes):
- 19: KEYCODE_DPAD_UP
- 20: KEYCODE_DPAD_DOWN
- 21: KEYCODE_DPAD_LEFT
- 22: KEYCODE_DPAD_RIGHT
- 23: KEYCODE_ENTER
- 3:  KEYCODE_HOME
- 4:  KEYCODE_BACK
- 24: KEYCODE_VOLUME_UP
- 25: KEYCODE_VOLUME_DOWN
- 85: KEYCODE_MEDIA_PLAY_PAUSE
```

### Clase AndroidTVRemoteProtocol

```java
// Conexión segura al TV
AndroidTVRemoteProtocol protocol = new AndroidTVRemoteProtocol("192.168.1.100");
protocol.connect();

// Enviar comandos
protocol.sendKeyCommand(AndroidTVRemoteProtocol.KeyCodes.KEYCODE_VOLUME_UP);

// Desconectar
protocol.disconnect();
```

## 🔍 Cómo Encontrar la IP de tu TV

### Opción 1: Desde el TV
1. Ir a **Configuración → Red → Estado de Red**
2. Buscar "Dirección IP"

### Opción 2: Desde el Router
1. Accede a 192.168.1.1 (o similar)
2. Busca "Dispositivos conectados"
3. Identifica el TV Android

### Opción 3: Usando la App
1. Abre la aplicación
2. Presiona **"Escanear Red"**
3. Espera a que encuentre los TVs

## 📲 Guía de Uso

### Primera vez

1. **Abrir la aplicación**
2. **Método A - Escaneo automático:**
   - Presiona "Escanear Red"
   - Selecciona tu TV de la lista
   - Presiona "Conectar"

3. **Método B - IP manual:**
   - Escribe la IP en el campo
   - Presiona "Conectar"

4. **Autorización en el TV** (si es necesario):
   - Algunos TVs mostrarán una pantalla de emparejamiento
   - Confirma en el TV el emparejamiento

### Usar el control remoto

Una vez conectado:
- Use **D-Pad** para navegar
- **OK** para seleccionar
- **Volumen** para ajustar
- **Play/Pausa** para media

## 🐛 Solución de Problemas

### "No se puede conectar"

**Problema**: Error al conectar con el TV

**Soluciones**:
1. Verifica que el TV está **encendido**
2. Verifica que ambos están en **la misma WiFi**
3. Verifica la **IP correcta**
4. Reinicia el TV y vuelve a intentar
5. Desactiva el firewall temporalmente

### "IP no encontrada en escaneo"

**Problema**: El escaneo no encuentra el TV

**Soluciones**:
1. Escribe la IP **manualmente**
2. Verifica el rango de IP en tu router
3. Asegúrate que el TV tiene **conectividad WiFi activa**

### "Conexión perdida"

**Problema**: La conexión se desconecta durante el uso

**Soluciones**:
1. Acércate más al router
2. Reinicia la WiFi en ambos dispositivos
3. Reinicia la aplicación
4. Verifica que la red es estable

## 🔐 Seguridad

- ✅ Usa **SSL/TLS** para encriptar la comunicación
- ✅ No envía contraseñas ni datos sensibles
- ✅ Solo funciona en **red local** (no necesita internet)
- ✅ Emparejamiento seguro entre dispositivos

## 📡 Protocolo Técnico Detallado

### Componentes principales

```
1. AndroidTVRemoteProtocol
   - Maneja conexión SSL/TLS
   - Construye comandos de teclado
   - Envía keycodes al TV

2. TVDiscovery
   - Escanea red local
   - Detecta puertos abiertos (6466)
   - Identifica TVs Android

3. PreferencesManager
   - Guarda IP del TV
   - Persiste configuración
   - Maneja emparejamientos

4. MainActivity
   - Interfaz de conexión
   - Escaneo y emparejamiento
   - Búsqueda de TVs

5. RemoteActivity
   - Control remoto interactivo
   - Mapeo de botones a keycodes
   - Gestión de conexión
```

### Flujo de conexión

```
┌─────────────┐
│  Aplicación │
└──────┬──────┘
       │
       ▼
┌────────────────────┐
│ MainActivity       │
│ - Escanear Red     │
│ - Conectar TV      │
└──────┬─────────────┘
       │
       ▼
┌────────────────────┐
│ AndroidTVRemote    │
│ Protocol           │
│ - SSL/TLS Connect  │
│ - Send Keycodes    │
└──────┬─────────────┘
       │
       ▼
┌──────────────────┐
│  Android TV      │
│  Puerto 6466     │
│  Protocolo v2    │
└──────────────────┘
```

## 🎮 Keycodes disponibles

```java
KEYCODE_HOME         = 3    // Inicio
KEYCODE_BACK         = 4    // Atrás
KEYCODE_DPAD_UP      = 19   // Arriba
KEYCODE_DPAD_DOWN    = 20   // Abajo
KEYCODE_DPAD_LEFT    = 21   // Izquierda
KEYCODE_DPAD_RIGHT   = 22   // Derecha
KEYCODE_ENTER        = 23   // Seleccionar/Enter
KEYCODE_VOLUME_UP    = 24   // Volumen +
KEYCODE_VOLUME_DOWN  = 25   // Volumen -
KEYCODE_POWER        = 26   // Encender/Apagar
KEYCODE_MEDIA_PLAY_PAUSE = 85  // Play/Pausa
KEYCODE_MUTE         = 91   // Silencio
KEYCODE_MENU         = 82   // Menú
```

## 📝 Ejemplos de código

### Conectar y enviar comando

```java
// Crear protocolo
AndroidTVRemoteProtocol protocol = new AndroidTVRemoteProtocol("192.168.1.100");

// Conectar
if (protocol.connect()) {
    // Enviar comando de volumen +
    protocol.sendKeyCommand(AndroidTVRemoteProtocol.KeyCodes.KEYCODE_VOLUME_UP);
    
    // Desconectar
    protocol.disconnect();
}
```

### Descubrir TVs

```java
TVDiscovery discovery = new TVDiscovery(context);
List<String> tvs = discovery.discoverTVs();

for (String tv : tvs) {
    Log.i("TV", "Encontrado: " + tv);
}
```

### Guardar y cargar configuración

```java
PreferencesManager prefs = new PreferencesManager(context);

// Guardar
prefs.saveTVIP("192.168.1.100");
prefs.saveTVName("Sala");
prefs.setPaired(true);

// Cargar
String ip = prefs.getTVIP();
String name = prefs.getTVName();
boolean paired = prefs.isPaired();
```

## 🤝 Contribuciones

Para mejorar la aplicación:
1. Añadir más keycodes
2. Soporte para múltiples TVs
3. Control de apps
4. Teclado virtual
5. Historial de comandos

## 📄 Licencia

MIT License - Libre para usar y modificar

## 📞 Soporte

Si tienes problemas:
1. Verifica la conectividad WiFi
2. Comprueba la IP del TV
3. Reinicia ambos dispositivos
4. Revisa los logs de la aplicación

---

**Versión**: 1.0  
**Ultima actualización**: 2025-12-26  
**Protocolo**: Android TV Remote Protocol v2  
**API mínima**: 21 (Android 5.0)
