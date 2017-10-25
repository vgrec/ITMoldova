package com.itmoldova.sync.v14


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import com.itmoldova.sync.SyncScheduler

import java.util.concurrent.TimeUnit

/**
 * A wrapper around [AlarmManager] that provides the
 * ability to schedule and cancel the sync on pre Lollipop devices.
 */
class SyncSchedulerV14(context: Context) : SyncScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val pendingIntent: PendingIntent

    init {
        val intent = Intent(context, SyncArticlesServiceV14::class.java)
        pendingIntent = PendingIntent.getService(context, 0, intent, 0)
    }

    override fun scheduleRepeatingSync(interval: Long) {
        val hours = TimeUnit.HOURS.toMillis(interval)
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                hours,
                hours, pendingIntent)
    }

    override fun cancel() {
        alarmManager.cancel(pendingIntent)
    }
}
