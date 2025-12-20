package com.example.controlremototv;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RemoteActivity extends AppCompatActivity {

    private TVController tvController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        tvController = TVController.getInstance();

        setupButtons();
    }

    private void setupButtons() {
        findViewById(R.id.btnPower).setOnClickListener(v -> {
            mostrarFeedback("Encender/Apagar");
            tvController.sendCommand(TVController.Command.POWER);
        });

        findViewById(R.id.btnVolumeUp).setOnClickListener(v -> {
            mostrarFeedback("Subiendo volumen");
            tvController.sendCommand(TVController.Command.VOLUME_UP);
        });

        findViewById(R.id.btnVolumeDown).setOnClickListener(v -> {
            mostrarFeedback("Bajando volumen");
            tvController.sendCommand(TVController.Command.VOLUME_DOWN);
        });

        findViewById(R.id.btnMute).setOnClickListener(v -> {
            mostrarFeedback("Silencio");
            tvController.sendCommand(TVController.Command.MUTE);
        });

        findViewById(R.id.btnChannelUp).setOnClickListener(v -> {
            mostrarFeedback("Canal arriba");
            tvController.sendCommand(TVController.Command.CHANNEL_UP);
        });

        findViewById(R.id.btnChannelDown).setOnClickListener(v -> {
            mostrarFeedback("Canal abajo");
            tvController.sendCommand(TVController.Command.CHANNEL_DOWN);
        });

        findViewById(R.id.btnUp).setOnClickListener(v -> {
            tvController.sendCommand(TVController.Command.DPAD_UP);
        });

        findViewById(R.id.btnDown).setOnClickListener(v -> {
            tvController.sendCommand(TVController.Command.DPAD_DOWN);
        });

        findViewById(R.id.btnLeft).setOnClickListener(v -> {
            tvController.sendCommand(TVController.Command.DPAD_LEFT);
        });

        findViewById(R.id.btnRight).setOnClickListener(v -> {
            tvController.sendCommand(TVController.Command.DPAD_RIGHT);
        });

        findViewById(R.id.btnOk).setOnClickListener(v -> {
            mostrarFeedback("OK");
            tvController.sendCommand(TVController.Command.DPAD_CENTER);
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            mostrarFeedback("Atrás");
            tvController.sendCommand(TVController.Command.BACK);
        });

        findViewById(R.id.btnHome).setOnClickListener(v -> {
            mostrarFeedback("Inicio");
            tvController.sendCommand(TVController.Command.HOME);
        });

        findViewById(R.id.btnMenu).setOnClickListener(v -> {
            mostrarFeedback("Menú");
            tvController.sendCommand(TVController.Command.MENU);
        });
    }

    private void mostrarFeedback(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
