package com.itmoldova.sync.v21;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.itmoldova.ITMoldova;
import com.itmoldova.sync.SyncRunner;

import javax.inject.Inject;

/**
 * Implementation of {@link JobService} used on Lollipop and above
 * to retrieve the latest rss feed.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SyncArticlesServiceV21 extends JobService {

    @Inject
    SyncRunner syncRunner;

    @Override
    public void onCreate() {
        super.onCreate();
        ITMoldova.getAppComponent().inject(this);
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
