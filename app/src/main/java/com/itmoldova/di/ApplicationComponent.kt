package com.itmoldova.di

import com.itmoldova.ITMoldova
import com.itmoldova.list.ArticlesPresenter
import com.itmoldova.settings.SettingsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, NetworkModule::class))
interface ApplicationComponent {

    fun inject(target: ITMoldova)

    fun inject(target: ArticlesPresenter)

    fun inject(target: SettingsFragment)
}
