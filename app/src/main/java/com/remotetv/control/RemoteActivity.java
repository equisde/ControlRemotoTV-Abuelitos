package com.remotetv.control;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.TypedValue;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Control Remoto ACCESIBLE para personas de tercera edad
 * 
 * Diseño especial:
 * - Botones ENORMES (96x96 dp)
 * - Textos gigantes y claros
 * - Emojis para mayor claridad visual
 * - Colores de alto contraste
 * - Sin botones pequeños o confusos
 * - Funciones simplificadas
 */
public class RemoteActivity extends AppCompatActivity {
    
    private String tvIP;
    private AndroidTVRemoteProtocol protocol;
    private ElderlyAccessibilityManager accessibilityManager;
    private TextView statusText, timeText;
    private int buttonHeightDp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_elderly);

        tvIP = getIntent().getStringExtra("TV_IP");
        accessibilityManager = new ElderlyAccessibilityManager(this);
        
        statusText = findViewById(R.id.status_text);
        timeText = findViewById(R.id.time_text);
        
        // Conectar con el TV
        protocol = new AndroidTVRemoteProtocol(tvIP);
        new Thread(() -> {
            if (!protocol.connect()) {
                runOnUiThread(() -> 
                    Toast.makeText(RemoteActivity.this, "Error conectando", Toast.LENGTH_SHORT).show()
                );
            } else {
                runOnUiThread(() -> statusText.setText("✅ TV Conectado"));
                accessibilityManager.logAction("Conectado a " + tvIP);
            }
        }).start();

        buttonHeightDp = accessibilityManager.getButtonHeightInDp();

        // Aplicar accesibilidad
        applyAccessibilitySettings();

        // Configurar botones GIGANTES
        setupBigNavigationButtons();
        setupMediaButtons();
        setupFavoriteAppsButtons();

        // Actualizar hora
        updateTime();
    }

    private void applyAccessibilitySettings() {
        float fontSize = accessibilityManager.getFontSizeInPixels();
        if (statusText != null) {
            statusText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize + 2);
        }
        if (timeText != null) {
            timeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        }

        // Alto contraste
        if (accessibilityManager.isHighContrast()) {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }
    }

    private void setupBigNavigationButtons() {
        // D-PAD - Botones ENORMES con emojis
        Button btnUp = findViewById(R.id.btn_up);
        Button btnDown = findViewById(R.id.btn_down);
        Button btnLeft = findViewById(R.id.btn_left);
        Button btnRight = findViewById(R.id.btn_right);
        Button btnOk = findViewById(R.id.btn_select);

        configurarBotonGrande(btnUp, "⬆️\nARRIBA", 19);
        configurarBotonGrande(btnDown, "⬇️\nABAJO", 20);
        configurarBotonGrande(btnLeft, "⬅️\nIZQ", 21);
        configurarBotonGrande(btnRight, "➡️\nDER", 22);
        configurarBotonGrande(btnOk, "✓\nOK", 23);

        // Botones de control simples
        Button btnHome = findViewById(R.id.btn_home);
        Button btnBack = findViewById(R.id.btn_back);

        configurarBotonGrande(btnHome, "🏠\nINICIO", 3);
        configurarBotonGrande(btnBack, "⬅️\nATRÁS", 4);
    }

    private void setupMediaButtons() {
        Button btnVolUp = findViewById(R.id.btn_volume_up);
        Button btnVolDown = findViewById(R.id.btn_volume_down);
        Button btnPlay = findViewById(R.id.btn_play);
        Button btnMute = findViewById(R.id.btn_mute);

        configurarBotonGrande(btnVolUp, "🔊+\nVOL+", 24);
        configurarBotonGrande(btnVolDown, "🔊-\nVOL-", 25);
        configurarBotonGrande(btnPlay, "▶️▮▮\nPLAY", 85);
        configurarBotonGrande(btnMute, "🔇\nSILENCIO", 91);
    }

    private void setupFavoriteAppsButtons() {
        // Botones para aplicaciones favoritas
        Button btnNetflix = findViewById(R.id.btn_netflix);
        Button btnYouTube = findViewById(R.id.btn_youtube);
        Button btnAllApps = findViewById(R.id.btn_all_apps);

        if (btnNetflix != null) {
            configurarBotonApp(btnNetflix, "🎬\nNETFLIX", () -> openApp("netflix"));
        }

        if (btnYouTube != null) {
            configurarBotonApp(btnYouTube, "📺\nYOUTUBE", () -> openApp("youtube"));
        }

        if (btnAllApps != null) {
            configurarBotonApp(btnAllApps, "📱\nTODAS", this::openAllApps);
        }
    }

    private void configurarBotonGrande(Button button, String text, int keyCode) {
        button.setHeight(dpToPx(buttonHeightDp));
        button.setMinimumHeight(dpToPx(buttonHeightDp));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        button.setText(text);
        
        button.setBackgroundColor(Color.rgb(0, 120, 215));
        button.setTextColor(Color.WHITE);
        button.setPadding(20, 20, 20, 20);

        button.setOnClickListener(v -> {
            sendKeyWithFeedback(keyCode, text);
        });

        button.setOnLongClickListener(v -> {
            repeatKey(keyCode);
            return true;
        });
    }

    private void configurarBotonApp(Button button, String text, Runnable action) {
        button.setHeight(dpToPx(buttonHeightDp));
        button.setMinimumHeight(dpToPx(buttonHeightDp));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        button.setText(text);
        button.setBackgroundColor(Color.rgb(50, 150, 50));
        button.setTextColor(Color.WHITE);
        button.setPadding(20, 20, 20, 20);

        button.setOnClickListener(v -> {
            action.run();
            if (accessibilityManager.isVoiceFeedbackEnabled()) {
                Toast.makeText(this, "Abriendo " + text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendKeyWithFeedback(int keyCode, String description) {
        sendKey(keyCode);
        Toast.makeText(this, description, Toast.LENGTH_SHORT).show();
        
        if (accessibilityManager.isVoiceFeedbackEnabled()) {
            speakText(description);
        }

        accessibilityManager.logAction("Tecla presionada: " + description);
    }

    private void repeatKey(int keyCode) {
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                if (protocol.isConnected()) {
                    protocol.sendKeyCommand(keyCode);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }).start();
    }

    private void sendKey(int keyCode) {
        new Thread(() -> {
            if (protocol.isConnected()) {
                protocol.sendKeyCommand(keyCode);
            }
        }).start();
    }

    private void openApp(String appName) {
        new Thread(() -> {
            if (!protocol.isConnected()) return;

            try {
                protocol.sendKeyCommand(3); // HOME
                Thread.sleep(1000);

                for (int i = 0; i < 3; i++) {
                    protocol.sendKeyCommand(22); // DPAD_RIGHT
                    Thread.sleep(300);
                }

                protocol.sendKeyCommand(23); // ENTER
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void openAllApps() {
        TVAppsActivity.startTVApps(this, tvIP);
    }

    private void speakText(String text) {
        // Text-to-Speech placeholder
    }

    private void updateTime() {
        if (timeText != null) {
            timeText.setText(accessibilityManager.getCurrentTimeString());
        }
        
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000);
                    runOnUiThread(this::updateTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (protocol != null) {
            protocol.disconnect();
        }
    }

    public static void startRemote(android.content.Context context, String tvIP) {
        android.content.Intent intent = new android.content.Intent(context, RemoteActivity.class);
        intent.putExtra("TV_IP", tvIP);
        context.startActivity(intent);
    }
}
