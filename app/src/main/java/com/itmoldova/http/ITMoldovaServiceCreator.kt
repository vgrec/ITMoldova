package com.itmoldova.http

import android.content.Context
import com.itmoldova.Constants
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Creates a new instance of [ITMoldovaService].
 *
 *
 * Author vgrec, on 10.09.16.
 */
object ITMoldovaServiceCreator {
    fun createItMoldovaService(context: Context): ITMoldovaService {
        val cache = Cache(File(context.applicationContext.cacheDir, "itmdcache"), (10 * 1024 * 1024).toLong()) // 10 MB

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor.get())
                .addInterceptor(provideOfflineCacheInterceptor(context))
                .cache(cache)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.IT_MOLDOVA_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()

        return retrofit.create(ITMoldovaService::class.java)
    }

    private fun provideOfflineCacheInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!NetworkDetector(context).hasInternetConnection()) {
                val cacheControl = CacheControl.Builder()
                        .maxStale(15, TimeUnit.DAYS)
                        .build()

                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build()
            }
            chain.proceed(request)
        }
    }
}
