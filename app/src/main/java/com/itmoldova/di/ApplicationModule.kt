package com.itmoldova.di

import android.app.NotificationManager
import android.arch.persistence.room.Room
import android.content.Context

import com.itmoldova.AppSettings
import com.itmoldova.db.AppDatabase
import com.itmoldova.sync.ITMoldovaJobCreator
import com.itmoldova.sync.RssChecker

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

    @Provides
    fun provideJobCreator(rssChecker: RssChecker): ITMoldovaJobCreator {
        return ITMoldovaJobCreator(rssChecker)
    }

    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return Room
                .databaseBuilder(context, AppDatabase::class.java, "articles")
                .build()
    }
}
