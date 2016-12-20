package com.itmoldova.sync.v14;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.itmoldova.AppSettings;
import com.itmoldova.R;
import com.itmoldova.http.ITMoldovaService;
import com.itmoldova.http.ITMoldovaServiceCreator;
import com.itmoldova.http.NetworkConnectionManager;
import com.itmoldova.list.MainActivity;
import com.itmoldova.model.Item;
import com.itmoldova.model.Rss;
import com.itmoldova.util.Logs;
import com.itmoldova.util.Utils;

import java.util.List;

import rx.Subscription;

/**
 * An {@link IntentService} that retrieves the latest rss feed
 * and fires a notification if new articles were published since
 * the last sync.
 */
public class SyncArticlesServiceV14 extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NetworkConnectionManager connectionManager;
    private Subscription subscription;
    private ITMoldovaService service;

    public SyncArticlesServiceV14() {
        super("SyncArticlesServiceV14");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connectionManager = new NetworkConnectionManager(this);
        service = ITMoldovaServiceCreator.createItMoldovaService();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!connectionManager.hasInternetConnection()) {
            Logs.d("No Connection. Abort sync.");
            return;
        }

        Logs.d("Alarm fired");

        subscription = service
                .getDefaultRssFeed(0)
                .subscribe(
                        rss -> processResponse(rss),
                        error -> handleError(error)
                );
    }

    private void processResponse(Rss response) {
        if (response != null && response.getChannel() != null) {
            List<Item> items = response.getChannel().getItemList();
            int newArticles = 0;
            long lastPubDate = AppSettings.getInstance(this).getLastPubDate();
            for (Item item : items) {
                long date = Utils.pubDateToMillis(item.getPubDate());
                if (date > lastPubDate) {
                    newArticles++;
                }
            }

            if (newArticles > 0) {
                showNotification(newArticles);
            }
            long newLastPubDate = Utils.pubDateToMillis(items.get(0).getPubDate());
            AppSettings.getInstance(this).setLastPubDate(newLastPubDate);
        }
    }

    private void handleError(Throwable error) {
        Logs.d("Error while syncing");
    }

    // Show big notification

    private void showNotification(int newArticles) {
        Logs.d(newArticles + " new articles published");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_gallery)
                        .setContentTitle("Xiami pregateste un nou produs, se presupune o masina")
                        .setContentText("Compania chineza Xiaomi pregateste lansarea pe piata unor dispozitive tare interesante.");

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
