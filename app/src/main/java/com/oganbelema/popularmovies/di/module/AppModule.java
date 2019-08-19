package com.oganbelema.popularmovies.di.module;

import android.app.Application;
import android.content.Context;

import androidx.paging.PagedList;

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
    @Singleton
    public MoviesDiffItemCallback provideMoviesDiffItemCallback(){
        return new MoviesDiffItemCallback();
    }

    @Provides
    public MovieAdapter provideMovieAdapter(MoviesDiffItemCallback moviesDiffItemCallback){
        return new MovieAdapter(moviesDiffItemCallback);
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

    @Provides
    @Singleton
    public PagedList.Config providePagedListConfig(){
        return (new PagedList.Config.Builder())
                .setPageSize(20)
                .setPrefetchDistance(10)
                .build();
    }
}
