package com.itmoldova

import android.content.Context
import android.preference.PreferenceManager

/**
 * A wrapper class around {@link android.content.SharedPreferences} that provides
 * convenient methods for manipulating user settings.
 */
class AppSettings(context: Context) {

    companion object {
        val SYNC_INTERVAL_NEVER: Long = -1
        private val SYNC_INTERVAL = "SYNC_INTERVAL"
        private val LAST_PUB_DATE = "LAST_PUB_DATE"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val resources = context.resources

    fun getNotificationEntry(): String {
        val key = resources.getString(R.string.key_notifications)
        val defaultValue = resources.getString(R.string.notification_value_never)
        return preferences.getString(key, defaultValue)
    }

    var lastPubDate: Long
        get() = preferences.getLong(LAST_PUB_DATE, 0)
        set(value) = preferences.edit().putLong(LAST_PUB_DATE, value).apply()

    var syncInterval: Long
        get() = preferences.getLong(SYNC_INTERVAL, SYNC_INTERVAL_NEVER)
        set(value) = preferences.edit().putLong(SYNC_INTERVAL, value).apply()
}