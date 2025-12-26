package com.abuelos.controlremototv;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.ArrayList;
import java.util.List;

public class AppManager {
    private final Context context;
    private final PackageManager packageManager;

    public AppManager(Context context) {
        this.context = context;
        this.packageManager = context.getPackageManager();
    }

    public List<AppInfo> getInstalledApps() {
        List<AppInfo> apps = new ArrayList<>();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(
                PackageManager.GET_META_DATA
        );

        for (ApplicationInfo appInfo : packages) {
            if (isSystemApp(appInfo)) continue;

            String appName = appInfo.loadLabel(packageManager).toString();
            String packageName = appInfo.packageName;

            apps.add(new AppInfo(appName, packageName, appInfo.loadIcon(packageManager)));
        }

        return apps;
    }

    private boolean isSystemApp(ApplicationInfo appInfo) {
        return (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public void launchApp(String packageName) {
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    public void sendKeyEvent(int keyCode) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, 
                new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, keyCode));
        context.sendBroadcast(intent);
    }
}
