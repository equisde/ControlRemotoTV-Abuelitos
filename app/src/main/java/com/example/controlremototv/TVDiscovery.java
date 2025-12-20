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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class TVDiscovery {
    private static final String TAG = "TVDiscovery";
    private final Context context;
    private final ExecutorService executor;
    private final Handler mainHandler;
    private final AtomicBoolean tvFound = new AtomicBoolean(false);
    private final Set<String> scannedHosts = new HashSet<>();

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
        tvFound.set(false);
        scannedHosts.clear();
        
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
        for (int i = 1; i < 255; i++) {
            final String host = subnet + "." + i;
            executor.execute(() -> {
                if (!tvFound.get() && checkAndroidTV(host)) {
                    synchronized (scannedHosts) {
                        if (!tvFound.get()) {
                            tvFound.set(true);
                            scannedHosts.add(host);
                            Log.d(TAG, "Android TV confirmada en: " + host);
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

        return tvFound.get();
    }

    private boolean checkAndroidTV(String host) {
        if (tvFound.get()) {
            return false;
        }
        
        // Solo verificar puerto ADB 5555 (el más específico de Android TV)
        if (checkADBPort(host)) {
            Log.d(TAG, "Android TV encontrada (ADB) en: " + host + ":5555");
            return true;
        }
        
        return false;
    }
    
    private boolean checkADBPort(String host) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, 5555), 500);
            
            // Verificar si responde como ADB
            socket.setSoTimeout(400);
            byte[] buffer = new byte[8];
            int read = socket.getInputStream().read(buffer);
            socket.close();
            
            // Si hay datos o timeout, probablemente es ADB
            if (read > 0) {
                Log.d(TAG, "Dispositivo ADB detectado en: " + host);
                return true;
            }
        } catch (SocketTimeoutException e) {
            // Timeout al leer, pero puerto abierto - es ADB
            return true;
        } catch (IOException e) {
            // Puerto cerrado o no es ADB
        }
        return false;
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
