package com.itmoldova.sync

import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SyncJob(private val rssChecker: RssChecker) : Job() {

    companion object {
        val TAG = "sync_job_tag"

        fun scheduleSync() {
            JobRequest.Builder(TAG)
                    .setPeriodic(TimeUnit.HOURS.toMillis(2))
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
        return Result.SUCCESS
    }
}