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

    public static final int SYNC_INTERVAL_NEVER = -1;
    private static final String SYNC_INTERVAL = "SYNC_INTERVAL";
    private static final String LAST_PUB_DATE = "LAST_PUB_DATE";

    private SharedPreferences preferences;
    private Resources resources;

    public AppSettings(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        resources = context.getResources();
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

    public void setLastPubDate(long lastPubDate) {
        preferences.edit().putLong(LAST_PUB_DATE, lastPubDate).apply();
    }

    public long getLastPubDate() {
        return preferences.getLong(LAST_PUB_DATE, 0);
    }
}
