package com.itmoldova

import android.app.Application

import com.itmoldova.di.ApplicationComponent
import com.itmoldova.di.ApplicationModule
import com.itmoldova.di.DaggerApplicationComponent

class ITMoldova : Application() {

    companion object {
        lateinit var appComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}
