package com.itmoldova.sync

import android.util.Log
import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SyncJob(private val rssChecker: RssChecker) : Job() {

    companion object {
        val TAG = "sync_job_tag"

        fun schedulePeriodicSync(interval: Long) {
            JobRequest.Builder(TAG)
                    .setPeriodic(TimeUnit.HOURS.toMillis(interval))
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .build()
                    .schedule()
        }

        fun cancelSync() {
            JobManager.instance().cancelAll()
        }
    }

    override fun onRunJob(params: Params?): Result {
        rssChecker.start(Schedulers.newThread(), AndroidSchedulers.mainThread())
        Log.d("GREC", "Job run")
        return Result.SUCCESS
    }
}