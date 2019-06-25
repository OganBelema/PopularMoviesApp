package com.oganbelema.popularmovies.di.component;

import com.oganbelema.popularmovies.di.module.AppModule;
import com.oganbelema.popularmovies.di.module.NetworkModule;
import com.oganbelema.popularmovies.movie.moviedetail.MovieDetailActivity;
import com.oganbelema.popularmovies.movie.movielist.MovieListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(MovieListActivity target);

    void inject(MovieDetailActivity target);

}
