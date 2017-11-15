package com.itmoldova

import android.app.Application
import com.evernote.android.job.JobManager
import com.itmoldova.di.ApplicationComponent
import com.itmoldova.di.ApplicationModule
import com.itmoldova.di.DaggerApplicationComponent
import com.itmoldova.sync.ITMoldovaJobCreator
import javax.inject.Inject

class ITMoldova : Application() {

    companion object {
        lateinit var appComponent: ApplicationComponent
    }

    @Inject
    lateinit var jobCreator: ITMoldovaJobCreator

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        appComponent.inject(this)

        JobManager.create(this).addJobCreator(jobCreator)
    }
}
