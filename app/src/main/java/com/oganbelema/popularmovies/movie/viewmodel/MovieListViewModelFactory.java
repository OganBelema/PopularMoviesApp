package com.oganbelema.popularmovies.movie.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.oganbelema.popularmovies.movie.repository.MovieRepository;
import com.oganbelema.popularmovies.movie.ui.MovieAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieListViewModelFactory implements ViewModelProvider.Factory {

    private final MovieRepository mMovieRepository;

    private final MovieAdapter mMovieAdapter;

    @Inject
    public MovieListViewModelFactory(MovieRepository movieRepository, MovieAdapter movieAdapter) {
        mMovieRepository = movieRepository;
        mMovieAdapter = movieAdapter;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieListViewModel(mMovieRepository, mMovieAdapter);
    }
}
