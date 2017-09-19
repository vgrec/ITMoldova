package com.itmoldova.di

import com.itmoldova.list.ArticlesPresenter
import com.itmoldova.settings.SettingsFragment
import com.itmoldova.sync.v14.BootReceiver
import com.itmoldova.sync.v14.SyncArticlesServiceV14
import com.itmoldova.sync.v21.SyncArticlesServiceV21

import javax.inject.Singleton

import dagger.Component

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, NetworkModule::class))
interface ApplicationComponent {

    fun inject(target: ArticlesPresenter)

    fun inject(target: BootReceiver)

    fun inject(target: SettingsFragment)

    fun inject(target: SyncArticlesServiceV21)

    fun inject(target: SyncArticlesServiceV14)
}
