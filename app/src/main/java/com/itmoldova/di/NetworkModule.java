package com.itmoldova.di;

import android.content.Context;

import com.itmoldova.Constants;
import com.itmoldova.http.ITMoldovaService;
import com.itmoldova.http.LoggingInterceptor;
import com.itmoldova.http.NetworkDetector;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@Module
public class NetworkModule {

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor.get()).build();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.IT_MOLDOVA_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();
    }

    @Singleton
    @Provides
    public ITMoldovaService provideITMoldovaService(Retrofit retrofit) {
        return retrofit.create(ITMoldovaService.class);
    }

    @Singleton
    @Provides
    public NetworkDetector provideNetworkDetector(Context context) {
        return new NetworkDetector(context);
    }

}
