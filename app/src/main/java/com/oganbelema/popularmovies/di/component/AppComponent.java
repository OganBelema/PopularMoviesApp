package com.oganbelema.popularmovies.di.component;

import com.oganbelema.popularmovies.di.module.AppModule;
import com.oganbelema.popularmovies.di.module.DatabaseModule;
import com.oganbelema.popularmovies.di.module.NetworkModule;
import com.oganbelema.popularmovies.movie.ui.MovieDetailActivity;
import com.oganbelema.popularmovies.movie.ui.MovieListActivity;
import com.oganbelema.popularmovies.service.FavoriteService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, DatabaseModule.class})
public interface AppComponent {

    void inject(MovieListActivity target);

    void inject(MovieDetailActivity target);

    void inject(FavoriteService target);

}
