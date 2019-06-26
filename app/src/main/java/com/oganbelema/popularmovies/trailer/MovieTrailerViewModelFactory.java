package com.oganbelema.popularmovies.trailer;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieTrailerViewModelFactory implements ViewModelProvider.Factory {

    private final MovieTrailerRepository mMovieTrailerRepository;

    private final MovieTrailerAdapter mMovieTrailerAdapter;

    @Inject
    public MovieTrailerViewModelFactory(MovieTrailerRepository mMovieTrailerRepository,
                                        MovieTrailerAdapter movieTrailerAdapter) {
        this.mMovieTrailerRepository = mMovieTrailerRepository;
        this.mMovieTrailerAdapter = movieTrailerAdapter;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieTrailerViewModel(mMovieTrailerRepository, mMovieTrailerAdapter);
    }
}
