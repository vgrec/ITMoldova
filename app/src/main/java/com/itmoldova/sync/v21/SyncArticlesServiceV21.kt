package com.itmoldova.sync.v21

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.support.annotation.RequiresApi

import com.itmoldova.ITMoldova
import com.itmoldova.sync.SyncRunner

import javax.inject.Inject

import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Implementation of [JobService] used on Lollipop and above
 * to retrieve the latest rss feed.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class SyncArticlesServiceV21 : JobService() {

    @Inject
    lateinit var syncRunner: SyncRunner

    override fun onCreate() {
        super.onCreate()
        ITMoldova.appComponent.inject(this)
    }

    override fun onStartJob(params: JobParameters): Boolean {
        syncRunner.start(
                Schedulers.newThread(),
                AndroidSchedulers.mainThread(),
                { success -> jobFinished(params, !success) })
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        syncRunner.cancel()
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        syncRunner.cancel()
    }
}
