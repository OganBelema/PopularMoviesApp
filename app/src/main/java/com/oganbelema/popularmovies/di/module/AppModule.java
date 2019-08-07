package com.oganbelema.popularmovies.di.module;

import android.app.Application;
import android.content.Context;

import com.oganbelema.popularmovies.movie.MovieAdapter;
import com.oganbelema.popularmovies.movie.MoviesDiffCallback;
import com.oganbelema.popularmovies.movie.MoviesDiffItemCallback;
import com.oganbelema.popularmovies.movie.PagedMovieAdapter;
import com.oganbelema.popularmovies.reviews.MovieReviewAdapter;
import com.oganbelema.popularmovies.trailer.MovieTrailerAdapter;

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

    @Provides
    public PagedMovieAdapter providePagedMovieAdapter(){
        return new PagedMovieAdapter(new MoviesDiffItemCallback());
    }

    @Provides
    public MovieReviewAdapter provideMovieReviewAdapter(){
        return new MovieReviewAdapter();
    }

    @Provides
    public MovieTrailerAdapter provideMovieTrailerAdapter(){
        return new MovieTrailerAdapter();
    }
}
