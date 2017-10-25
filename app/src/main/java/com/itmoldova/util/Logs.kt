package com.itmoldova.util

import android.util.Log

import com.itmoldova.BuildConfig

/**
 * A wrapper logger that logs only in debug builds
 * and does not need a TAG parameter.
 */
object Logs {
    private val TAG = "ITMoldova"
    private val enabled = BuildConfig.DEBUG

    fun d(text: String) {
        if (enabled) {
            Log.d(TAG, text)
        }
    }

    fun e(text: String, e: Throwable) {
        if (enabled) {
            Log.e(TAG, text, e)
        }
    }
}
