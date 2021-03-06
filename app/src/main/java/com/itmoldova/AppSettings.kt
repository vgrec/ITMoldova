package com.itmoldova

import android.content.Context
import android.preference.PreferenceManager

/**
 * A wrapper class around {@link android.content.SharedPreferences} that provides
 * convenient methods for manipulating user settings.
 */
class AppSettings(context: Context) {

    companion object {
        private const val NOTIFICATIONS_ENABLED = "NOTIFICATIONS_ENABLED"
        private const val LAST_PUB_DATE = "LAST_PUB_DATE"
        private const val DARK_MODE = "DARK_MODE"
        private const val NOTIFICATIONS_SETUP = "NOTIFICATIONS_SETUP"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var lastPubDate: Long
        get() = preferences.getLong(LAST_PUB_DATE, 0)
        set(value) = preferences.edit().putLong(LAST_PUB_DATE, value).apply()

    var areNotificationsEnabled: Boolean
        get() = preferences.getBoolean(NOTIFICATIONS_ENABLED, true)
        set(value) = preferences.edit().putBoolean(NOTIFICATIONS_ENABLED, value).apply()

    var areNotificationsFirstTimeConfigured: Boolean
        get() = preferences.getBoolean(NOTIFICATIONS_SETUP, false)
        set(value) = preferences.edit().putBoolean(NOTIFICATIONS_SETUP, value).apply()

    var isDarkModeEnabled: Boolean
        get() = preferences.getBoolean(DARK_MODE, true)
        set(value) = preferences.edit().putBoolean(DARK_MODE, value).apply()
}