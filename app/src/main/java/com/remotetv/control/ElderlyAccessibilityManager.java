package com.remotetv.control;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Gestor de datos y configuración accesible para adultos mayores
 * - Textos más grandes
 * - Fuentes claras
 * - Configuración de accesibilidad
 */
public class ElderlyAccessibilityManager {
    private static final String PREF_FILE = "elderly_prefs";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_BRIGHTNESS = "brightness";
    private static final String KEY_VOICE_FEEDBACK = "voice_feedback";
    private static final String KEY_HIGH_CONTRAST = "high_contrast";
    private static final String KEY_BUTTON_SIZE = "button_size";
    private static final String KEY_AUTO_DISCONNECT = "auto_disconnect";
    private static final String KEY_FAVORITE_APPS = "favorite_apps";
    
    private SharedPreferences prefs;

    public ElderlyAccessibilityManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    // CONFIGURACIÓN DE FUENTES
    public void setFontSize(int size) {
        // 0=Normal, 1=Grande, 2=Muy Grande
        prefs.edit().putInt(KEY_FONT_SIZE, size).apply();
    }

    public int getFontSize() {
        return prefs.getInt(KEY_FONT_SIZE, 1); // Por defecto: Grande
    }

    public float getFontSizeInPixels() {
        switch (getFontSize()) {
            case 0: return 14f; // Normal
            case 1: return 18f; // Grande (por defecto para mayores)
            case 2: return 24f; // Muy Grande
            default: return 18f;
        }
    }

    // CONFIGURACIÓN DE BOTONES
    public void setButtonSize(int size) {
        // 0=Normal, 1=Grande, 2=Muy Grande
        prefs.edit().putInt(KEY_BUTTON_SIZE, size).apply();
    }

    public int getButtonSize() {
        return prefs.getInt(KEY_BUTTON_SIZE, 2); // Por defecto: Muy Grande
    }

    public int getButtonHeightInDp() {
        switch (getButtonSize()) {
            case 0: return 48;   // Normal
            case 1: return 72;   // Grande
            case 2: return 96;   // Muy Grande (por defecto)
            default: return 96;
        }
    }

    public int getButtonWidthInDp() {
        switch (getButtonSize()) {
            case 0: return 48;
            case 1: return 72;
            case 2: return 96;
            default: return 96;
        }
    }

    // CONTRASTE ALTO
    public void setHighContrast(boolean enabled) {
        prefs.edit().putBoolean(KEY_HIGH_CONTRAST, enabled).apply();
    }

    public boolean isHighContrast() {
        return prefs.getBoolean(KEY_HIGH_CONTRAST, true); // Por defecto: ON para mayores
    }

    // FEEDBACK DE VOZ (text-to-speech)
    public void setVoiceFeedback(boolean enabled) {
        prefs.edit().putBoolean(KEY_VOICE_FEEDBACK, enabled).apply();
    }

    public boolean isVoiceFeedbackEnabled() {
        return prefs.getBoolean(KEY_VOICE_FEEDBACK, true); // Por defecto: ON
    }

    // DESCONEXIÓN AUTOMÁTICA
    public void setAutoDisconnectMinutes(int minutes) {
        // Desconecta automáticamente después de X minutos sin actividad
        prefs.edit().putInt(KEY_AUTO_DISCONNECT, minutes).apply();
    }

    public int getAutoDisconnectMinutes() {
        return prefs.getInt(KEY_AUTO_DISCONNECT, 30); // Por defecto: 30 min
    }

    // APLICACIONES FAVORITAS
    public void addFavoriteApp(String appName) {
        String favorites = prefs.getString(KEY_FAVORITE_APPS, "");
        if (!favorites.contains(appName)) {
            favorites += (favorites.isEmpty() ? "" : ",") + appName;
            prefs.edit().putString(KEY_FAVORITE_APPS, favorites).apply();
        }
    }

    public String getFavoriteApps() {
        return prefs.getString(KEY_FAVORITE_APPS, "");
    }

    // INFORMACIÓN DE HORA (para mayor seguridad)
    public String getCurrentTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    public String getCurrentDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d 'de' MMMM", new Locale("es", "ES"));
        return sdf.format(new Date());
    }

    // LOGGING para monitoreo
    public void logAction(String action) {
        // Útil para debugging remoto si es necesario
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());
        // Log.i("ElderlyAccess", timestamp + " - " + action);
    }
}
