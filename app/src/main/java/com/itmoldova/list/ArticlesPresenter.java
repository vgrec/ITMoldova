package com.itmoldova.list;

import com.itmoldova.http.HttpRequestListener;
import com.itmoldova.http.RssFeedLoader;
import com.itmoldova.model.Rss;

/**
 * Author vgrec, on 09.07.16.
 */
public class ArticlesPresenter implements ArticlesContract.Presenter {

    private ArticlesContract.View view;
    private RssFeedLoader rssFeedLoader;

    public ArticlesPresenter(RssFeedLoader rssFeedLoader, ArticlesContract.View view) {
        this.view = view;
        this.rssFeedLoader = rssFeedLoader;
        this.view.setPresenter(this);
    }

    @Override
    public void loadArticles() {
        if (!rssFeedLoader.hasInternetConnection()) {
            view.showNoInternetConnection();
            return;
        }

        rssFeedLoader.getRssFeed(new HttpRequestListener() {
            @Override
            public void onStart() {
                view.setLoadingIndicator(true);
            }

            @Override
            public void onTerminate() {
                view.setLoadingIndicator(false);
            }

            @Override
            public void onSuccess(Rss response) {
                processResponse(response);
            }

            @Override
            public void onError(Throwable e) {
                view.showError();
            }
        });
    }

    private void processResponse(Rss response) {
        if (response != null && response.getChannel() != null) {
            view.showArticles(response.getChannel().getItemList());
        } else {
            view.showError();
        }
    }

    @Override
    public void start() {
        loadArticles();
    }
}
