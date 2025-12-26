package com.remotetv.control;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

/**
 * Descubre Android TVs en la red local
 */
public class TVDiscovery {
    private static final String TAG = "TVDiscovery";
    private Context context;

    public TVDiscovery(Context context) {
        this.context = context;
    }

    /**
     * Descubre TVs escaneando puertos en la subred
     */
    public List<String> discoverTVs() {
        List<String> discoveredTVs = new ArrayList<>();
        
        try {
            // Obtener IP local
            String localIP = getLocalIP();
            if (localIP == null) return discoveredTVs;
            
            // Obtener subnet
            String subnet = localIP.substring(0, localIP.lastIndexOf('.') + 1);
            
            // Escanear direcciones IP comunes en la red
            for (int i = 1; i < 255; i++) {
                String ip = subnet + i;
                if (isTV(ip)) {
                    discoveredTVs.add(ip);
                    Log.i(TAG, "TV encontrado: " + ip);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en descubrimiento: " + e.getMessage());
        }
        
        return discoveredTVs;
    }

    /**
     * Verifica si un IP es un Android TV
     */
    private boolean isTV(String ip) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, 6466), 500);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Obtiene la IP local del dispositivo
     */
    private String getLocalIP() {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) return null;
            
            int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
            return Formatter.formatIpAddress(ipAddress);
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo IP: " + e.getMessage());
            return null;
        }
    }

    /**
     * Escanea un rango específico de IPs
     */
    public List<String> scanIPRange(String startIP, String endIP) {
        List<String> foundTVs = new ArrayList<>();
        
        try {
            String[] startParts = startIP.split("\\.");
            String[] endParts = endIP.split("\\.");
            
            int startNum = Integer.parseInt(startParts[3]);
            int endNum = Integer.parseInt(endParts[3]);
            
            String subnet = startIP.substring(0, startIP.lastIndexOf('.') + 1);
            
            for (int i = startNum; i <= endNum; i++) {
                String ip = subnet + i;
                if (isTV(ip)) {
                    foundTVs.add(ip);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error escaneando rango: " + e.getMessage());
        }
        
        return foundTVs;
    }
}
