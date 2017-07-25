package com.itmoldova.sync.v14;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.itmoldova.sync.SyncScheduler;

import java.util.concurrent.TimeUnit;

/**
 * A wrapper around {@link AlarmManager} that provides the
 * ability to schedule and cancel the sync on pre Lollipop devices.
 */
public class SyncSchedulerV14 implements SyncScheduler {

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public SyncSchedulerV14(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SyncArticlesServiceV14.class);
        pendingIntent = PendingIntent.getService(context, 0, intent, 0);
    }

    @Override
    public void scheduleRepeatingSync(long interval) {
        long hours = TimeUnit.HOURS.toMillis(interval);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                hours,
                hours, pendingIntent);
    }

    @Override
    public void cancel() {
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
