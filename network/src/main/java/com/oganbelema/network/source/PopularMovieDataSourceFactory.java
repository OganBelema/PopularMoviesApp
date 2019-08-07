package com.oganbelema.network.source;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.oganbelema.network.MoviesApi;

import javax.inject.Inject;

public class PopularMovieDataSourceFactory extends DataSource.Factory {

    private final MoviesApi mMoviesApi;

    private final MutableLiveData<PagedPopularMovieNetworkSource> mPagedPopularMovieNetworkSourceMutableLiveData
            = new MutableLiveData<>();

    @Inject
    public PopularMovieDataSourceFactory(MoviesApi mMoviesApi) {
        this.mMoviesApi = mMoviesApi;
    }

    @NonNull
    @Override
    public DataSource create() {
        PagedPopularMovieNetworkSource mPagedPopularMovieNetworkSource = new PagedPopularMovieNetworkSource(mMoviesApi);
        mPagedPopularMovieNetworkSourceMutableLiveData.postValue(mPagedPopularMovieNetworkSource);
        return mPagedPopularMovieNetworkSource;
    }

    public LiveData<PagedPopularMovieNetworkSource> getPagedPopularMovieNetworkSourceMutableLiveData() {
        return mPagedPopularMovieNetworkSourceMutableLiveData;
    }
}
