package com.oganbelema.popularmovies.movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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
