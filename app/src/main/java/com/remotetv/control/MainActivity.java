package com.remotetv.control;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.TypedValue;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

/**
 * Actividad principal ACCESIBLE para personas de tercera edad
 * - Botones grandes (96dp)
 * - Textos grandes y claros (18sp)
 * - Colores de alto contraste
 * - Instrucciones simples
 * - Sin jerga técnica
 */
public class MainActivity extends AppCompatActivity {
    
    private EditText ipInput;
    private Button scanButton, connectButton, settingsButton;
    private ProgressBar progressBar;
    private TextView statusText, resultsText, instructionsText, timeText;
    private TVDiscovery tvDiscovery;
    private PreferencesManager prefsManager;
    private ElderlyAccessibilityManager accessibilityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_elderly);

        // Inicializar managers
        tvDiscovery = new TVDiscovery(this);
        prefsManager = new PreferencesManager(this);
        accessibilityManager = new ElderlyAccessibilityManager(this);

        // Encontrar vistas
        ipInput = findViewById(R.id.ip_input);
        scanButton = findViewById(R.id.scan_button);
        connectButton = findViewById(R.id.connect_button);
        settingsButton = findViewById(R.id.settings_button);
        progressBar = findViewById(R.id.progress_bar);
        statusText = findViewById(R.id.status_text);
        resultsText = findViewById(R.id.results_text);
        instructionsText = findViewById(R.id.instructions_text);
        timeText = findViewById(R.id.time_text);

        // Aplicar configuración de accesibilidad
        applyAccessibilitySettings();

        // Mostrar hora actual
        updateTime();

        // Cargar IP guardada si existe
        String savedIP = prefsManager.getTVIP();
        if (savedIP != null) {
            ipInput.setText(savedIP);
        }

        // Configurar listeners
        scanButton.setOnClickListener(v -> {
            accessibilityManager.logAction("Iniciando escaneo de red");
            scanForTVs();
        });
        
        connectButton.setOnClickListener(v -> {
            accessibilityManager.logAction("Intentando conectar");
            connectToTV();
        });

        settingsButton.setOnClickListener(v -> {
            SettingsActivity.startSettings(MainActivity.this);
        });

        // Actualizar hora cada minuto
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000); // 1 minuto
                    runOnUiThread(this::updateTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    private void applyAccessibilitySettings() {
        // Tamaño de fuente
        float fontSize = accessibilityManager.getFontSizeInPixels();
        statusText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        resultsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        
        // Tamaño de botones
        int buttonHeight = accessibilityManager.getButtonHeightInDp();
        scanButton.setHeight(dpToPx(buttonHeight));
        connectButton.setHeight(dpToPx(buttonHeight));
        settingsButton.setHeight(dpToPx(buttonHeight));

        // Contraste alto
        if (accessibilityManager.isHighContrast()) {
            // Aplicar colores de alto contraste
            statusText.setTextColor(0xFF000000); // Negro
            getWindow().getDecorView().setBackgroundColor(0xFFFFFFFF); // Blanco
        }
    }

    private void updateTime() {
        if (timeText != null) {
            timeText.setText(accessibilityManager.getCurrentTimeString() + " | " + 
                            accessibilityManager.getCurrentDateString());
        }
    }

    private void scanForTVs() {
        progressBar.setVisibility(android.view.View.VISIBLE);
        statusText.setText("Buscando tu TV...");
        resultsText.setText("");
        instructionsText.setText("Espera un momento, estoy buscando...");

        new Thread(() -> {
            List<String> tvs = tvDiscovery.discoverTVs();
            runOnUiThread(() -> {
                progressBar.setVisibility(android.view.View.GONE);
                if (tvs.isEmpty()) {
                    statusText.setText("❌ No encontré tu TV");
                    instructionsText.setText(
                        "Por favor:\n" +
                        "1. Enciende tu TV\n" +
                        "2. Asegúrate que está conectado a WiFi\n" +
                        "3. Presiona el botón CONECTAR después\n\n" +
                        "O escribe el número de la TV manualmente (pregunta a alguien si no lo sabes)"
                    );
                    resultsText.setText("");
                } else {
                    statusText.setText("✅ ¡Encontré tu TV!");
                    instructionsText.setText("Presiona CONECTAR");
                    StringBuilder result = new StringBuilder();
                    for (String ip : tvs) {
                        result.append("TV encontrado: ").append(ip).append("\n");
                    }
                    resultsText.setText(result.toString());
                    if (!tvs.isEmpty()) {
                        ipInput.setText(tvs.get(0));
                    }
                }
            });
        }).start();
    }

    private void connectToTV() {
        String ip = ipInput.getText().toString().trim();
        if (ip.isEmpty()) {
            Toast.makeText(this, "Por favor escribe el número de tu TV", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(android.view.View.VISIBLE);
        statusText.setText("Conectando con tu TV...");
        instructionsText.setText("Espera un momento...");

        new Thread(() -> {
            AndroidTVRemoteProtocol protocol = new AndroidTVRemoteProtocol(ip);
            boolean connected = protocol.connect();
            
            runOnUiThread(() -> {
                progressBar.setVisibility(android.view.View.GONE);
                if (connected) {
                    protocol.disconnect();
                    prefsManager.saveTVIP(ip);
                    prefsManager.setPaired(true);
                    statusText.setText("✅ ¡Conectado!");
                    instructionsText.setText("Abriendo tu control remoto...");
                    accessibilityManager.logAction("Conexión exitosa con " + ip);
                    
                    // Esperar un momento antes de cambiar de pantalla
                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                        RemoteActivity.startRemote(MainActivity.this, ip);
                    }, 1000);
                } else {
                    statusText.setText("❌ No pude conectar");
                    instructionsText.setText(
                        "Verifica que:\n" +
                        "1. Tu TV está ENCENDIDO\n" +
                        "2. Tu TV está conectado a WiFi\n" +
                        "3. El número es correcto\n\n" +
                        "Intenta de nuevo"
                    );
                }
            });
        }).start();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
