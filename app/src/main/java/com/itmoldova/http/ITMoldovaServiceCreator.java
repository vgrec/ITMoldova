package com.itmoldova.http;

import com.itmoldova.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Creates a new instance of {@link ITMoldovaService}.
 * <p>
 * Author vgrec, on 10.09.16.
 */
public class ITMoldovaServiceCreator {
    public static ITMoldovaService createItMoldovaService() {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor.get()).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.IT_MOLDOVA_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(ITMoldovaService.class);
    }
}
