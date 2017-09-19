package com.itmoldova.di

import android.app.NotificationManager
import android.content.Context

import com.itmoldova.AppSettings

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideAppSettings(context: Context): AppSettings {
        return AppSettings(context)
    }

    @Provides
    fun provideNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}
