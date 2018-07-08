package com.itmoldova.kotlinex

import android.os.Build

/**
 * Calls the specified block of code only if the app's version
 * is equal or greater than the specified version.
 */
fun runOnVersion(version: Int, body: () -> Unit){
    if (Build.VERSION.SDK_INT >= version) {
        body()
    }
}
