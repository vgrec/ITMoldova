package com.itmoldova

import android.annotation.TargetApi
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.evernote.android.job.JobManager
import com.itmoldova.di.ApplicationComponent
import com.itmoldova.di.ApplicationModule
import com.itmoldova.di.DaggerApplicationComponent
import com.itmoldova.kotlinex.oreoAndAbove
import com.itmoldova.sync.ITMoldovaJobCreator
import com.itmoldova.sync.SyncJob
import javax.inject.Inject

class ITMoldova : Application() {

    companion object {
        lateinit var appComponent: ApplicationComponent
        val DEFAULT_CHANNEL_ID = "default_chanel_id"
    }

    @Inject
    lateinit var jobCreator: ITMoldovaJobCreator

    @Inject
    lateinit var appSettings: AppSettings

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        appComponent.inject(this)

        JobManager.create(this).addJobCreator(jobCreator)
        createDefaultNotificationChannel()

        if (appSettings.notificationsEnabled) {
            SyncJob.scheduleSync()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createDefaultNotificationChannel() {
        oreoAndAbove {
            val name = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val channel = NotificationChannel(DEFAULT_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = description

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
