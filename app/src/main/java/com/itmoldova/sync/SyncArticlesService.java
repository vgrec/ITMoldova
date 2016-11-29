package com.itmoldova.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * An {@link IntentService} that retrieves the latest rss feed
 * and fires a notification if new articles were published since
 * the last sync.
 */
public class SyncArticlesService extends IntentService {

    public SyncArticlesService() {
        super("SyncArticlesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("GREC_TEST", "Alarm fired!");
    }

}
