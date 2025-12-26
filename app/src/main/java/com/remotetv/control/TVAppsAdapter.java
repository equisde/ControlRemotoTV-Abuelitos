package com.remotetv.control;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.util.TypedValue;
import java.util.List;

/**
 * Adaptador para mostrar apps del TV de forma accesible
 */
public class TVAppsAdapter extends ArrayAdapter<TVAppManager.TVAppInfo> {
    
    private Context context;
    private List<TVAppManager.TVAppInfo> apps;
    private ElderlyAccessibilityManager accessibilityManager;

    public TVAppsAdapter(Context context, List<TVAppManager.TVAppInfo> apps, 
                        ElderlyAccessibilityManager accessibilityManager) {
        super(context, 0, apps);
        this.context = context;
        this.apps = apps;
        this.accessibilityManager = accessibilityManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                .inflate(R.layout.item_app, parent, false);
        }

        TVAppManager.TVAppInfo app = apps.get(position);

        // Configurar vista
        TextView appNameText = convertView.findViewById(R.id.app_name);
        TextView appEmojiText = convertView.findViewById(R.id.app_emoji);

        // Texto grande para mayores
        float fontSize = accessibilityManager.getFontSizeInPixels();
        appNameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        appEmojiText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize + 4);

        appNameText.setText(app.name);
        appEmojiText.setText(app.emoji);

        // Colores de alto contraste
        if (accessibilityManager.isHighContrast()) {
            appNameText.setTextColor(Color.BLACK);
            convertView.setBackgroundColor(position % 2 == 0 ? 
                Color.rgb(240, 240, 240) : Color.WHITE);
        } else {
            appNameText.setTextColor(Color.BLACK);
            convertView.setBackgroundColor(Color.WHITE);
        }

        // Altura mínima para tocar fácilmente
        convertView.setMinimumHeight(dpToPx(64));

        return convertView;
    }

    private int dpToPx(int dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }
}
