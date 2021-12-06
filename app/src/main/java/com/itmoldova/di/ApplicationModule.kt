package com.itmoldova.di

import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.itmoldova.AppSettings
import com.itmoldova.R
import com.itmoldova.db.AppDatabase
import com.itmoldova.repository.RssFeedRepository
import com.itmoldova.repository.RssFeedRepositoryImpl
import com.itmoldova.sync.ITMoldovaJobCreator
import com.itmoldova.sync.RssChecker
import com.itmoldova.util.HtmlParser
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApplicationModule.Bindings::class])
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

    @Provides
    fun provideHtmlParser(context: Context, appSettings: AppSettings): HtmlParser {
        // Update the application context theme in order to be able to obtain proper styled attributes in the HtmlParser.
        context.theme.applyStyle(if (appSettings.isDarkModeEnabled) R.style.AppTheme_Dark else R.style.AppTheme_Light,
            true)
        return HtmlParser(context)
    }

    @Module
    interface Bindings {
        @Binds
        fun bindRssFeedRepository(rss: RssFeedRepositoryImpl): RssFeedRepository
    }
}
