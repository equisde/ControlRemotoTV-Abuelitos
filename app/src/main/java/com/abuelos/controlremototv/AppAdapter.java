package com.abuelos.controlremototv;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.abuelos.controlremototv.databinding.ItemAppBinding;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {
    private final List<AppInfo> apps;
    private final OnAppClickListener listener;

    public interface OnAppClickListener {
        void onAppClick(AppInfo app);
    }

    public AppAdapter(List<AppInfo> apps, OnAppClickListener listener) {
        this.apps = apps;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppBinding binding = ItemAppBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new AppViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        AppInfo app = apps.get(position);
        holder.binding.appName.setText(app.name);
        holder.binding.appIcon.setImageDrawable(app.icon);
        holder.itemView.setOnClickListener(v -> listener.onAppClick(app));
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        ItemAppBinding binding;

        AppViewHolder(ItemAppBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
