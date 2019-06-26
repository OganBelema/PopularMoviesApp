package com.oganbelema.popularmovies.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.popularmovies.PopularMoviesApp;
import com.oganbelema.popularmovies.movie.repository.MovieRepository;

import javax.inject.Inject;

public class FavoriteService extends Service {

    public static final String MOVIE_KEY = "favorite_movie";

    public static final String FAVORITE_ACTION = "favorite";

    public static final String UN_FAVORITE_ACTION = "unFavorite";

    @Inject
    MovieRepository mMovieRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        ((PopularMoviesApp) getApplication()).getAppComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Movie favoriteMovie = intent.getParcelableExtra(MOVIE_KEY);

        String action = intent.getAction();

        if (action != null){
            if (action.equals(FAVORITE_ACTION)){
                mMovieRepository.addFavoriteMovie(favoriteMovie);
            } else if(action.equals(UN_FAVORITE_ACTION)){
                mMovieRepository.removeFavoriteMovie(favoriteMovie);
            } else {
                throw new UnsupportedOperationException("Unsupported action");
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
