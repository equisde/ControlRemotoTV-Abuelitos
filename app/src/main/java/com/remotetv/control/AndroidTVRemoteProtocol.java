package com.remotetv.control;

import android.util.Log;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * Protocolo Android TV Remote v2 - Conexión segura SSL/TLS
 */
public class AndroidTVRemoteProtocol {
    private static final String TAG = "TVRemote";
    private static final int PORT = 6466;
    private static final int PAIRING_PORT = 6467;

    private String tvIP;
    private int port;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private boolean connected;

    public AndroidTVRemoteProtocol(String tvIP) {
        this.tvIP = tvIP;
        this.port = PORT;
        this.connected = false;
    }

    /**
     * Conecta con el TV usando SSL/TLS
     */
    public boolean connect() {
        try {
            // Crear socket SSL que acepta cualquier certificado (TV antiguo)
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
            }}, new SecureRandom());

            socket = sslContext.getSocketFactory().createSocket(tvIP, port);
            socket.setTcpNoDelay(true);
            
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            
            connected = true;
            Log.i(TAG, "Conectado a TV: " + tvIP);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error conectando: " + e.getMessage());
            return false;
        }
    }

    /**
     * Desconecta del TV
     */
    public void disconnect() {
        try {
            if (socket != null) socket.close();
            connected = false;
            Log.i(TAG, "Desconectado");
        } catch (IOException e) {
            Log.e(TAG, "Error desconectando: " + e.getMessage());
        }
    }

    /**
     * Envía comando de tecla
     */
    public boolean sendKeyCommand(int keyCode) {
        if (!connected) return false;
        try {
            // Protocolo: enviar estructura de comando
            byte[] command = buildKeyCommand(keyCode);
            output.write(command);
            output.flush();
            Log.d(TAG, "Comando enviado: " + keyCode);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error enviando comando: " + e.getMessage());
            return false;
        }
    }

    /**
     * Construye comando de tecla en protocolo v2
     */
    private byte[] buildKeyCommand(int keyCode) {
        // Estructura simplificada del protocolo
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeByte(0x00); // Tipo: Key event
            dos.writeByte(keyCode & 0xFF);
            dos.flush();
        } catch (IOException e) {
            Log.e(TAG, "Error building command: " + e.getMessage());
        }
        return baos.toByteArray();
    }

    /**
     * Códigos de tecla estándar Android
     */
    public static class KeyCodes {
        public static final int KEYCODE_HOME = 3;
        public static final int KEYCODE_BACK = 4;
        public static final int KEYCODE_DPAD_UP = 19;
        public static final int KEYCODE_DPAD_DOWN = 20;
        public static final int KEYCODE_DPAD_LEFT = 21;
        public static final int KEYCODE_DPAD_RIGHT = 22;
        public static final int KEYCODE_ENTER = 23;
        public static final int KEYCODE_MENU = 82;
        public static final int KEYCODE_POWER = 26;
        public static final int KEYCODE_VOLUME_UP = 24;
        public static final int KEYCODE_VOLUME_DOWN = 25;
        public static final int KEYCODE_MUTE = 91;
        public static final int KEYCODE_MEDIA_PLAY_PAUSE = 85;
        public static final int KEYCODE_MEDIA_PLAY = 126;
        public static final int KEYCODE_MEDIA_PAUSE = 127;
        public static final int KEYCODE_MEDIA_NEXT = 87;
        public static final int KEYCODE_MEDIA_PREVIOUS = 88;
        public static final int KEYCODE_MEDIA_FAST_FORWARD = 90;
        public static final int KEYCODE_MEDIA_REWIND = 89;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getTVIP() {
        return tvIP;
    }
}
