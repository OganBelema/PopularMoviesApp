package com.oganbelema.popularmovies.movie.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.network.source.PagedPopularMovieNetworkSource;
import com.oganbelema.network.source.PagedTopRatedMovieNetworkSource;
import com.oganbelema.popularmovies.movie.FilterOptions;
import com.oganbelema.popularmovies.movie.PagedMovieAdapter;
import com.oganbelema.popularmovies.movie.repository.MovieRepository;
import com.oganbelema.popularmovies.movie.MovieAdapter;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MovieViewModel extends ViewModel {

    private final MovieRepository mMovieRepository;

    private final MovieAdapter mMovieAdapter;

    private final PagedMovieAdapter mPagedMovieAdapter;

    private LiveData<Throwable> mError;

    private Executor executor = Executors.newFixedThreadPool(5);

    private final PagedList.Config mPagedListConfig;

    private LiveData<PagedList<Movie>> mPopularMovieLiveData;

    private LiveData<PagedList<Movie>> mTopRatedMovieLiveData;

    private MutableLiveData<FilterOptions> filterOptions = new MutableLiveData<>();


    public MovieViewModel(MovieRepository movieRepository, MovieAdapter movieAdapter,
                          PagedMovieAdapter pagedMovieAdapter) {
        mMovieRepository = movieRepository;
        mMovieAdapter = movieAdapter;
        mPagedMovieAdapter = pagedMovieAdapter;
        mPagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .setPrefetchDistance(4)
                .build();
        setupPopularMoviesDataSource();
        setupTopRatedMoviesDataSource();
        filterOptions.setValue(FilterOptions.POPULAR_MOVIES);
    }

    public MovieAdapter getMovieAdapter() {
        return mMovieAdapter;
    }

    public PagedMovieAdapter getPagedMovieAdapter() {
        return mPagedMovieAdapter;
    }

    public LiveData<PagedList<Movie>> getPopularMovies(){
        listenForErrorFromPopularMovies();

        return mPopularMovieLiveData;
    }

    private void listenForErrorFromPopularMovies() {
        mError = Transformations.switchMap(mMovieRepository.getPopularMovieDataSourceFactory()
                        .getPagedPopularMovieNetworkSourceMutableLiveData(),
                PagedPopularMovieNetworkSource::getError);
    }

    private void setupPopularMoviesDataSource(){
        mPopularMovieLiveData = new LivePagedListBuilder<>(
                mMovieRepository.getPopularMovieDataSourceFactory(), mPagedListConfig)
                .setFetchExecutor(executor)
                .build();

        listenForErrorFromPopularMovies();
    }

    public LiveData<PagedList<Movie>> getTopRatedMovies(){
        mError = Transformations.switchMap(mMovieRepository.getTopRatedMovieDataSourceFactory()
                        .getTopRatedMovieNetworkSourceMutableLiveData(),
                PagedTopRatedMovieNetworkSource::getError);

        return mTopRatedMovieLiveData;
    }

    private void setupTopRatedMoviesDataSource(){
        mTopRatedMovieLiveData = new LivePagedListBuilder<>(
                mMovieRepository.getTopRatedMovieDataSourceFactory(), mPagedListConfig)
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<List<Movie>> getFavoriteMovies(){
        return mMovieRepository.getFavoriteMovies();
    }

    public LiveData<Throwable> getError(){
        return mError;
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
