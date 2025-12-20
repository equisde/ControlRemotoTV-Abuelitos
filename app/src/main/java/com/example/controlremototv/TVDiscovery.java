package com.example.controlremototv;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TVDiscovery {
    private static final String TAG = "TVDiscovery";
    private final Context context;
    private final ExecutorService executor;
    private final Handler mainHandler;
    private final AtomicBoolean tvFound = new AtomicBoolean(false);

    public interface DiscoveryCallback {
        void onTVFound(String ip, String name);
        void onDiscoveryFailed(String error);
    }

    public TVDiscovery(Context context) {
        this.context = context;
        this.executor = Executors.newFixedThreadPool(50);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void discoverTV(DiscoveryCallback callback) {
        tvFound.set(false);
        
        executor.execute(() -> {
            try {
                String localIp = getLocalIpAddress();
                if (localIp == null) {
                    mainHandler.post(() -> 
                        callback.onDiscoveryFailed("Conéctate a WiFi primero"));
                    return;
                }

                Log.d(TAG, "Tu IP: " + localIp);
                String subnet = localIp.substring(0, localIp.lastIndexOf('.'));
                Log.d(TAG, "Buscando en: " + subnet + ".0/24");

                scanNetwork(subnet, callback);
                
                // Esperar a que termine el escaneo
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                if (!tvFound.get()) {
                    mainHandler.post(() -> 
                        callback.onDiscoveryFailed("No se encontró TV.\n\n¿Tienes ADB habilitado?\nLee: CONFIGURAR_TV.md"));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage(), e);
                mainHandler.post(() -> 
                    callback.onDiscoveryFailed("Error: " + e.getMessage()));
            }
        });
    }

    private void scanNetwork(String subnet, DiscoveryCallback callback) {
        Log.d(TAG, "Escaneando red completa...");
        
        for (int i = 1; i < 255; i++) {
            final String host = subnet + "." + i;
            executor.execute(() -> {
                if (!tvFound.get()) {
                    if (isReachable(host)) {
                        Log.d(TAG, "Host activo: " + host);
                        if (hasADB(host)) {
                            if (!tvFound.getAndSet(true)) {
                                Log.d(TAG, "✅ ANDROID TV ENCONTRADA: " + host);
                                mainHandler.post(() -> 
                                    callback.onTVFound(host, "Android TV"));
                            }
                        }
                    }
                }
            });
        }
    }

    private boolean isReachable(String host) {
        try {
            InetAddress address = InetAddress.getByName(host);
            return address.isReachable(300);
        } catch (IOException e) {
            return false;
        }
    }

    private boolean hasADB(String host) {
        // Verificar puerto 5555 (ADB)
        if (checkPort(host, 5555)) {
            Log.d(TAG, "Puerto ADB abierto: " + host + ":5555");
            return true;
        }
        return false;
    }

    private boolean checkPort(String host, int port) {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 500);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }

    private String getLocalIpAddress() {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null && wifiManager.isWifiEnabled()) {
                int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
                if (ipAddress != 0) {
                    String ip = String.format("%d.%d.%d.%d",
                        (ipAddress & 0xff),
                        (ipAddress >> 8 & 0xff),
                        (ipAddress >> 16 & 0xff),
                        (ipAddress >> 24 & 0xff));
                    Log.d(TAG, "IP local: " + ip);
                    return ip;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo IP: " + e.getMessage());
        }
        return null;
    }
}
