package com.oganbelema.popularmovies.di.module;

import android.app.Application;
import android.content.Context;

import com.oganbelema.popularmovies.movie.ui.MovieAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return mApplication;
    }

    @Provides
    public MovieAdapter provideMovieAdapter(){
        return new MovieAdapter();
    }
}
