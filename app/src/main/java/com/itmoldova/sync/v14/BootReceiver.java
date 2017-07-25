package com.itmoldova.sync.v14;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itmoldova.AppSettings;
import com.itmoldova.ITMoldova;
import com.itmoldova.sync.SyncScheduler;
import com.itmoldova.sync.SyncSchedulerFactory;

import javax.inject.Inject;

/**
 * {@link BroadcastReceiver} that listens for {@link Intent.ACTION_BOOT_COMPLETED} action
 * and schedules the sync.
 */
public class BootReceiver extends BroadcastReceiver {

    @Inject
    AppSettings appSettings;

    @Override
    public void onReceive(Context context, Intent intent) {
        ITMoldova.getAppComponent().inject(this);
        long interval = appSettings.getSyncInterval();
        if (interval != AppSettings.SYNC_INTERVAL_NEVER) {
            SyncScheduler syncScheduler = SyncSchedulerFactory.getScheduler(context);
            syncScheduler.scheduleRepeatingSync(interval);
        }
    }
}
