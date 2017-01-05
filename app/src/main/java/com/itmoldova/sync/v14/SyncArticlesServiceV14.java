package com.itmoldova.sync.v14;

import android.app.IntentService;
import android.content.Intent;

import com.itmoldova.sync.SyncRunner;

/**
 * Implementation of {@link IntentService} used on Pre-Lollipop devices
 * to retrieve the latest rss feed.
 */
public class SyncArticlesServiceV14 extends IntentService {

    public SyncArticlesServiceV14() {
        super("SyncArticlesServiceV14");
    }

    private SyncRunner syncRunner;

    @Override
    public void onCreate() {
        super.onCreate();
        syncRunner = new SyncRunner();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        syncRunner.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        syncRunner.cancel();
    }
}
