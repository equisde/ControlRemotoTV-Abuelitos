package com.remotetv.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Gestor de aplicaciones del Android TV remoto
 * Permite listar y lanzar apps instaladas en el TV
 */
public class TVAppManager {
    
    private AndroidTVRemoteProtocol protocol;
    
    public static class TVAppInfo {
        public String name;
        public String packageName;
        public String emoji;
        public int navigationSteps;  // Pasos para navegación
        
        public TVAppInfo(String name, String packageName, String emoji, int steps) {
            this.name = name;
            this.packageName = packageName;
            this.emoji = emoji;
            this.navigationSteps = steps;
        }
    }
    
    public TVAppManager(AndroidTVRemoteProtocol protocol) {
        this.protocol = protocol;
    }
    
    /**
     * Obtiene lista de apps populares de Android TV
     */
    public List<TVAppInfo> getPopularTVApps() {
        List<TVAppInfo> apps = new ArrayList<>();
        
        // Apps de streaming (la mayoría viene preinstalada)
        apps.add(new TVAppInfo("Netflix", "com.netflix.mediaclient", "🎬", 3));
        apps.add(new TVAppInfo("YouTube", "com.google.android.youtube.tv", "📺", 4));
        apps.add(new TVAppInfo("Prime Video", "com.amazon.amazonvideo.livingroom", "🎥", 5));
        apps.add(new TVAppInfo("Disney+", "com.disney.disneyplus", "🎪", 4));
        apps.add(new TVAppInfo("HBO Max", "com.hbo.hbogo", "🎭", 4));
        apps.add(new TVAppInfo("Hulu", "com.hulu.plus", "📹", 3));
        apps.add(new TVAppInfo("Twitch", "tv.twitch.android.app", "🎮", 3));
        apps.add(new TVAppInfo("Spotify", "com.spotify.tv", "🎵", 3));
        apps.add(new TVAppInfo("Google Play Movies", "com.google.android.videos", "🎬", 2));
        apps.add(new TVAppInfo("YouTube Music", "com.google.android.youtube.tv.music", "🎶", 3));
        
        // Apps de noticias y deportes
        apps.add(new TVAppInfo("News", "com.google.android.tvnews", "📰", 2));
        apps.add(new TVAppInfo("ESPN", "com.espn.score_center", "⚽", 3));
        apps.add(new TVAppInfo("TuneIn Radio", "tunein.player", "📻", 3));
        
        // Utilidades
        apps.add(new TVAppInfo("Chrome", "com.google.android.tv.remote.service", "🌐", 2));
        apps.add(new TVAppInfo("Google Play", "com.android.vending", "🛒", 2));
        apps.add(new TVAppInfo("Configuración", "com.android.settings", "⚙️", 1));
        apps.add(new TVAppInfo("Home", "com.google.android.tvlauncher", "🏠", 1));
        
        // Juegos
        apps.add(new TVAppInfo("Google Play Games", "com.google.android.play.games", "🎮", 2));
        apps.add(new TVAppInfo("Stadia", "com.google.stadia.android", "🕹️", 2));
        
        // Ordenar alfabéticamente
        Collections.sort(apps, new Comparator<TVAppInfo>() {
            @Override
            public int compare(TVAppInfo a, TVAppInfo b) {
                return a.name.compareToIgnoreCase(b.name);
            }
        });
        
        return apps;
    }
    
    /**
     * Obtiene emoji según el tipo de app
     */
    public String getEmojiForApp(String appName) {
        String lower = appName.toLowerCase();
        
        if (lower.contains("netflix")) return "🎬";
        if (lower.contains("youtube")) return "📺";
        if (lower.contains("prime") || lower.contains("amazon")) return "🎥";
        if (lower.contains("disney")) return "🎪";
        if (lower.contains("hbo")) return "🎭";
        if (lower.contains("hulu")) return "📹";
        if (lower.contains("twitch")) return "🎮";
        if (lower.contains("spotify")) return "🎵";
        if (lower.contains("music")) return "🎶";
        if (lower.contains("news")) return "📰";
        if (lower.contains("sport") || lower.contains("espn")) return "⚽";
        if (lower.contains("radio")) return "📻";
        if (lower.contains("chrome") || lower.contains("browser")) return "🌐";
        if (lower.contains("play")) return "🛒";
        if (lower.contains("config") || lower.contains("setting")) return "⚙️";
        if (lower.contains("home") || lower.contains("launcher")) return "🏠";
        if (lower.contains("game") || lower.contains("stadia")) return "🎮";
        
        return "📺";
    }
    
    /**
     * Lanza una app en el TV mediante navegación
     * Envía secuencia de keycodes para abrir la app
     */
    public void launchTVApp(TVAppInfo app) {
        if (!protocol.isConnected()) {
            return;
        }
        
        new Thread(() -> {
            try {
                // Ir a home primero
                protocol.sendKeyCommand(3); // KEYCODE_HOME
                Thread.sleep(1000);
                
                // Navegar según los pasos necesarios
                for (int i = 0; i < app.navigationSteps; i++) {
                    protocol.sendKeyCommand(22); // KEYCODE_DPAD_RIGHT
                    Thread.sleep(300);
                }
                
                // Presionar SELECT para abrir
                protocol.sendKeyCommand(23); // KEYCODE_ENTER
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    /**
     * Obtiene app por nombre
     */
    public TVAppInfo getAppByName(String name) {
        List<TVAppInfo> apps = getPopularTVApps();
        for (TVAppInfo app : apps) {
            if (app.name.equalsIgnoreCase(name)) {
                return app;
            }
        }
        return null;
    }
    
    /**
     * Obtiene solo las apps que probablemente estén instaladas
     */
    public List<TVAppInfo> getInstalledTVApps() {
        // En un TV real, esto consultaría las apps instaladas
        // Por ahora retorna todas las populares
        // En futuro se puede mejorar consultando el TV
        return getPopularTVApps();
    }
}
