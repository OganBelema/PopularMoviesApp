package com.oganbelema.network.source.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.oganbelema.network.MoviesApi;
import com.oganbelema.network.source.PagedPopularMovieNetworkSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PopularMovieDataSourceFactory extends DataSource.Factory {

    private final MoviesApi mMoviesApi;

    private final MutableLiveData<PagedPopularMovieNetworkSource> mPagedPopularMovieNetworkSourceMutableLiveData
            = new MutableLiveData<>();

    @Inject
    public PopularMovieDataSourceFactory(MoviesApi moviesApi) {
        mMoviesApi = moviesApi;

    }

    @NonNull
    @Override
    public DataSource create() {
        PagedPopularMovieNetworkSource pagedPopularMovieNetworkSource = new PagedPopularMovieNetworkSource(mMoviesApi);
        mPagedPopularMovieNetworkSourceMutableLiveData.postValue(pagedPopularMovieNetworkSource);
        return pagedPopularMovieNetworkSource;
    }

    public LiveData<PagedPopularMovieNetworkSource> getPagedPopularMovieNetworkSourceMutableLiveData() {
        return mPagedPopularMovieNetworkSourceMutableLiveData;
    }
}
