package com.itmoldova.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Helper class to check the internet connection.
 * <p>
 * (Main reason why this is not a utility method (public static .... ) is to
 * be able to mock the class during unit tests)
 * <p>
 * Author vgrec, on 10.09.16.
 */
public class NetworkConnectionManager {
    private Context context;

    public NetworkConnectionManager(Context context) {
        this.context = context;
    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
