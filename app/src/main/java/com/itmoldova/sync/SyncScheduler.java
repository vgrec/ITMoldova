package com.itmoldova.sync;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

/**
 * A very thin wrapper around {@link AlarmManager} that provides the
 * ability to schedule and cancel the sync.
 */
public class SyncScheduler {

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public SyncScheduler(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SyncArticlesService.class);
        pendingIntent = PendingIntent.getService(context, 0, intent, 0);
    }

    public void scheduleRepeatingSync(long interval) {
        long hours = TimeUnit.HOURS.toMillis(interval);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                hours,
                hours, pendingIntent);
    }

    public void cancel() {
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
