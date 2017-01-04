package com.itmoldova.di;

import com.itmoldova.list.ArticlesPresenter;
import com.itmoldova.settings.SettingsFragment;
import com.itmoldova.sync.v14.BootReceiver;
import com.itmoldova.sync.v14.SyncArticlesServiceV14;

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

    void inject(SyncArticlesServiceV14 target);
}
