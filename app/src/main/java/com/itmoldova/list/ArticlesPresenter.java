package com.itmoldova.list;

import android.support.annotation.NonNull;
import android.util.Log;

import com.itmoldova.http.ArticlesHttpController;
import com.itmoldova.http.SimpleSubscriber;
import com.itmoldova.http.test.GitHubService;
import com.itmoldova.model.Channel;
import com.itmoldova.model.Item;
import com.itmoldova.model.Rss;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Author vgrec, on 09.07.16.
 */
public class ArticlesPresenter implements ArticlesContract.Presenter {

    private ArticlesContract.View view;
    private ArticlesHttpController httpController;

    public ArticlesPresenter(ArticlesHttpController httpController, ArticlesContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.httpController = httpController;
    }

    @Override
    public void loadArticles() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://itmoldova.com")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);
        service.getArticles()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(setLoadingAction(true))
                .doOnTerminate(setLoadingAction(false))
                .subscribe(new SimpleSubscriber<Rss>() {
                    @Override
                    public void onNext(Rss response) {
                        processResponse(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("GREC", e.getMessage());
                    }
                });

    }

    private void processResponse(Rss response) {
        if (response != null) {
            Channel channel = response.getChannel();
            if (channel != null) {
                List<Item> items = channel.getItemList();
                for (Item item : items) {
                    Log.d("GREC", item.toString());
                }
            } else {
                Log.d("GREC", "Channel is null");
            }


        } else {
            Log.d("GREC", "Response is null");
        }
    }

    @NonNull
    private Action0 setLoadingAction(final boolean loading) {
        return new Action0() {
            @Override
            public void call() {
                view.setLoadingIndicator(loading);
            }
        };
    }

    @Override
    public void start() {
        loadArticles();
    }
}
