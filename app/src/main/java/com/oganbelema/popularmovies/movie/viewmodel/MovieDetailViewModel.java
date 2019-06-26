package com.oganbelema.popularmovies.movie.viewmodel;

import androidx.lifecycle.ViewModel;

import com.oganbelema.popularmovies.movie.repository.MovieRepository;

public class MovieDetailViewModel extends ViewModel {

    private final MovieRepository mMovieRepository;

    public MovieDetailViewModel(MovieRepository mMovieRepository) {
        this.mMovieRepository = mMovieRepository;
    }


}
