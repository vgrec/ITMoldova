package com.itmoldova;

import android.app.Application;

import com.itmoldova.di.ApplicationComponent;
import com.itmoldova.di.ApplicationModule;
import com.itmoldova.di.DaggerApplicationComponent;

public class ITMoldova extends Application {

    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent getAppComponent() {
        return applicationComponent;
    }

}
