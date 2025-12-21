package com.example.controlremototv;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TVController {
    private static final String TAG = "TVController";
    private static TVController instance;
    private String tvIp;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public enum Command {
        POWER("KEYCODE_POWER"),
        VOLUME_UP("KEYCODE_VOLUME_UP"),
        VOLUME_DOWN("KEYCODE_VOLUME_DOWN"),
        MUTE("KEYCODE_VOLUME_MUTE"),
        CHANNEL_UP("KEYCODE_CHANNEL_UP"),
        CHANNEL_DOWN("KEYCODE_CHANNEL_DOWN"),
        DPAD_UP("KEYCODE_DPAD_UP"),
        DPAD_DOWN("KEYCODE_DPAD_DOWN"),
        DPAD_LEFT("KEYCODE_DPAD_LEFT"),
        DPAD_RIGHT("KEYCODE_DPAD_RIGHT"),
        DPAD_CENTER("KEYCODE_DPAD_CENTER"),
        BACK("KEYCODE_BACK"),
        HOME("KEYCODE_HOME"),
        MENU("KEYCODE_MENU");

        private final String keyCode;
        Command(String keyCode) {
            this.keyCode = keyCode;
        }
    }

    private TVController() {
        executor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static synchronized TVController getInstance() {
        if (instance == null) {
            instance = new TVController();
        }
        return instance;
    }

    public void setTvIp(String ip) {
        this.tvIp = ip;
        Log.d(TAG, "IP configurada: " + ip);
    }

    public void sendCommand(Command command) {
        if (tvIp == null) {
            Log.e(TAG, "TV IP no configurada");
            return;
        }

        executor.execute(() -> {
            try {
                Log.d(TAG, "Enviando comando: " + command.keyCode + " a " + tvIp);
                sendADBCommand(command.keyCode);
            } catch (Exception e) {
                Log.e(TAG, "Error enviando comando: " + e.getMessage(), e);
            }
        });
    }

    private void sendADBCommand(String keyCode) {
        Socket socket = null;
        try {
            // Conectar a ADB en puerto 5555
            socket = new Socket(tvIp, 5555);
            socket.setSoTimeout(3000);
            
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            
            // Enviar handshake ADB
            String connectMsg = "host:transport-any";
            sendADBMessage(out, connectMsg);
            
            // Leer respuesta
            byte[] response = new byte[4];
            in.read(response);
            String resp = new String(response);
            
            if (resp.equals("OKAY")) {
                // Enviar comando input keyevent
                String command = "shell:input keyevent " + keyCode;
                sendADBMessage(out, command);
                
                // Leer respuesta
                in.read(response);
                resp = new String(response);
                
                if (resp.equals("OKAY")) {
                    Log.d(TAG, "✅ Comando ejecutado: " + keyCode);
                } else {
                    Log.e(TAG, "Error en respuesta: " + resp);
                }
            } else {
                Log.e(TAG, "Error en handshake: " + resp);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error ADB: " + e.getMessage(), e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void sendADBMessage(OutputStream out, String message) throws IOException {
        String lengthHex = String.format("%04x", message.length());
        String fullMessage = lengthHex + message;
        out.write(fullMessage.getBytes());
        out.flush();
    }
}
