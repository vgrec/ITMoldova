package com.itmoldova.settings

import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import com.itmoldova.AppSettings
import com.itmoldova.ITMoldova
import com.itmoldova.R
import com.itmoldova.sync.SyncSchedulerFactory
import javax.inject.Inject

/**
 * Main UI for the settings screen.
 *
 *
 * author vgrec-home on 29.11.16.
 */
class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

    @Inject
    lateinit var appSettings: AppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
        ITMoldova.appComponent.inject(this)

        val notificationsList = findPreference(getString(R.string.key_notifications)) as ListPreference
        notificationsList.onPreferenceChangeListener = this
        setPreferenceSummary(notificationsList, appSettings.getNotificationEntry())
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        setPreferenceSummary(preference, newValue)
        if (getString(R.string.key_notifications) == preference.key) {
            setSyncInterval(newValue)
        }
        return true
    }

    private fun setSyncInterval(newValue: Any) {
        val interval = java.lang.Long.valueOf(newValue.toString())!!
        appSettings.syncInterval = interval
        val scheduler = SyncSchedulerFactory.getScheduler(activity)
        if (interval != AppSettings.SYNC_INTERVAL_NEVER) {
            scheduler.scheduleRepeatingSync(interval)
        } else {
            scheduler.cancel()
        }
    }

    private fun setPreferenceSummary(preference: Preference, newValue: Any) {
        val summary = newValue.toString()
        if (preference is ListPreference) {
            val prefIndex = preference.findIndexOfValue(summary)
            val entries = preference.entries
            if (prefIndex >= 0) {
                preference.setSummary(entries[prefIndex])
            }
        } else {
            preference.summary = summary
        }
    }
}
