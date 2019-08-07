package com.oganbelema.network.source.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.oganbelema.network.MoviesApi;
import com.oganbelema.network.source.PagedTopRatedMovieNetworkSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TopRatedMovieDataSourceFactory extends DataSource.Factory {

    private final MoviesApi moviesApi;

    private MutableLiveData<PagedTopRatedMovieNetworkSource> mTopRatedMovieNetworkSourceMutableLiveData
            = new MutableLiveData<>();

    @Inject
    public TopRatedMovieDataSourceFactory(MoviesApi moviesApi) {
        this.moviesApi = moviesApi;
    }

    @NonNull
    @Override
    public DataSource create() {
        PagedTopRatedMovieNetworkSource pagedTopRatedMovieNetworkSource = new PagedTopRatedMovieNetworkSource(moviesApi);
        mTopRatedMovieNetworkSourceMutableLiveData.postValue(pagedTopRatedMovieNetworkSource);

        return pagedTopRatedMovieNetworkSource;
    }

    public LiveData<PagedTopRatedMovieNetworkSource> getTopRatedMovieNetworkSourceMutableLiveData() {
        return mTopRatedMovieNetworkSourceMutableLiveData;
    }


}
