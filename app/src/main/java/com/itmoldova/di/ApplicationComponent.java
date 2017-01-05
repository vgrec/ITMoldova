package com.itmoldova.di;

import com.itmoldova.list.ArticlesPresenter;
import com.itmoldova.settings.SettingsFragment;
import com.itmoldova.sync.SyncRunner;
import com.itmoldova.sync.v14.BootReceiver;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class
})
public interface ApplicationComponent {

    void inject(ArticlesPresenter target);

    void inject(BootReceiver target);

    void inject(SettingsFragment target);

    void inject(SyncRunner target);
}
