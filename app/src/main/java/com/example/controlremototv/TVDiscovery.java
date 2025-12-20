package com.example.controlremototv;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
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
    private String foundIp = null;

    public interface DiscoveryCallback {
        void onTVFound(String ip, String name);
        void onDiscoveryFailed(String error);
    }

    public TVDiscovery(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void discoverTV(DiscoveryCallback callback) {
        tvFound.set(false);
        foundIp = null;
        
        executor.execute(() -> {
            try {
                String localIp = getLocalIpAddress();
                if (localIp == null) {
                    mainHandler.post(() -> 
                        callback.onDiscoveryFailed("No se puede obtener dirección IP. Verifica que estés conectado a WiFi."));
                    return;
                }

                Log.d(TAG, "Tu IP local: " + localIp);
                String subnet = localIp.substring(0, localIp.lastIndexOf('.'));
                
                // Buscar usando ARP (más confiable que port scanning)
                String tvIp = findTVByARP(subnet);
                
                if (tvIp != null && !tvFound.getAndSet(true)) {
                    foundIp = tvIp;
                    String finalIp = tvIp;
                    Log.d(TAG, "✅ Android TV encontrada: " + finalIp);
                    mainHandler.post(() -> 
                        callback.onTVFound(finalIp, "Android TV (" + finalIp + ")"));
                } else {
                    mainHandler.post(() -> 
                        callback.onDiscoveryFailed("No se encontró Android TV.\n\nAsegúrate de:\n1. La TV está encendida\n2. Conectada al mismo WiFi\n3. ADB habilitado en la TV"));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage(), e);
                mainHandler.post(() -> 
                    callback.onDiscoveryFailed("Error al buscar: " + e.getMessage()));
            }
        });
    }

    private String findTVByARP(String subnet) {
        Log.d(TAG, "Buscando dispositivos en subnet: " + subnet + ".0/24");
        
        // Primero hacer ping a toda la subnet para popular tabla ARP
        ExecutorService pingExecutor = Executors.newFixedThreadPool(50);
        for (int i = 1; i < 255; i++) {
            final String host = subnet + "." + i;
            pingExecutor.execute(() -> {
                try {
                    InetAddress.getByName(host).isReachable(200);
                } catch (Exception ignored) {}
            });
        }
        
        pingExecutor.shutdown();
        try {
            pingExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ahora leer tabla ARP y buscar dispositivos Android
        try {
            Process process = Runtime.getRuntime().exec("cat /proc/net/arp");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            String line;
            while ((line = reader.readLine()) != null) {
                // Formato: IP address HW type Flags HW address Mask Device
                String[] parts = line.split("\\s+");
                if (parts.length >= 4 && !line.contains("IP address")) {
                    String ip = parts[0];
                    String mac = parts[3];
                    
                    Log.d(TAG, "ARP: " + ip + " -> MAC: " + mac);
                    
                    // Verificar si es Android TV por MAC address (OUI)
                    if (isAndroidTVMac(mac) || ip.startsWith(subnet)) {
                        // Verificar que tenga puerto 5555 abierto
                        if (checkADBPort(ip)) {
                            reader.close();
                            return ip;
                        }
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            Log.e(TAG, "Error leyendo ARP: " + e.getMessage());
        }
        
        return null;
    }
    
    private boolean isAndroidTVMac(String mac) {
        if (mac == null || mac.equals("00:00:00:00:00:00")) {
            return false;
        }
        
        String macUpper = mac.toUpperCase();
        
        // OUI de fabricantes comunes de Android TV
        String[] androidTVOUIs = {
            "D0:63:B4", // Google (Chromecast)
            "54:60:09", // Google
            "30:8C:FB", // Google
            "6C:AD:F8", // Google (Nest)
            "34:AF:2C", // Xiaomi (Mi Box)
            "64:09:80", // Xiaomi
            "F4:8E:92", // Xiaomi
            "00:04:4B", // NVIDIA (Shield)
            "00:04:4F", // NVIDIA
            "E4:F8:9C", // Amazon (Fire TV)
            "74:C6:3B", // Amazon
            "AC:63:BE", // Amazon
        };
        
        for (String oui : androidTVOUIs) {
            if (macUpper.startsWith(oui)) {
                Log.d(TAG, "✅ MAC de Android TV detectada: " + mac);
                return true;
            }
        }
        
        return false;
    }

    private boolean checkADBPort(String host) {
        try {
            java.net.Socket socket = new java.net.Socket();
            socket.connect(new java.net.InetSocketAddress(host, 5555), 500);
            socket.close();
            Log.d(TAG, "✅ Puerto ADB 5555 abierto en: " + host);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getLocalIpAddress() {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null && wifiManager.isWifiEnabled()) {
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
            Log.e(TAG, "Error obteniendo IP: " + e.getMessage());
        }
        return null;
    }
}
