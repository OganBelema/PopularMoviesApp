package com.oganbelema.network.source;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.oganbelema.network.MoviesApi;

import javax.inject.Inject;

public class TopRatedMovieDataSourceFactory extends DataSource.Factory {

    private final MoviesApi mMoviesApi;

    private MutableLiveData<PagedTopRatedMovieNetworkSource> mTopRatedMovieNetworkSourceMutableLiveData
            = new MutableLiveData<>();

    @Inject
    public TopRatedMovieDataSourceFactory(MoviesApi mMoviesApi) {
        this.mMoviesApi = mMoviesApi;
    }

    @NonNull
    @Override
    public DataSource create() {
        PagedTopRatedMovieNetworkSource pagedTopRatedMovieNetworkSource = new PagedTopRatedMovieNetworkSource(mMoviesApi);
        mTopRatedMovieNetworkSourceMutableLiveData.postValue(pagedTopRatedMovieNetworkSource);
        return pagedTopRatedMovieNetworkSource;
    }

    public LiveData<PagedTopRatedMovieNetworkSource> getTopRatedMovieNetworkSourceMutableLiveData() {
        return mTopRatedMovieNetworkSourceMutableLiveData;
    }
}
