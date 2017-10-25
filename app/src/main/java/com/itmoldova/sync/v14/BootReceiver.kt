package com.itmoldova.sync.v14

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.itmoldova.AppSettings
import com.itmoldova.ITMoldova
import com.itmoldova.sync.SyncScheduler
import com.itmoldova.sync.SyncSchedulerFactory

import javax.inject.Inject

/**
 * [BroadcastReceiver] that listens for [Intent.ACTION_BOOT_COMPLETED] action
 * and schedules the sync.
 */
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var appSettings: AppSettings

    override fun onReceive(context: Context, intent: Intent) {
        ITMoldova.appComponent.inject(this)

        val interval = appSettings.syncInterval
        if (interval != AppSettings.SYNC_INTERVAL_NEVER) {
            val syncScheduler = SyncSchedulerFactory.getScheduler(context)
            syncScheduler.scheduleRepeatingSync(interval)
        }
    }
}
