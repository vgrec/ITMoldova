package com.itmoldova.util;

import android.util.Log;

import com.itmoldova.BuildConfig;

/**
 * A wrapper logger that logs only in debug builds
 * and does not need a TAG parameter.
 */
public class Logs {
    private static final String TAG = "ITMoldova";
    private static boolean enabled = BuildConfig.DEBUG;

    public static void d(String text) {
        if (enabled) {
            Log.d(TAG, text);
        }
    }

    public static void e(String text, Throwable e) {
        if (enabled) {
            Log.e(TAG, text, e);
        }
    }
}
