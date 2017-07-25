package com.itmoldova.sync;

import android.content.Context;
import android.os.Build;

import com.itmoldova.sync.v14.SyncSchedulerV14;
import com.itmoldova.sync.v21.SyncSchedulerV21;

/**
 * Provides the appropriate {@link SyncScheduler} depending of the target device.
 */
public class SyncSchedulerFactory {
    public static SyncScheduler getScheduler(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new SyncSchedulerV21(context);
        } else {
            return new SyncSchedulerV14(context);
        }
    }
}
