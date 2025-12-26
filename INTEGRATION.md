# Integración y Ejemplos Avanzados

## 🚀 Integración en tu Proyecto Android

### Opción 1: Copiar Clases Directamente

1. Copia estas 3 clases a tu proyecto:
   - `AndroidTVRemoteProtocol.java`
   - `TVDiscovery.java`
   - `PreferencesManager.java`

2. Actualiza tu `build.gradle`:
```gradle
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
    implementation 'com.google.code.gson:gson:2.10.1'
}
```

3. Agrega permisos a `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### Opción 2: Usar como Librería

```gradle
dependencies {
    implementation project(':tvremote')
}
```

## 💡 Ejemplos de Código

### Ejemplo 1: Control Básico

```java
import com.remotetv.control.*;

public class SimpleRemoteExample {
    
    public static void main(String[] args) throws Exception {
        // Crear protocolo
        AndroidTVRemoteProtocol remote = new AndroidTVRemoteProtocol("192.168.1.100");
        
        // Conectar
        if (!remote.connect()) {
            System.out.println("Error conectando");
            return;
        }
        
        // Cambiar volumen
        remote.sendKeyCommand(AndroidTVRemoteProtocol.KeyCodes.KEYCODE_VOLUME_UP);
        Thread.sleep(500);
        
        // Ir a home
        remote.sendKeyCommand(AndroidTVRemoteProtocol.KeyCodes.KEYCODE_HOME);
        
        // Desconectar
        remote.disconnect();
    }
}
```

### Ejemplo 2: Descubrimiento de TVs

```java
public class DiscoveryExample extends AppCompatActivity {
    
    private void findAllTVs() {
        TVDiscovery discovery = new TVDiscovery(this);
        
        new Thread(() -> {
            // Opción A: Descubrimiento automático
            List<String> tvs = discovery.discoverTVs();
            
            // Opción B: Escanear rango específico
            List<String> tvs2 = discovery.scanIPRange("192.168.1.1", "192.168.1.254");
            
            runOnUiThread(() -> {
                for (String tv : tvs) {
                    Toast.makeText(this, "TV: " + tv, Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
```

### Ejemplo 3: Control Persistente

```java
public class PersistentRemote {
    private AndroidTVRemoteProtocol protocol;
    private Handler handler = new Handler();
    private KeepAliveRunnable keepAliveRunnable;
    
    public void startPersistentConnection(String tvIP) {
        protocol = new AndroidTVRemoteProtocol(tvIP);
        
        new Thread(() -> {
            if (protocol.connect()) {
                startKeepAlive();
            }
        }).start();
    }
    
    private void startKeepAlive() {
        keepAliveRunnable = new KeepAliveRunnable();
        handler.postDelayed(keepAliveRunnable, 30000);
    }
    
    private class KeepAliveRunnable implements Runnable {
        @Override
        public void run() {
            if (protocol.isConnected()) {
                // Enviar keep-alive (comando dummy)
                protocol.sendKeyCommand(-1);
                handler.postDelayed(this, 30000);
            }
        }
    }
    
    public void sendCommand(int keyCode) {
        if (protocol.isConnected()) {
            new Thread(() -> protocol.sendKeyCommand(keyCode)).start();
        }
    }
    
    public void stop() {
        handler.removeCallbacks(keepAliveRunnable);
        protocol.disconnect();
    }
}
```

### Ejemplo 4: Secuencia de Comandos

```java
public class CommandSequence {
    private AndroidTVRemoteProtocol protocol;
    
    public CommandSequence(String tvIP) {
        this.protocol = new AndroidTVRemoteProtocol(tvIP);
        protocol.connect();
    }
    
    // Abrir Netflix
    public void openNetflix() {
        executeSequence(
            new Command(KEYCODE_HOME, 500),
            new Command(KEYCODE_DPAD_RIGHT, 300),
            new Command(KEYCODE_DPAD_RIGHT, 300),
            new Command(KEYCODE_DPAD_RIGHT, 300),
            new Command(KEYCODE_ENTER, 1000)
        );
    }
    
    // Subir volumen 5 veces
    public void volumeUp5Times() {
        executeSequence(
            new Command(KEYCODE_VOLUME_UP, 200),
            new Command(KEYCODE_VOLUME_UP, 200),
            new Command(KEYCODE_VOLUME_UP, 200),
            new Command(KEYCODE_VOLUME_UP, 200),
            new Command(KEYCODE_VOLUME_UP, 200)
        );
    }
    
    private void executeSequence(Command... commands) {
        new Thread(() -> {
            for (Command cmd : commands) {
                protocol.sendKeyCommand(cmd.keyCode);
                try {
                    Thread.sleep(cmd.delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
    
    private static class Command {
        int keyCode;
        long delayMs;
        
        Command(int keyCode, long delayMs) {
            this.keyCode = keyCode;
            this.delayMs = delayMs;
        }
    }
}
```

### Ejemplo 5: Control con Voice Assistant

```java
public class VoiceControlActivity extends AppCompatActivity {
    private AndroidTVRemoteProtocol protocol;
    private SpeechRecognizer recognizer;
    
    private void setupVoiceControl() {
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String command = matches.get(0).toLowerCase();
                    processVoiceCommand(command);
                }
            }
            // ... otros métodos ...
        });
    }
    
    private void processVoiceCommand(String command) {
        switch (command) {
            case "volumen arriba":
                protocol.sendKeyCommand(KEYCODE_VOLUME_UP);
                break;
            case "volumen abajo":
                protocol.sendKeyCommand(KEYCODE_VOLUME_DOWN);
                break;
            case "silencio":
                protocol.sendKeyCommand(KEYCODE_MUTE);
                break;
            case "inicio":
                protocol.sendKeyCommand(KEYCODE_HOME);
                break;
            case "atrás":
                protocol.sendKeyCommand(KEYCODE_BACK);
                break;
            case "play":
                protocol.sendKeyCommand(KEYCODE_MEDIA_PLAY_PAUSE);
                break;
        }
    }
}
```

### Ejemplo 6: Interfaz Personalizada

```xml
<!-- custom_remote_layout.xml -->
<GridLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:columnCount="3"
    android:rowCount="4"
    android:padding="16dp">

    <Button
        android:id="@+id/btn_netflix"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:text="Netflix"
        android:layout_row="0"
        android:layout_column="0" />

    <Button
        android:id="@+id/btn_youtube"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:text="YouTube"
        android:layout_row="0"
        android:layout_column="1" />

    <Button
        android:id="@+id/btn_settings"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:text="Ajustes"
        android:layout_row="0"
        android:layout_column="2" />

    <!-- Más botones... -->
</GridLayout>
```

## 📱 Integración con Home Assistant

Si tienes Home Assistant, puedes integrar tu TV:

```yaml
# configuration.yaml
androidtv:
  - name: "Sala TV"
    host: 192.168.1.100
    port: 6466
    timeout: 5
    turn_on_cmd: "KEYCODE_POWER"
    turn_off_cmd: "KEYCODE_POWER"
```

```yaml
automation:
  - alias: "Encender TV al atardecer"
    trigger:
      platform: sun
      event: sunset
    action:
      service: media_player.turn_on
      entity_id: media_player.sala_tv
```

## 🔌 Integración con Node-RED

```json
{
  "flows": [
    {
      "id": "tv-remote-node",
      "type": "http request",
      "method": "POST",
      "url": "http://192.168.1.100:6466",
      "payload": {
        "type": "keycode",
        "code": 19
      }
    }
  ]
}
```

## 🎮 Integración con Control Bluetooth

```java
public class BluetoothRemoteControl implements BluetoothAdapter.LeScanCallback {
    
    private AndroidTVRemoteProtocol tvRemote;
    
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (device.getName() != null && device.getName().contains("RemoteControl")) {
            connectToRemote(device);
        }
    }
    
    private void connectToRemote(BluetoothDevice device) {
        // Mapear comandos Bluetooth a TV
    }
}
```

## 📊 Logging y Debugging

```java
public class RemoteLogger {
    private static final String TAG = "TVRemote";
    
    public static void logConnection(String tvIP, boolean success) {
        if (success) {
            Log.i(TAG, "Conexión exitosa: " + tvIP);
        } else {
            Log.e(TAG, "Error conectando: " + tvIP);
        }
    }
    
    public static void logCommand(int keyCode, long delayMs) {
        Log.d(TAG, "Comando: " + keyCode + ", Delay: " + delayMs + "ms");
    }
    
    public static void logError(Exception e) {
        Log.e(TAG, "Error: " + e.getMessage(), e);
    }
}
```

## 🧪 Testing

```java
public class RemoteTest extends AndroidTestCase {
    
    private AndroidTVRemoteProtocol protocol;
    
    @Before
    public void setUp() {
        protocol = new AndroidTVRemoteProtocol("192.168.1.100");
    }
    
    public void testConnection() {
        assertTrue("Debería conectar", protocol.connect());
    }
    
    public void testKeyCommand() {
        protocol.connect();
        boolean result = protocol.sendKeyCommand(19); // DPAD_UP
        assertTrue("Debería enviar comando", result);
        protocol.disconnect();
    }
    
    @After
    public void tearDown() {
        protocol.disconnect();
    }
}
```

## 🔐 Mejoras de Seguridad

```java
public class SecureRemoteConnection {
    
    // Validar IP
    private boolean isValidIP(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) return false;
        for (String part : parts) {
            int num = Integer.parseInt(part);
            if (num < 0 || num > 255) return false;
        }
        return true;
    }
    
    // Validar keycode
    private boolean isValidKeyCode(int keyCode) {
        return keyCode >= 0 && keyCode <= 255;
    }
    
    // Encriptar credenciales locales
    private void saveSecureIP(String ip, String name) {
        SharedPreferences prefs = context.getSharedPreferences("secure", Context.MODE_PRIVATE);
        // Usar EncryptedSharedPreferences en vez de SharedPreferences
    }
}
```

---

**Última actualización**: 2025-12-26
**Versión de protocolo**: v2
