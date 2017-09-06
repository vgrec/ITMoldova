package com.itmoldova.http;

import android.content.Context;

import com.itmoldova.Constants;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Creates a new instance of {@link ITMoldovaService}.
 * <p>
 * Author vgrec, on 10.09.16.
 */
public class ITMoldovaServiceCreator {
    public static ITMoldovaService createItMoldovaService(Context context) {
        Cache cache = new Cache(new File(context.getApplicationContext().getCacheDir(), "itmdcache"), 10 * 1024 * 1024); // 10 MB

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor.get())
                .addInterceptor(provideOfflineCacheInterceptor(context))
                .cache(cache)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.INSTANCE.getIT_MOLDOVA_BASE_URL())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(ITMoldovaService.class);
    }

    private static Interceptor provideOfflineCacheInterceptor(Context context) {
        return chain -> {
            Request request = chain.request();
            if (!new NetworkDetector(context).hasInternetConnection()) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(15, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }
            return chain.proceed(request);
        };
    }
}
