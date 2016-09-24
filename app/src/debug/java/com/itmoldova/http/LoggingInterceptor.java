package com.itmoldova.http;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Defines the {@link HttpLoggingInterceptor} for debug builds.
 */
public class LoggingInterceptor {

    public static HttpLoggingInterceptor get() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
