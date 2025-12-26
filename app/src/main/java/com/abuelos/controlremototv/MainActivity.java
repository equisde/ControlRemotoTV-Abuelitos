package com.abuelos.controlremototv;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.abuelos.controlremototv.databinding.ActivityMainBinding;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppManager appManager;
    private AppAdapter adapter;
    private int currentScreen = 0; // 0 = remoto, 1 = apps

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appManager = new AppManager(this);
        showRemoteScreen();

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        // Botones de navegación
        binding.btnUp.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_DPAD_UP));
        binding.btnDown.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_DPAD_DOWN));
        binding.btnLeft.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_DPAD_LEFT));
        binding.btnRight.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_DPAD_RIGHT));
        binding.btnSelect.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_DPAD_CENTER));

        // Botones de volumen
        binding.btnVolUp.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_VOLUME_UP));
        binding.btnVolDown.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_VOLUME_DOWN));
        binding.btnMute.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_MUTE));

        // Botones especiales
        binding.btnPower.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_POWER));
        binding.btnHome.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_HOME));
        binding.btnBack.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_BACK));
        binding.btnApps.setOnClickListener(v -> showAppsScreen());

        // Números
        for (int i = 0; i <= 9; i++) {
            int btnId = getResources().getIdentifier("btnNum" + i, "id", getPackageName());
            if (btnId != 0) {
                findViewById(btnId).setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_0 + i));
            }
        }
    }

    private void sendKey(int keyCode) {
        appManager.sendKeyEvent(keyCode);
    }

    private void showRemoteScreen() {
        currentScreen = 0;
        binding.remoteLayout.setVisibility(View.VISIBLE);
        binding.appsLayout.setVisibility(View.GONE);
    }

    private void showAppsScreen() {
        currentScreen = 1;
        binding.remoteLayout.setVisibility(View.GONE);
        binding.appsLayout.setVisibility(View.VISIBLE);

        List<AppInfo> apps = appManager.getInstalledApps();
        adapter = new AppAdapter(apps, app -> {
            appManager.launchApp(app.packageName);
            showRemoteScreen();
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        binding.appsRecycler.setLayoutManager(layoutManager);
        binding.appsRecycler.setAdapter(adapter);

        binding.btnBackFromApps.setOnClickListener(v -> showRemoteScreen());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (currentScreen == 1) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                showRemoteScreen();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
