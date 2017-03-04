package com.itmoldova.sync;

import com.itmoldova.AppSettings;
import com.itmoldova.controller.NotificationController;
import com.itmoldova.http.ITMoldovaService;
import com.itmoldova.http.NetworkDetector;
import com.itmoldova.model.Item;
import com.itmoldova.model.Rss;
import com.itmoldova.util.Logs;
import com.itmoldova.util.Utils;

import java.util.List;

import javax.inject.Inject;

import rx.Scheduler;
import rx.Subscription;

/**
 * Executes an http request against "http://itmoldova.com/feed/?paged=1" and in case
 * new articles were published since the last sync, fires a notifications.
 */
public class SyncRunner {

    // Always query the first page, this will give the latest articles.
    private static final int PAGE_NUMBER = 1;

    private Subscription subscription;

    private NotificationController notificationController;
    private AppSettings appSettings;
    private ITMoldovaService service;
    private NetworkDetector networkDetector;

    @Inject
    public SyncRunner(NotificationController notificationController,
                      AppSettings appSettings,
                      ITMoldovaService service,
                      NetworkDetector networkDetector) {
        this.notificationController = notificationController;
        this.appSettings = appSettings;
        this.service = service;
        this.networkDetector = networkDetector;
    }

    public void start(Scheduler subscribeOnScheduler, Scheduler observeOnScheduler) {
        start(subscribeOnScheduler, observeOnScheduler, null);
    }

    public void start(Scheduler subscribeOnScheduler, Scheduler observeOnScheduler, SyncFinishedListener listener) {
        if (!networkDetector.hasInternetConnection()) {
            return;
        }
        subscription = service
                .getDefaultRssFeed(PAGE_NUMBER)
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(
                        rss -> onSuccess(rss, listener),
                        error -> onError(error, listener)
                );
    }

    private void onSuccess(Rss response, SyncFinishedListener listener) {
        if (listener != null) {
            listener.onSyncFinished();
        }

        if (response == null || response.getChannel() == null) {
            return;
        }

        List<Item> items = response.getChannel().getItemList();
        if (notificationController.shouldShowNotification(items)) {
            notificationController.showNotification(items);
            long newLastPubDate = Utils.pubDateToMillis(items.get(0).getPubDate());
            appSettings.setLastPubDate(newLastPubDate);
        }
    }

    private void onError(Throwable error, SyncFinishedListener listener) {
        if (listener != null) {
            listener.onSyncFinished();
        }

        // Ignore errors during sync
        Logs.e("Error during sync", error);
    }

    public void cancel() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
