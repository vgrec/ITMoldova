package com.itmoldova.sync.v21;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.itmoldova.sync.SyncScheduler;

import java.util.concurrent.TimeUnit;

/**
 * A wrapper around {@link JobScheduler} that provides the ability
 * to schedule and cancel a sync on Lollipop and above devices.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SyncSchedulerV21 implements SyncScheduler {

    public static final int JOB_ID = 12390;

    private ComponentName serviceComponent;
    private JobScheduler jobScheduler;

    public SyncSchedulerV21(Context context) {
        this.serviceComponent = new ComponentName(context, SyncArticlesServiceV21.class);
        jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    @Override
    public void scheduleRepeatingSync(long interval) {
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceComponent);
        builder.setPeriodic(TimeUnit.HOURS.toMillis(interval));
        builder.setPersisted(true);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public void cancel() {
        jobScheduler.cancelAll();
    }
}
