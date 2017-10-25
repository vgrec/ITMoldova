package com.itmoldova.sync

import android.content.Context
import android.os.Build

import com.itmoldova.sync.v14.SyncSchedulerV14
import com.itmoldova.sync.v21.SyncSchedulerV21

/**
 * Provides the appropriate [SyncScheduler] depending of the target device.
 */
object SyncSchedulerFactory {
    fun getScheduler(context: Context): SyncScheduler {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SyncSchedulerV21(context)
        } else {
            SyncSchedulerV14(context)
        }
    }
}
