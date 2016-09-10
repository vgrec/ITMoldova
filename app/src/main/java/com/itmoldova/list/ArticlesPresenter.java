package com.itmoldova.list;

import com.itmoldova.http.ITMoldovaService;
import com.itmoldova.http.NetworkConnectionManager;
import com.itmoldova.model.Rss;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author vgrec, on 09.07.16.
 */
public class ArticlesPresenter implements ArticlesContract.Presenter {

    private ArticlesContract.View view;
    private ITMoldovaService apiService;
    private Subscription subscription;
    private NetworkConnectionManager connectionManager;

    public ArticlesPresenter(ITMoldovaService apiService, ArticlesContract.View view, NetworkConnectionManager connectionManager) {
        this.view = view;
        this.apiService = apiService;
        this.connectionManager = connectionManager;
    }

    @Override
    public void loadArticles() {
        if (!connectionManager.hasInternetConnection()) {
            view.showNoInternetConnection();
            return;
        }
        subscription = apiService.getRssFeed()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.setLoadingIndicator(true))
                .doOnTerminate(() -> view.setLoadingIndicator(false))
                .subscribe(
                        response -> processResponse(response),
                        error -> view.showError());
    }

    private void processResponse(Rss response) {
        if (response != null && response.getChannel() != null) {
            view.showArticles(response.getChannel().getItemList());
        } else {
            view.showError();
        }
    }

    @Override
    public void cancel() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
