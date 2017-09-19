package com.itmoldova.sync.v21;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.itmoldova.ITMoldova;
import com.itmoldova.sync.SyncRunner;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        ITMoldova.Companion.getAppComponent().inject(this);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        syncRunner.start(
                Schedulers.newThread(),
                AndroidSchedulers.mainThread(),
                success -> jobFinished(params, !success));
        return true;
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
