package com.oganbelema.popularmovies.movie.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.oganbelema.popularmovies.movie.repository.MovieRepository;
import com.oganbelema.popularmovies.movie.model.MovieResponse;
import com.oganbelema.popularmovies.network.NetworkCallResult;


public class MovieViewModel extends ViewModel {

    private final MovieRepository mMovieRepository;

    public MovieViewModel(MovieRepository movieRepository) {
        mMovieRepository = movieRepository;
    }

    public LiveData<NetworkCallResult<MovieResponse>> getPopularMovies(){
        mMovieRepository.getPopularMovies();
        return mMovieRepository.getPopularMovieNetworkCallResult();
    }

    public LiveData<NetworkCallResult<MovieResponse>> getTopRatedMovies(){
        mMovieRepository.getTopRatedMovies();
        return mMovieRepository.getTopRatedMovieNetworkCallResult();
    }

    @Override
    protected void onCleared() {
        mMovieRepository.dispose();
        super.onCleared();
    }
}
