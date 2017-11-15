package com.itmoldova.kotlinex

import android.os.Build

/**
 * Calls the specified block of code only if the app is running on
 * a device with Lollipop or above.
 */
fun lollipopAndAbove(body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        body()
    }
}
