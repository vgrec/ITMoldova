package com.itmoldova.http;

import com.itmoldova.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Utility class related to http operations.
 * <p>
 * Author vgrec, on 10.09.16.
 */
public class ITMoldovaServiceCreator {
    public static ITMoldovaService createItMoldovaService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.IT_MOLDOVA_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(ITMoldovaService.class);
    }
}
