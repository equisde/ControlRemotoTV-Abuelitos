package com.remotetv.control;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Gestiona preferencias y configuración
 */
public class PreferencesManager {
    private static final String PREF_FILE = "tvremote_prefs";
    private static final String KEY_TV_IP = "tv_ip";
    private static final String KEY_TV_NAME = "tv_name";
    private static final String KEY_PAIRED = "is_paired";
    
    private SharedPreferences prefs;

    public PreferencesManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    public void saveTVIP(String ip) {
        prefs.edit().putString(KEY_TV_IP, ip).apply();
    }

    public String getTVIP() {
        return prefs.getString(KEY_TV_IP, null);
    }

    public void saveTVName(String name) {
        prefs.edit().putString(KEY_TV_NAME, name).apply();
    }

    public String getTVName() {
        return prefs.getString(KEY_TV_NAME, "Mi TV");
    }

    public void setPaired(boolean paired) {
        prefs.edit().putBoolean(KEY_PAIRED, paired).apply();
    }

    public boolean isPaired() {
        return prefs.getBoolean(KEY_PAIRED, false);
    }

    public void clearAll() {
        prefs.edit().clear().apply();
    }
}
