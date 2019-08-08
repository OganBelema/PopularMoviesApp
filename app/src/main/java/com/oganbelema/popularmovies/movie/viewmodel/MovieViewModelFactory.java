package com.oganbelema.popularmovies.movie.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.oganbelema.popularmovies.movie.PagedMovieAdapter;
import com.oganbelema.popularmovies.movie.repository.MovieRepository;
import com.oganbelema.popularmovies.movie.MovieAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieViewModelFactory implements ViewModelProvider.Factory {

    private final MovieRepository mMovieRepository;

    private final MovieAdapter mMovieAdapter;

    private final PagedMovieAdapter mPagedMovieAdapter;

    @Inject
    public MovieViewModelFactory(MovieRepository movieRepository, MovieAdapter movieAdapter,
                                 PagedMovieAdapter pagedMovieAdapter) {
        mMovieRepository = movieRepository;
        mMovieAdapter = movieAdapter;
        mPagedMovieAdapter = pagedMovieAdapter;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieViewModel(mMovieRepository, mMovieAdapter, mPagedMovieAdapter);
    }
}
