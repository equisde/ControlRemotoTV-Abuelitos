package com.example.controlremototv;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    }

    public void sendCommand(Command command) {
        if (tvIp == null) {
            Log.e(TAG, "TV IP no configurada");
            return;
        }

        executor.execute(() -> {
            try {
                sendADBCommand(command.keyCode);
            } catch (Exception e) {
                Log.e(TAG, "Error enviando comando: " + e.getMessage());
            }
        });
    }

    private void sendADBCommand(String keyCode) throws IOException {
        String urlString = "http://" + tvIp + ":5555/input?key=" + keyCode;
        
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            
            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Comando enviado: " + keyCode + ", Respuesta: " + responseCode);
            
            conn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Error en sendADBCommand: " + e.getMessage());
            sendAlternativeCommand(keyCode);
        }
    }

    private void sendAlternativeCommand(String keyCode) {
        try {
            String urlString = "http://" + tvIp + ":8080/remote?key=" + keyCode;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1000);
            conn.getResponseCode();
            conn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Error en comando alternativo: " + e.getMessage());
        }
    }
}
