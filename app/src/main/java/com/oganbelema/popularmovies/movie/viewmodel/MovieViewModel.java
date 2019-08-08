package com.oganbelema.popularmovies.movie.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.popularmovies.movie.FilterOptions;
import com.oganbelema.popularmovies.movie.PagedMovieAdapter;
import com.oganbelema.popularmovies.movie.repository.MovieRepository;
import com.oganbelema.popularmovies.movie.MovieAdapter;

import java.util.List;


public class MovieViewModel extends ViewModel {

    private final MovieRepository mMovieRepository;

    private final MovieAdapter mMovieAdapter;

    private final PagedMovieAdapter mPagedMovieAdapter;

    private MutableLiveData<FilterOptions> filterOptions = new MutableLiveData<>();


    public MovieViewModel(MovieRepository movieRepository, MovieAdapter movieAdapter,
                          PagedMovieAdapter pagedMovieAdapter) {
        mMovieRepository = movieRepository;
        mMovieAdapter = movieAdapter;
        mPagedMovieAdapter = pagedMovieAdapter;
        filterOptions.setValue(FilterOptions.POPULAR_MOVIES);
    }

    public MovieAdapter getMovieAdapter() {
        return mMovieAdapter;
    }

    public PagedMovieAdapter getPagedMovieAdapter() {
        return mPagedMovieAdapter;
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

    public LiveData<Boolean> getLoading() {
        return mMovieRepository.getLoading();
    }

    public LiveData<PagedList<Movie>> getPopularMovieLiveData() {
        return mMovieRepository.getPopularMovies();
    }

    public LiveData<PagedList<Movie>> getTopRatedMovieLiveData() {
        return mMovieRepository.getTopRatedMovies();
    }

    @Override
    protected void onCleared() {
        mMovieRepository.dispose();
        super.onCleared();
    }
}
