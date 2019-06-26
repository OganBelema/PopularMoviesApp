package com.oganbelema.popularmovies.movie.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.popularmovies.movie.FilterOptions;
import com.oganbelema.popularmovies.movie.repository.MovieRepository;
import com.oganbelema.popularmovies.movie.ui.MovieAdapter;

import java.util.List;


public class MovieListViewModel extends ViewModel {

    private final MovieRepository mMovieRepository;

    private final MovieAdapter mMovieAdapter;

    public MovieListViewModel(MovieRepository movieRepository, MovieAdapter movieAdapter) {
        mMovieRepository = movieRepository;
        mMovieAdapter = movieAdapter;
        filterOptions.setValue(FilterOptions.POPULAR_MOVIES);
    }

    private MutableLiveData<FilterOptions> filterOptions = new MutableLiveData<>();

    public MovieAdapter getMovieAdapter() {
        return mMovieAdapter;
    }

    public LiveData<List<Movie>> getPopularMovies(){
        mMovieRepository.getPopularMovies();
        return mMovieRepository.getMovies();
    }

    public LiveData<List<Movie>> getTopRatedMovies(){
        mMovieRepository.getTopRatedMovies();
        return mMovieRepository.getMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies(){
        return mMovieRepository.getFavoriteMovies();
    }

    public LiveData<Throwable> getError(){
        return mMovieRepository.getError();
    }

    public LiveData<Boolean> getNetworkStatus(){
        return mMovieRepository.getNetworkStatus();
    }

    public void setFilterOption(FilterOptions filterOption) {
        filterOptions.postValue(filterOption);
    }

    public LiveData<FilterOptions> getFilterOptions() {
        return filterOptions;
    }

    public FilterOptions getRawFilterOption(){
        return filterOptions.getValue();
    }

    @Override
    protected void onCleared() {
        mMovieRepository.dispose();
        super.onCleared();
    }
}
