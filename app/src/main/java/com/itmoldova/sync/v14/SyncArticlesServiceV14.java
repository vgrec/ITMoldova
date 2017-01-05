package com.itmoldova.sync.v14;

import android.app.IntentService;
import android.content.Intent;

import com.itmoldova.AppSettings;
import com.itmoldova.ITMoldova;
import com.itmoldova.controller.NotificationController;
import com.itmoldova.http.ITMoldovaService;
import com.itmoldova.http.NetworkDetector;
import com.itmoldova.model.Item;
import com.itmoldova.model.Rss;
import com.itmoldova.util.Logs;
import com.itmoldova.util.Utils;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * An {@link IntentService} that retrieves the latest rss feed
 * and fires a notification if new articles were published since
 * the last sync.
 */
public class SyncArticlesServiceV14 extends IntentService {

    // Always query the first page, this will give the latest articles.
    public static final int PAGE_NUMBER = 1;

    private Subscription subscription;

    @Inject
    NotificationController notificationController;

    @Inject
    AppSettings appSettings;

    @Inject
    ITMoldovaService service;

    @Inject
    NetworkDetector networkDetector;

    public SyncArticlesServiceV14() {
        super("SyncArticlesServiceV14");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ITMoldova.getAppComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!networkDetector.hasInternetConnection()) {
            Logs.d("No Connection. Abort sync.");
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

        if (notificationController.shouldShowNotification(response.getChannel().getItemList())) {
            List<Item> items = response.getChannel().getItemList();
            long newLastPubDate = Utils.pubDateToMillis(items.get(0).getPubDate());
            appSettings.setLastPubDate(newLastPubDate);
            notificationController.showNotification(items);
        }
    }

    private void onError(Throwable error) {
        // Ignore errors during sync
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
