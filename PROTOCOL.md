# Protocolo Android TV Remote v2 - Especificación Técnica

## 📡 Detalles del Protocolo

### Información General

- **Nombre**: Android TV Remote Protocol v2
- **Puerto**: 6466 (conexión principal)
- **Puerto alternativo**: 6467 (emparejamiento)
- **Encriptación**: SSL/TLS 1.2+
- **Descubrimiento**: mDNS (_androidtvremote._tcp)

### Handshake de Conexión

```
Cliente                          Servidor (TV)
  |                                 |
  |---- TCP SYN (6466) ----------->|
  |<---- TCP SYN-ACK --------------|
  |                                 |
  |---- TLS ClientHello ---------->|
  |<---- TLS ServerHello ----------|
  |                                 |
  |---- TLS Handshake ------------>|
  |<---- TLS Handshake ------------|
  |                                 |
  |---- Cambio de Cipher Suite --->|
  |<---- Cambio de Cipher Suite ---|
  |                                 |
  | Conexión SSL/TLS Establecida   |
  |                                 |
  |---- Mensaje de Comando ------->|
  |<---- Confirmación de Recibo ----|
  |                                 |
```

## 🔑 Estructura de Comandos

### Formato de Paquete

```
┌────────────┬────────────┬──────────────────┐
│   Tipo     │  Keycode   │    Payload       │
│  1 byte    │  1 byte    │   Variable       │
└────────────┴────────────┴──────────────────┘
```

### Tipos de Comandos

```
0x00 - Key Press (Teclado)
0x01 - Text Input (Texto)
0x02 - Custom Command (Personalizado)
0x03 - Pairing Request (Emparejamiento)
0xFF - Keep Alive (Mantener vivo)
```

### Ejemplo: Enviar tecla VOLUMEN ARRIBA

```python
import socket
import ssl

# Crear contexto SSL
context = ssl.create_default_context()
context.check_hostname = False
context.verify_mode = ssl.CERT_NONE

# Conectar a TV
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
ssock = context.wrap_socket(sock, server_hostname="192.168.1.100")
ssock.connect(("192.168.1.100", 6466))

# Construir comando (Volumen+)
command = bytes([0x00, 0x18])  # 0x00=KeyPress, 0x18=KEYCODE_VOLUME_UP

# Enviar
ssock.send(command)

# Desconectar
ssock.close()
```

## 🔍 Mapa de Keycodes Completo

```
KEYCODE_UNKNOWN = 0
KEYCODE_SOFT_LEFT = 1
KEYCODE_SOFT_RIGHT = 2
KEYCODE_HOME = 3
KEYCODE_BACK = 4
KEYCODE_CALL = 5
KEYCODE_ENDCALL = 6
KEYCODE_0 = 7
KEYCODE_1 = 8
KEYCODE_2 = 9
KEYCODE_3 = 10
KEYCODE_4 = 11
KEYCODE_5 = 12
KEYCODE_6 = 13
KEYCODE_7 = 14
KEYCODE_8 = 15
KEYCODE_9 = 16
KEYCODE_STAR = 17
KEYCODE_POUND = 18
KEYCODE_DPAD_UP = 19
KEYCODE_DPAD_DOWN = 20
KEYCODE_DPAD_LEFT = 21
KEYCODE_DPAD_RIGHT = 22
KEYCODE_DPAD_CENTER = 23
KEYCODE_VOLUME_UP = 24
KEYCODE_VOLUME_DOWN = 25
KEYCODE_POWER = 26
KEYCODE_CAMERA = 27
KEYCODE_CLEAR = 28
KEYCODE_A = 29
... (hasta KEYCODE_Z = 54)
KEYCODE_COMMA = 55
KEYCODE_PERIOD = 56
KEYCODE_ALT_LEFT = 57
KEYCODE_ALT_RIGHT = 58
KEYCODE_SHIFT_LEFT = 59
KEYCODE_SHIFT_RIGHT = 60
KEYCODE_TAB = 61
KEYCODE_SPACE = 62
KEYCODE_SYM = 63
KEYCODE_EXPLORER = 64
KEYCODE_ENVELOPE = 65
KEYCODE_ENTER = 66
KEYCODE_DEL = 67
KEYCODE_GRAVE = 68
KEYCODE_MINUS = 69
KEYCODE_EQUALS = 70
KEYCODE_LEFT_BRACKET = 71
KEYCODE_RIGHT_BRACKET = 72
KEYCODE_BACKSLASH = 73
KEYCODE_SEMICOLON = 74
KEYCODE_APOSTROPHE = 75
KEYCODE_SLASH = 76
KEYCODE_AT = 77
KEYCODE_NUM = 78
KEYCODE_HEADSETHOOK = 79
KEYCODE_FOCUS = 80
KEYCODE_PLUS = 81
KEYCODE_MENU = 82
KEYCODE_NOTIFICATION = 83
KEYCODE_SEARCH = 84
KEYCODE_MEDIA_PLAY_PAUSE = 85
KEYCODE_MEDIA_STOP = 86
KEYCODE_MEDIA_NEXT = 87
KEYCODE_MEDIA_PREVIOUS = 88
KEYCODE_MEDIA_REWIND = 89
KEYCODE_MEDIA_FAST_FORWARD = 90
KEYCODE_MUTE = 91
KEYCODE_PAGE_UP = 92
KEYCODE_PAGE_DOWN = 93
KEYCODE_PICTSYMBOLS = 94
KEYCODE_SWITCH_CHARSET = 95
KEYCODE_BUTTON_A = 96
KEYCODE_BUTTON_B = 97
KEYCODE_BUTTON_C = 98
KEYCODE_BUTTON_X = 99
KEYCODE_BUTTON_Y = 100
KEYCODE_BUTTON_Z = 101
KEYCODE_BUTTON_L1 = 102
KEYCODE_BUTTON_R1 = 103
KEYCODE_BUTTON_L2 = 104
KEYCODE_BUTTON_R2 = 105
KEYCODE_BUTTON_THUMBL = 106
KEYCODE_BUTTON_THUMBR = 107
KEYCODE_BUTTON_START = 108
KEYCODE_BUTTON_SELECT = 109
KEYCODE_BUTTON_MODE = 110
KEYCODE_ESCAPE = 111
KEYCODE_FORWARD_DEL = 112
KEYCODE_CTRL_LEFT = 113
KEYCODE_CTRL_RIGHT = 114
KEYCODE_CAPS_LOCK = 115
KEYCODE_SCROLL_LOCK = 116
KEYCODE_META_LEFT = 117
KEYCODE_META_RIGHT = 118
KEYCODE_FUNCTION = 119
KEYCODE_SYSRQ = 120
KEYCODE_BREAK = 121
KEYCODE_MOVE_HOME = 122
KEYCODE_MOVE_END = 123
KEYCODE_INSERT = 124
KEYCODE_FORWARD = 125
KEYCODE_MEDIA_PLAY = 126
KEYCODE_MEDIA_PAUSE = 127
```

## 🛡️ Emparejamiento Seguro

### Proceso de Emparejamiento

```
1. Usuario inicia emparejamiento en la app
2. App envía solicitud a TV:6467
3. TV muestra código de 4 dígitos en pantalla
4. Usuario ingresa código en la app
5. App valida código con TV
6. Ambos generan claves compartidas
7. Emparejamiento confirmado
```

### Estructura de Emparejamiento

```json
{
  "type": "pairing_request",
  "client_name": "Mi Telefono",
  "code": "1234"
}
```

## 📱 Detección de TV en Red

### mDNS/Bonjour

```
Servicio: _androidtvremote._tcp.local
Campos:
- Name: MODELO-TV
- IP: 192.168.x.x
- Puerto: 6466 y 6467
- Properties:
  * name=MODELO-TV
  * model=KD-55X8000E
  * brand=Sony
```

### Descubrimiento Python

```python
from zeroconf import ServiceBrowser, Zeroconf

class MyListener:
    def add_service(self, zeroconf, service_type, name):
        info = zeroconf.get_service_info(service_type, name)
        if service_type == "_androidtvremote._tcp.local.":
            print(f"TV encontrado: {info}")

zeroconf = Zeroconf()
listener = MyListener()
ServiceBrowser(zeroconf, "_androidtvremote._tcp.local.", listener)
```

## 🔄 Mantener Conexión Activa

```
El protocolo requiere enviar un "Keep Alive" cada 30-60 segundos

┌─────────────────────┐
│  Conexión Abierta   │
├─────────────────────┤
│  0-30s: Inactivo    │
├─────────────────────┤
│  30s: Enviar 0xFF   │ (Keep Alive)
├─────────────────────┤
│  30-60s: Inactivo   │
├─────────────────────┤
│  60s: Enviar 0xFF   │ (Keep Alive)
└─────────────────────┘

Si no se envía keep alive:
- La conexión se cierra en ~2 minutos
- Necesita reconectar
```

## 🎯 Casos de Uso Avanzados

### 1. Control de Aplicaciones

```java
// Lanzar Netflix
Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setData(Uri.parse("netflix://"));
startActivity(intent);

// Mediante comandos
protocol.sendKeyCommand(KEYCODE_HOME);  // Ir a home
Thread.sleep(500);
// Navegar a Netflix
for (int i = 0; i < 3; i++) {
    protocol.sendKeyCommand(KEYCODE_DPAD_RIGHT);
    Thread.sleep(300);
}
protocol.sendKeyCommand(KEYCODE_ENTER);
```

### 2. Cadenas de Comandos

```java
public void volumeUp5Times() {
    for (int i = 0; i < 5; i++) {
        protocol.sendKeyCommand(KEYCODE_VOLUME_UP);
        Thread.sleep(100);  // Delay entre comandos
    }
}
```

### 3. Mapeo Personalizado de Botones

```java
public void customButton1() {
    // Simular secuencia compleja
    protocol.sendKeyCommand(KEYCODE_HOME);
    Thread.sleep(1000);
    protocol.sendKeyCommand(KEYCODE_DPAD_DOWN);
    Thread.sleep(300);
    protocol.sendKeyCommand(KEYCODE_DPAD_DOWN);
    Thread.sleep(300);
    protocol.sendKeyCommand(KEYCODE_ENTER);
}
```

## 🚨 Manejo de Errores

### Tipos de Errores

```
Error Type 1: Connection Timeout
- Causa: TV no responde o no está encendido
- Solución: Reintentar o verificar TV

Error Type 2: SSL Certificate Error
- Causa: TV con certificado inválido (normal en TVs antiguos)
- Solución: Aceptar cualquier certificado (implementado)

Error Type 3: Command Not Acknowledged
- Causa: TV no reconoce comando
- Solución: Usar keycode válido

Error Type 4: Pairing Required
- Causa: TV requiere emparejamiento
- Solución: Completar proceso de emparejamiento
```

## 📊 Monitoreo de Conexión

```java
public class ConnectionMonitor extends Thread {
    private AndroidTVRemoteProtocol protocol;
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            if (!protocol.isConnected()) {
                Log.w(TAG, "Conexión perdida, reconectando...");
                protocol.reconnect();
            }
            // Keep Alive
            protocol.sendKeepAlive();
            
            try {
                Thread.sleep(30000); // 30 segundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        running = false;
    }
}
```

---

**Referencia**: Android TV Remote Control Protocol v2
**Última actualización**: 2025-12-26
