package com.itmoldova.sync.v21;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Implementation of {@link JobService} that retrieves the latest rss feed
 * and fires a notification if new articles were published since
 * the last sync.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SyncArticlesServiceV21 extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("GREC_JOB", "Job started");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("GREC_JOB", "Job stopped");
        return false;
    }
}
