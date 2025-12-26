package com.remotetv.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.util.TypedValue;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Configuración accesible para personas de tercera edad
 * - Opciones simples y claras
 * - Botones grandes
 * - Sin jerga técnica
 */
public class SettingsActivity extends AppCompatActivity {
    
    private ElderlyAccessibilityManager accessibilityManager;
    private RadioGroup fontSizeGroup, buttonSizeGroup, autoDisconnectGroup;
    private Switch highContrastSwitch, voiceFeedbackSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        accessibilityManager = new ElderlyAccessibilityManager(this);

        // Textos de títulos
        TextView titleText = findViewById(R.id.title_text);
        if (titleText != null) {
            titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            titleText.setTextColor(Color.BLACK);
        }

        // TAMAÑO DE LETRA
        TextView fontLabel = findViewById(R.id.font_size_label);
        if (fontLabel != null) {
            fontLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }

        fontSizeGroup = findViewById(R.id.font_size_group);
        if (fontSizeGroup != null) {
            fontSizeGroup.check(
                accessibilityManager.getFontSize() == 0 ? R.id.radio_font_normal :
                accessibilityManager.getFontSize() == 1 ? R.id.radio_font_large :
                R.id.radio_font_xlarge
            );

            fontSizeGroup.setOnCheckedChangeListener((group, checkedId) -> {
                int size = checkedId == R.id.radio_font_normal ? 0 :
                          checkedId == R.id.radio_font_large ? 1 : 2;
                accessibilityManager.setFontSize(size);
            });
        }

        // TAMAÑO DE BOTONES
        TextView buttonLabel = findViewById(R.id.button_size_label);
        if (buttonLabel != null) {
            buttonLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }

        buttonSizeGroup = findViewById(R.id.button_size_group);
        if (buttonSizeGroup != null) {
            buttonSizeGroup.check(
                accessibilityManager.getButtonSize() == 0 ? R.id.radio_button_normal :
                accessibilityManager.getButtonSize() == 1 ? R.id.radio_button_large :
                R.id.radio_button_xlarge
            );

            buttonSizeGroup.setOnCheckedChangeListener((group, checkedId) -> {
                int size = checkedId == R.id.radio_button_normal ? 0 :
                          checkedId == R.id.radio_button_large ? 1 : 2;
                accessibilityManager.setButtonSize(size);
            });
        }

        // CONTRASTE ALTO
        TextView contrastLabel = findViewById(R.id.contrast_label);
        if (contrastLabel != null) {
            contrastLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }

        highContrastSwitch = findViewById(R.id.contrast_switch);
        if (highContrastSwitch != null) {
            highContrastSwitch.setChecked(accessibilityManager.isHighContrast());
            highContrastSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                accessibilityManager.setHighContrast(isChecked);
            });
        }

        // FEEDBACK DE VOZ
        TextView voiceLabel = findViewById(R.id.voice_label);
        if (voiceLabel != null) {
            voiceLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }

        voiceFeedbackSwitch = findViewById(R.id.voice_switch);
        if (voiceFeedbackSwitch != null) {
            voiceFeedbackSwitch.setChecked(accessibilityManager.isVoiceFeedbackEnabled());
            voiceFeedbackSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                accessibilityManager.setVoiceFeedback(isChecked);
            });
        }

        // DESCONEXIÓN AUTOMÁTICA
        TextView disconnectLabel = findViewById(R.id.disconnect_label);
        if (disconnectLabel != null) {
            disconnectLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }

        autoDisconnectGroup = findViewById(R.id.disconnect_group);
        if (autoDisconnectGroup != null) {
            int minutes = accessibilityManager.getAutoDisconnectMinutes();
            int checkedId = minutes == 15 ? R.id.radio_disconnect_15 :
                           minutes == 30 ? R.id.radio_disconnect_30 :
                           R.id.radio_disconnect_60;
            autoDisconnectGroup.check(checkedId);

            autoDisconnectGroup.setOnCheckedChangeListener((group, checkedId2) -> {
                int newMinutes = checkedId2 == R.id.radio_disconnect_15 ? 15 :
                                checkedId2 == R.id.radio_disconnect_30 ? 30 : 60;
                accessibilityManager.setAutoDisconnectMinutes(newMinutes);
            });
        }

        // Botón para volver
        Button backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setHeight(dpToPx(72));
            backButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            backButton.setText("✓ GUARDAR Y VOLVER");
            backButton.setBackgroundColor(Color.rgb(0, 150, 0));
            backButton.setTextColor(Color.WHITE);
            backButton.setOnClickListener(v -> finish());
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    public static void startSettings(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }
}
