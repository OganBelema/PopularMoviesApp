package com.oganbelema.popularmovies.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.oganbelema.network.model.trailer.Trailer;
import com.oganbelema.popularmovies.PopularMoviesApp;
import com.oganbelema.popularmovies.trailer.MovieTrailerRepository;

import java.util.List;

import javax.inject.Inject;

public class TrailerService extends Service {

    public static final String TRAILERS_KEY = "trailers";

    @Inject
    MovieTrailerRepository mMovieTrailerRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        ((PopularMoviesApp) getApplication()).getAppComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<Trailer> trailers = intent.getParcelableArrayListExtra(TRAILERS_KEY);
        mMovieTrailerRepository.saveMovieTrailers(trailers);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
