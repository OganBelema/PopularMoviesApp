package com.oganbelema.popularmovies.movie.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.oganbelema.popularmovies.movie.repository.MovieRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieDetailViewModelFactory implements ViewModelProvider.Factory {

    private final MovieRepository mMovieRepository;

    @Inject
    public MovieDetailViewModelFactory(MovieRepository mMovieRepository) {
        this.mMovieRepository = mMovieRepository;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailViewModel(mMovieRepository);
    }
}
