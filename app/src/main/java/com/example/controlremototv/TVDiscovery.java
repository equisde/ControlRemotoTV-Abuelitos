package com.example.controlremototv;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TVDiscovery {
    private static final String TAG = "TVDiscovery";
    private final Context context;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public interface DiscoveryCallback {
        void onTVFound(String ip, String name);
        void onDiscoveryFailed(String error);
    }

    public TVDiscovery(Context context) {
        this.context = context;
        this.executor = Executors.newFixedThreadPool(30);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void discoverTV(DiscoveryCallback callback) {
        executor.execute(() -> {
            try {
                String localIp = getLocalIpAddress();
                if (localIp == null) {
                    mainHandler.post(() -> 
                        callback.onDiscoveryFailed("No se puede obtener dirección IP"));
                    return;
                }

                String subnet = localIp.substring(0, localIp.lastIndexOf('.'));
                Log.d(TAG, "Buscando Android TV en subnet: " + subnet);

                boolean found = scanNetwork(subnet, callback);
                
                if (!found) {
                    mainHandler.post(() -> 
                        callback.onDiscoveryFailed("No se encontró ninguna TV"));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error en descubrimiento: " + e.getMessage());
                mainHandler.post(() -> 
                    callback.onDiscoveryFailed(e.getMessage()));
            }
        });
    }

    private boolean scanNetwork(String subnet, DiscoveryCallback callback) {
        final boolean[] found = {false};
        
        for (int i = 1; i < 255; i++) {
            final String host = subnet + "." + i;
            executor.execute(() -> {
                if (checkAndroidTV(host)) {
                    synchronized (found) {
                        if (!found[0]) {
                            found[0] = true;
                            mainHandler.post(() -> 
                                callback.onTVFound(host, "Android TV"));
                        }
                    }
                }
            });
        }

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return found[0];
    }

    private boolean checkAndroidTV(String host) {
        // Verificar puerto 5555 (ADB - específico de Android)
        if (checkADBPort(host)) {
            Log.d(TAG, "Android TV encontrada (ADB) en: " + host + ":5555");
            return true;
        }
        
        // Verificar puerto 8008 (Google Cast - usado por Android TV)
        if (checkPort(host, 8008, 300)) {
            Log.d(TAG, "Android TV encontrada (Cast) en: " + host + ":8008");
            return true;
        }
        
        // Verificar puerto 9000 (Android TV Remote Service)
        if (checkPort(host, 9000, 300)) {
            Log.d(TAG, "Android TV encontrada (Remote) en: " + host + ":9000");
            return true;
        }
        
        return false;
    }
    
    private boolean checkADBPort(String host) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, 5555), 400);
            
            // Verificar si responde como ADB
            socket.setSoTimeout(300);
            byte[] buffer = new byte[4];
            int read = socket.getInputStream().read(buffer);
            socket.close();
            
            // Si hay datos, probablemente es ADB
            if (read > 0) {
                Log.d(TAG, "Dispositivo ADB detectado en: " + host);
                return true;
            }
        } catch (SocketTimeoutException e) {
            // Timeout al leer, pero puerto abierto - puede ser ADB
            return true;
        } catch (IOException e) {
            // Puerto cerrado o no es ADB
        }
        return false;
    }
    
    private boolean checkPort(String host, int port, int timeout) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), timeout);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String getLocalIpAddress() {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
                if (ipAddress != 0) {
                    return String.format("%d.%d.%d.%d",
                        (ipAddress & 0xff),
                        (ipAddress >> 8 & 0xff),
                        (ipAddress >> 16 & 0xff),
                        (ipAddress >> 24 & 0xff));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo IP local: " + e.getMessage());
        }
        return null;
    }
}
