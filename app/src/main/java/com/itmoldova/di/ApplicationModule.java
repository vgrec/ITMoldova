package com.itmoldova.di;

import android.app.NotificationManager;
import android.content.Context;

import com.itmoldova.AppSettings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public AppSettings provideAppSettings(Context context) {
        return new AppSettings(context);
    }

    @Provides
    public NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
