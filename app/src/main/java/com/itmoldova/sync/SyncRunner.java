package com.itmoldova.sync;

import com.itmoldova.AppSettings;
import com.itmoldova.ITMoldova;
import com.itmoldova.controller.NotificationController;
import com.itmoldova.http.ITMoldovaService;
import com.itmoldova.http.NetworkDetector;
import com.itmoldova.model.Item;
import com.itmoldova.model.Rss;
import com.itmoldova.util.Utils;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Executes an http request against "http://itmoldova.com/feed/?paged=1" and in case
 * new articles were published since the last sync, fires a notifications.
 */
public class SyncRunner {

    // Always query the first page, this will give the latest articles.
    private static final int PAGE_NUMBER = 1;

    private Subscription subscription;

    @Inject
    NotificationController notificationController;

    @Inject
    AppSettings appSettings;

    @Inject
    ITMoldovaService service;

    @Inject
    NetworkDetector networkDetector;

    public SyncRunner() {
        ITMoldova.getAppComponent().inject(this);
    }

    public void start() {
        if (!networkDetector.hasInternetConnection()) {
            return;
        }
        subscription = service
                .getDefaultRssFeed(PAGE_NUMBER)
                .subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess(Rss response) {
        if (response == null || response.getChannel() == null) {
            return;
        }

        List<Item> items = response.getChannel().getItemList();
        if (notificationController.shouldShowNotification(items)) {
            long newLastPubDate = Utils.pubDateToMillis(items.get(0).getPubDate());
            appSettings.setLastPubDate(newLastPubDate);
            notificationController.showNotification(items);
        }
    }

    private void onError(Throwable error) {
        // Ignore errors during sync
    }

    public void cancel() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
