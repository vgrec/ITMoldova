package com.itmoldova.sync.v21;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.itmoldova.sync.SyncRunner;

/**
 * Implementation of {@link JobService} used on Lollipop and above
 * to retrieve the latest rss feed.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SyncArticlesServiceV21 extends JobService {

    private SyncRunner syncRunner;

    @Override
    public void onCreate() {
        super.onCreate();
        syncRunner = new SyncRunner();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        syncRunner.start();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        syncRunner.cancel();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        syncRunner.cancel();
    }
}
