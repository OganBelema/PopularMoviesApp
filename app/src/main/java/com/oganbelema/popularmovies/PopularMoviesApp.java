package com.oganbelema.popularmovies;

import android.app.Application;

import com.oganbelema.popularmovies.di.component.AppComponent;
import com.oganbelema.popularmovies.di.component.DaggerAppComponent;
import com.oganbelema.popularmovies.di.module.AppModule;

public class PopularMoviesApp extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }
}
