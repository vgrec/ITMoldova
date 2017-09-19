package com.itmoldova.di

import android.content.Context

import com.itmoldova.Constants
import com.itmoldova.http.ITMoldovaService
import com.itmoldova.http.LoggingInterceptor
import com.itmoldova.http.NetworkDetector

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor.get()).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.IT_MOLDOVA_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build()
    }

    @Singleton
    @Provides
    fun provideITMoldovaService(retrofit: Retrofit): ITMoldovaService {
        return retrofit.create(ITMoldovaService::class.java)
    }

    @Singleton
    @Provides
    fun provideNetworkDetector(context: Context): NetworkDetector {
        return NetworkDetector(context)
    }

}
