# Estructura del Proyecto TV Remote Control

## 📁 Estructura de Carpetas

```
TVRemote/
├── AndroidManifest.xml          # Configuración de la app
├── build.gradle                  # Dependencias y compilación
├── settings.gradle               # Configuración de Gradle
├── proguard-rules.pro            # Reglas de ofuscación
├── build.sh                      # Script de compilación
│
├── src/main/java/
│   └── com/remotetv/control/
│       ├── AndroidTVRemoteProtocol.java    # Protocolo SSL/TLS
│       ├── TVDiscovery.java                # Descubrimiento de TVs
│       ├── PreferencesManager.java         # Gestión de datos
│       ├── MainActivity.java               # Pantalla principal
│       └── RemoteActivity.java             # Control remoto
│
├── src/main/res/
│   ├── layout/
│   │   ├── activity_main.xml               # Layout principal
│   │   └── activity_remote.xml             # Layout remoto
│   ├── values/
│   │   └── strings.xml                     # Strings y textos
│   └── drawable/
│       └── (iconos y recursos)
│
└── docs/
    ├── README.md                # Guía principal
    ├── PROTOCOL.md              # Especificación técnica
    ├── INTEGRATION.md           # Ejemplos avanzados
    └── STRUCTURE.md             # Este archivo
```

## 📄 Descripción de Archivos

### Código Fuente

#### `AndroidTVRemoteProtocol.java`
- **Función**: Gestiona la conexión SSL/TLS con el TV
- **Responsabilidades**:
  - Crear socket seguro
  - Construir comandos de teclado
  - Enviar keycodes
  - Mantener conexión activa
- **Métodos principales**:
  - `connect()` - Conecta con TV
  - `sendKeyCommand(int)` - Envía tecla
  - `disconnect()` - Desconecta

#### `TVDiscovery.java`
- **Función**: Descubre TVs en la red local
- **Responsabilidades**:
  - Escanear puertos
  - Detectar TVs Android
  - Buscar por IP manual
  - Validar disponibilidad
- **Métodos principales**:
  - `discoverTVs()` - Descubre automáticamente
  - `scanIPRange()` - Escanea rango específico
  - `isTV()` - Valida si es TV

#### `PreferencesManager.java`
- **Función**: Gestiona configuración persistente
- **Responsabilidades**:
  - Guardar IP del TV
  - Persistir nombre del TV
  - Guardar estado de emparejamiento
- **Métodos principales**:
  - `saveTVIP(String)` - Guarda IP
  - `getTVIP()` - Recupera IP
  - `setPaired(boolean)` - Marca emparejado

#### `MainActivity.java`
- **Función**: Pantalla principal de conexión
- **Responsabilidades**:
  - Interfaz de búsqueda
  - Escaneo de red
  - Emparejamiento
- **Métodos principales**:
  - `scanForTVs()` - Inicia escaneo
  - `connectToTV()` - Conecta a TV específico

#### `RemoteActivity.java`
- **Función**: Control remoto interactivo
- **Responsabilidades**:
  - Interfaz de botones
  - Mapeo de keycodes
  - Envío de comandos
- **Métodos principales**:
  - `setupNavigationButtons()` - Configura navegación
  - `sendKey(int)` - Envía tecla

### Archivos de Configuración

#### `AndroidManifest.xml`
- Actividades principales
- Permisos de red
- Configuración de aplicación

#### `build.gradle`
- Versiones de compilación
- Dependencias externas
- Configuración de compilador

#### `strings.xml`
- Textos y etiquetas
- Mensajes de usuario
- Internacionalización (i18n)

### Layouts (XML)

#### `activity_main.xml`
- Campo de entrada IP
- Botones escanear y conectar
- Barra de progreso
- Área de resultados

#### `activity_remote.xml`
- D-Pad de navegación
- Botones de control
- Controles de media
- Botones de aplicaciones

### Documentación

#### `README.md`
- Guía general
- Instalación
- Uso básico
- Solución de problemas

#### `PROTOCOL.md`
- Especificación técnica del protocolo
- Estructura de paquetes
- Handshake SSL/TLS
- Mapa de keycodes completo
- Emparejamiento seguro

#### `INTEGRATION.md`
- Ejemplos de código avanzado
- Integración con otros sistemas
- Control persistente
- Voice control
- Home Assistant
- Testing

## 🔄 Flujo de Datos

```
┌─────────────────┐
│  Usuario abre   │
│   Aplicación    │
└────────┬────────┘
         │
         ▼
┌─────────────────────┐
│  MainActivity       │
│  - Carga IP guardada│
│  - Muestra opciones │
└────────┬────────────┘
         │
         ├─► Escanear ──────┐
         │                   │
         └─► Ingresar IP ────┼─────┐
                             │     │
                             ▼     │
                    ┌──────────────┤
                    │ TVDiscovery  │
                    │ - Escanea    │
                    │ - Valida TV  │
                    └──────┬───────┘
                           │
                           ▼
                    ┌─────────────────┐
                    │  Conectar a TV  │
                    │  Port: 6466     │
                    └────────┬────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │ AndroidTVRemote  │
                    │ Protocol         │
                    │ - SSL/TLS        │
                    │ - Keycodes       │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌───────────────────┐
                    │ RemoteActivity    │
                    │ - Interfaz remoto │
                    │ - Botones control │
                    └───────────────────┘
```

## 🔑 Keycodes Principales Utilizados

| Código | Nombre | Función |
|--------|--------|---------|
| 3 | HOME | Ir a inicio |
| 4 | BACK | Volver atrás |
| 19 | DPAD_UP | Arriba |
| 20 | DPAD_DOWN | Abajo |
| 21 | DPAD_LEFT | Izquierda |
| 22 | DPAD_RIGHT | Derecha |
| 23 | ENTER | Seleccionar |
| 24 | VOLUME_UP | Volumen + |
| 25 | VOLUME_DOWN | Volumen - |
| 82 | MENU | Menú |
| 85 | MEDIA_PLAY_PAUSE | Play/Pausa |
| 91 | MUTE | Silencio |

## 📦 Dependencias Externas

```gradle
// Android Framework
androidx.appcompat:appcompat:1.6.1
androidx.constraintlayout:constraintlayout:2.1.4
androidx.preference:preference:1.2.1
com.google.android.material:material:1.10.0
androidx.core:core:1.12.0

// Redes
com.squareup.okhttp3:okhttp:4.11.0

// JSON
com.google.code.gson:gson:2.10.1
```

## 🏗️ Patrones de Diseño

- **Singleton**: PreferencesManager
- **Observer**: Broadcast Receiver para red
- **Thread**: Operaciones de red en background
- **Factory**: Creación de protocolos
- **Adapter**: Listado de TVs

## 🔐 Consideraciones de Seguridad

1. **SSL/TLS**: Encriptación de comunicación
2. **Certificados**: Aceptar certificados inválidos (TVs antiguos)
3. **Validación**: Verificar IP antes de conectar
4. **Thread Safety**: Sincronización de operaciones
5. **Permisos**: Solo lo necesario en AndroidManifest

## 📱 Compatibilidad

- **API mínima**: 21 (Android 5.0)
- **API objetivo**: 34 (Android 14)
- **TVs soportados**: Todos los Android TV
- **Redes**: WiFi 802.11 b/g/n/ac

## 🚀 Próximas Mejoras

- [ ] Soporte para múltiples TVs simultáneamente
- [ ] Historial de comandos
- [ ] Macros personalizables
- [ ] Detección automática de apps
- [ ] Control de brillo
- [ ] Entrada de texto remota
- [ ] Widget homescreen
- [ ] Notificaciones de estado

---

**Proyecto**: TV Remote Control
**Versión**: 1.0
**Actualización**: 2025-12-26
