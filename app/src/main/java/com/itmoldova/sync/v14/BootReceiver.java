package com.itmoldova.sync.v14;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itmoldova.AppSettings;
import com.itmoldova.sync.SyncScheduler;
import com.itmoldova.sync.SyncSchedulerFactory;

/**
 * {@link BroadcastReceiver} that listens for {@link Intent.ACTION_BOOT_COMPLETED} action
 * and schedules the sync.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long interval = AppSettings.getInstance(context).getSyncInterval();
        if (interval != AppSettings.SYNC_INTERVAL_NEVER) {
            SyncScheduler syncScheduler = SyncSchedulerFactory.getScheduler(context);
            syncScheduler.scheduleRepeatingSync(interval);
        }
    }
}
