package com.itmoldova.list;

import com.itmoldova.AppSettings;
import com.itmoldova.ITMoldova;
import com.itmoldova.http.ITMoldovaService;
import com.itmoldova.http.NetworkDetector;
import com.itmoldova.model.Category;
import com.itmoldova.model.Item;
import com.itmoldova.model.Rss;
import com.itmoldova.util.Utils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ArticlesPresenter implements ArticlesContract.Presenter {

    private ArticlesContract.View view;
    private ITMoldovaService apiService;
    private Subscription subscription;
    private NetworkDetector connectionManager;
    private Category category;

    @Inject
    AppSettings appSettings;

    public ArticlesPresenter(ITMoldovaService apiService, ArticlesContract.View view, NetworkDetector connectionManager) {
        this.view = view;
        this.apiService = apiService;
        this.connectionManager = connectionManager;
        ITMoldova.getAppComponent().inject(this);
    }

    @Override
    public void loadArticles(Category category, int page) {
        loadRssFeed(category, page, false);
    }

    @Override
    public void refreshArticles(Category category) {
        loadRssFeed(category, 0, true);
    }

    private void loadRssFeed(Category category, int page, boolean clearDataSet) {
        if (!connectionManager.hasInternetConnection()) {
            view.showNoInternetConnection();
            return;
        }
        this.category = category;
        subscription = getObservableByCategory(category, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.setLoadingIndicator(true))
                .doOnTerminate(() -> view.setLoadingIndicator(false))
                .subscribe(
                        rss -> processResponse(rss, clearDataSet),
                        error -> view.showError());
    }

    private Observable<Rss> getObservableByCategory(Category category, int page) {
        if (category == Category.HOME) {
            return apiService.getDefaultRssFeed(page);
        } else {
            return apiService.getRssFeedByCategory(category.getCategoryName(), page);
        }
    }

    private void processResponse(Rss response, boolean clearDataSet) {
        if (response != null && response.getChannel() != null) {
            List<Item> items = response.getChannel().getItemList();
            updateLastPubDate(items);
            view.showArticles(items, clearDataSet);
        } else {
            view.showError();
        }
    }

    private void updateLastPubDate(List<Item> items) {
        // Update last pub date only for articles that belong to HOME category
        if (category == Category.HOME) {
            long newLastPubDate = Utils.pubDateToMillis(items.get(0).getPubDate());
            appSettings.setLastPubDate(newLastPubDate);
        }
    }

    @Override
    public void cancel() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
