package com.itmoldova;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

/**
 * A wrapper class around {@link android.content.SharedPreferences} that provides
 * convenient methods for manipulating user settings.
 */
public class AppSettings {

    public static final String SYNC_INTERVAL = "SYNC_INTERVAL";
    public static final int SYNC_INTERVAL_NEVER = -1;

    private static AppSettings instance;
    private SharedPreferences preferences;
    private Resources resources;

    public AppSettings(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        resources = context.getResources();
    }

    public static AppSettings getInstance(Context context) {
        if (instance == null) {
            instance = new AppSettings(context);
        }
        return instance;
    }

    public void setSyncInterval(long interval) {
        preferences.edit().putLong(SYNC_INTERVAL, interval).apply();
    }

    public long getSyncInterval() {
        return preferences.getLong(SYNC_INTERVAL, SYNC_INTERVAL_NEVER);
    }

    public String getNotificationEntry() {
        String key = resources.getString(R.string.key_notifications);
        String defaultValue = resources.getString(R.string.notification_value_never);
        return preferences.getString(key, defaultValue);
    }

}
