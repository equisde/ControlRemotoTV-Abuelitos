package com.example.controlremototv;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
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
        this.executor = Executors.newFixedThreadPool(20);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void discoverTV(DiscoveryCallback callback) {
        executor.execute(() -> {
            try {
                String localIp = getLocalIpAddress();
                if (localIp == null) {
                    callback.onDiscoveryFailed("No se puede obtener dirección IP");
                    return;
                }

                String subnet = localIp.substring(0, localIp.lastIndexOf('.'));
                Log.d(TAG, "Buscando en subnet: " + subnet);

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
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return found[0];
    }

    private boolean checkAndroidTV(String host) {
        int[] ports = {5555, 8080, 9090};
        
        for (int port : ports) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), 500);
                socket.close();
                Log.d(TAG, "TV encontrada en: " + host + ":" + port);
                return true;
            } catch (IOException ignored) {
            }
        }
        return false;
    }

    private String getLocalIpAddress() {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
                return String.format("%d.%d.%d.%d",
                    (ipAddress & 0xff),
                    (ipAddress >> 8 & 0xff),
                    (ipAddress >> 16 & 0xff),
                    (ipAddress >> 24 & 0xff));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo IP local: " + e.getMessage());
        }
        return null;
    }
}
