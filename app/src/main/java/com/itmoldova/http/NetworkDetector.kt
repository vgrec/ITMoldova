package com.itmoldova.http

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Helper class to check the internet connection.
 *
 *
 * (Main reason why this is not a utility method (public static .... ) is to
 * be able to mock the class during unit tests)
 *
 *
 * Author vgrec, on 10.09.16.
 */
class NetworkDetector(private val context: Context) {

    fun hasInternetConnection(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
