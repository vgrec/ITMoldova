package com.itmoldova.sync.v21

import android.annotation.TargetApi
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build

import com.itmoldova.sync.SyncScheduler

import java.util.concurrent.TimeUnit

/**
 * A wrapper around [JobScheduler] that provides the ability
 * to schedule and cancel a sync on Lollipop and above devices.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class SyncSchedulerV21(context: Context) : SyncScheduler {

    private val serviceComponent: ComponentName
    private val jobScheduler: JobScheduler

    init {
        this.serviceComponent = ComponentName(context, SyncArticlesServiceV21::class.java)
        jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    }

    override fun scheduleRepeatingSync(interval: Long) {
        val builder = JobInfo.Builder(JOB_ID, serviceComponent)
        builder.setPeriodic(TimeUnit.HOURS.toMillis(interval))
        builder.setPersisted(true)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        jobScheduler.schedule(builder.build())
    }

    override fun cancel() {
        jobScheduler.cancelAll()
    }

    companion object {

        val JOB_ID = 12390
    }
}
