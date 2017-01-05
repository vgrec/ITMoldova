package com.itmoldova.sync.v14;

import android.app.IntentService;
import android.content.Intent;

import com.itmoldova.ITMoldova;
import com.itmoldova.sync.SyncRunner;

import javax.inject.Inject;

/**
 * Implementation of {@link IntentService} used on Pre-Lollipop devices
 * to retrieve the latest rss feed.
 */
public class SyncArticlesServiceV14 extends IntentService {

    public SyncArticlesServiceV14() {
        super("SyncArticlesServiceV14");
    }

    @Inject
    SyncRunner syncRunner;

    @Override
    public void onCreate() {
        super.onCreate();
        ITMoldova.getAppComponent().inject(this);
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
