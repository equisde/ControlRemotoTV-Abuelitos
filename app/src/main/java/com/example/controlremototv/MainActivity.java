package com.example.controlremototv;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvStatus;
    private TextView tvDeviceInfo;
    private Button btnBuscar;
    private Button btnConectar;
    private ProgressBar progressBar;
    private TVDiscovery tvDiscovery;
    private String foundTvIp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);
        tvDeviceInfo = findViewById(R.id.tvDeviceInfo);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnConectar = findViewById(R.id.btnConectar);
        progressBar = findViewById(R.id.progressBar);

        tvDiscovery = new TVDiscovery(this);

        btnBuscar.setOnClickListener(v -> buscarTV());
        btnConectar.setOnClickListener(v -> conectarTV());
    }

    private void buscarTV() {
        btnBuscar.setEnabled(false);
        btnConectar.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        tvStatus.setText("Buscando tu TV...\nPor favor espera");
        tvDeviceInfo.setVisibility(View.GONE);

        tvDiscovery.discoverTV(new TVDiscovery.DiscoveryCallback() {
            @Override
            public void onTVFound(String ip, String name) {
                runOnUiThread(() -> {
                    foundTvIp = ip;
                    progressBar.setVisibility(View.GONE);
                    btnBuscar.setEnabled(true);
                    btnConectar.setVisibility(View.VISIBLE);
                    tvStatus.setText("✓ TV Encontrada");
                    tvDeviceInfo.setText(name + "\n" + ip);
                    tvDeviceInfo.setVisibility(View.VISIBLE);
                    
                    Toast.makeText(MainActivity.this, 
                        "TV encontrada! Presiona CONECTAR", 
                        Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onDiscoveryFailed(String error) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnBuscar.setEnabled(true);
                    tvStatus.setText("No se encontró ninguna TV\n" + 
                        "Verifica que tu TV esté encendida\ny conectada al mismo WiFi");
                    
                    Toast.makeText(MainActivity.this, 
                        "No se encontró TV. Intenta de nuevo", 
                        Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void conectarTV() {
        if (foundTvIp != null) {
            TVController.getInstance().setTvIp(foundTvIp);
            
            Toast.makeText(this, 
                "¡Conectado! Abriendo control remoto", 
                Toast.LENGTH_SHORT).show();
            
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, RemoteActivity.class);
                startActivity(intent);
            }, 500);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnBuscar.setEnabled(true);
    }
}
