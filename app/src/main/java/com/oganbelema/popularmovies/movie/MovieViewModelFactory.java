package com.oganbelema.popularmovies.movie;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.oganbelema.popularmovies.movie.MovieRepository;
import com.oganbelema.popularmovies.movie.MovieViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieViewModelFactory implements ViewModelProvider.Factory {

    private final MovieRepository mMovieRepository;

    @Inject
    public MovieViewModelFactory(MovieRepository movieRepository) {
        mMovieRepository = movieRepository;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieViewModel(mMovieRepository);
    }
}
