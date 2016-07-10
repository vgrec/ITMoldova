package com.itmoldova.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.itmoldova.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author vgrec, on 10.07.16.
 */
public class RssFeedLoader {

    private Context context;

    public RssFeedLoader(Context context) {
        this.context = context;
    }

    public void getRssFeed(final HttpRequestListener httpRequestListener) {
        ITMoldovaService service = createItMoldovaService();
        service.getRssFeed()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(httpRequestListener::onStart)
                .doOnTerminate(httpRequestListener::onTerminate)
                .subscribe(
                        response -> httpRequestListener.onSuccess(response),
                        error -> httpRequestListener.onError(error));
    }

    private ITMoldovaService createItMoldovaService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.IT_MOLDOVA_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(ITMoldovaService.class);
    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
