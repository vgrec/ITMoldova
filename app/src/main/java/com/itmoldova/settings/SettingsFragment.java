package com.itmoldova.settings;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.itmoldova.AppSettings;
import com.itmoldova.ITMoldova;
import com.itmoldova.R;
import com.itmoldova.sync.SyncScheduler;
import com.itmoldova.sync.SyncSchedulerFactory;

import javax.inject.Inject;

/**
 * Main UI for the settings screen.
 * <p>
 * author vgrec-home on 29.11.16.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Inject
    AppSettings appSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        ITMoldova.getAppComponent().inject(this);

        ListPreference notificationsList = (ListPreference) findPreference(getString(R.string.key_notifications));
        notificationsList.setOnPreferenceChangeListener(this);
        setPreferenceSummary(notificationsList, appSettings.getNotificationEntry());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        setPreferenceSummary(preference, newValue);
        if (getString(R.string.key_notifications).equals(preference.getKey())) {
            setSyncInterval(newValue);
        }
        return true;
    }

    private void setSyncInterval(Object newValue) {
        long interval = Long.valueOf(newValue.toString());
        appSettings.setSyncInterval(interval);
        SyncScheduler scheduler = SyncSchedulerFactory.getScheduler(getActivity());
        if (interval != AppSettings.SYNC_INTERVAL_NEVER) {
            scheduler.scheduleRepeatingSync(interval);
        } else {
            scheduler.cancel();
        }
    }

    private void setPreferenceSummary(Preference preference, Object newValue) {
        String summary = newValue.toString();
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(summary);
            CharSequence[] entries = listPreference.getEntries();
            if (prefIndex >= 0) {
                preference.setSummary(entries[prefIndex]);
            }
        } else {
            preference.setSummary(summary);
        }
    }
}
