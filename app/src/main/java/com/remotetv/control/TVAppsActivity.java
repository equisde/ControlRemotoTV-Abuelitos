package com.remotetv.control;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.TypedValue;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

/**
 * Pantalla para seleccionar y lanzar apps del Android TV
 * Interfaz accesible para mayores
 */
public class TVAppsActivity extends AppCompatActivity {
    
    private TVAppManager appManager;
    private ElderlyAccessibilityManager accessibilityManager;
    private AndroidTVRemoteProtocol protocol;
    private ListView appsListView;
    private List<TVAppManager.TVAppInfo> apps;
    private TVAppsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);

        // Obtener TV IP del intent
        String tvIP = getIntent().getStringExtra("TV_IP");
        protocol = new AndroidTVRemoteProtocol(this, tvIP);
        
        appManager = new TVAppManager(protocol);
        accessibilityManager = new ElderlyAccessibilityManager(this);

        // Encontrar vistas
        TextView titleText = findViewById(R.id.title_text);
        appsListView = findViewById(R.id.apps_list_view);
        Button backButton = findViewById(R.id.back_button);

        // Configurar título
        if (titleText != null) {
            titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
            titleText.setTextColor(Color.BLACK);
            titleText.setText("APLICACIONES DEL TV");
        }

        // Cargar apps del TV
        loadTVApps();

        // Configurar click en lista
        appsListView.setOnItemClickListener((parent, view, position, id) -> {
            TVAppManager.TVAppInfo app = apps.get(position);
            launchTVApp(app);
        });

        // Botón volver
        if (backButton != null) {
            backButton.setHeight(dpToPx(72));
            backButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            backButton.setText("↩️ VOLVER");
            backButton.setBackgroundColor(Color.rgb(200, 0, 0));
            backButton.setTextColor(Color.WHITE);
            backButton.setOnClickListener(v -> finish());
        }
    }

    private void loadTVApps() {
        new Thread(() -> {
            apps = appManager.getInstalledTVApps();
            
            runOnUiThread(() -> {
                if (apps.isEmpty()) {
                    Toast.makeText(TVAppsActivity.this, 
                        "No hay apps en el TV", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    adapter = new TVAppsAdapter(TVAppsActivity.this, apps, accessibilityManager);
                    appsListView.setAdapter(adapter);
                }
            });
        }).start();
    }

    private void launchTVApp(TVAppManager.TVAppInfo app) {
        accessibilityManager.logAction("Abriendo en TV: " + app.name);
        
        Toast.makeText(this, app.emoji + " Abriendo " + app.name + " en TV...", 
            Toast.LENGTH_SHORT).show();
        
        appManager.launchTVApp(app);
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    public static void startTVApps(Context context, String tvIP) {
        android.content.Intent intent = new android.content.Intent(context, TVAppsActivity.class);
        intent.putExtra("TV_IP", tvIP);
        context.startActivity(intent);
    }
}
