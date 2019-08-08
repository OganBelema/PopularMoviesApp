package com.oganbelema.popularmovies.movie.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.oganbelema.database.PopularMoviesDB;
import com.oganbelema.database.entity.FavoriteMovieEntity;
import com.oganbelema.database.mapper.EntityMapper;
import com.oganbelema.network.NetworkUtil;
import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.network.source.PagedPopularMovieNetworkSource;
import com.oganbelema.network.source.PagedTopRatedMovieNetworkSource;
import com.oganbelema.network.source.factory.PopularMovieDataSourceFactory;
import com.oganbelema.network.source.factory.TopRatedMovieDataSourceFactory;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class MovieRepository {

    private final NetworkUtil mNetworkUtil;

    private final PopularMovieDataSourceFactory mPopularMovieDataSourceFactory;

    private final TopRatedMovieDataSourceFactory mTopRatedMovieDataSourceFactory;

    private final PopularMoviesDB mPopularMoviesDB;

    private final EntityMapper<FavoriteMovieEntity, Movie> mEntityMapper;

    private final MutableLiveData<List<Movie>> mFavoriteMovies = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<Boolean> mNetworkStatus = new MutableLiveData<>();

    private LiveData<Throwable> mError;

    private LiveData<Boolean> mLoading;

    private final PagedList.Config mPagedListConfig;

    private LiveData<PagedList<Movie>> mPopularMovieLiveData;

    private LiveData<PagedList<Movie>> mTopRatedMovieLiveData;

    @Inject
    public MovieRepository(NetworkUtil networkUtil,
                           PopularMovieDataSourceFactory popularMovieDataSourceFactory,
                           TopRatedMovieDataSourceFactory topRatedMovieDataSourceFactory,
                           PopularMoviesDB popularMoviesDB,
                           EntityMapper<FavoriteMovieEntity, Movie> entityMapper,
                           PagedList.Config config) {
        mNetworkUtil = networkUtil;
        mPopularMovieDataSourceFactory = popularMovieDataSourceFactory;
        mTopRatedMovieDataSourceFactory = topRatedMovieDataSourceFactory;
        mPopularMoviesDB = popularMoviesDB;
        mEntityMapper = entityMapper;
        mPagedListConfig = config;
        setupPopularMoviesDataSource();
        setupTopRatedMoviesDataSource();
    }

    public LiveData<Boolean> getNetworkStatus() {
        return mNetworkStatus;
    }

    public LiveData<Throwable> getError() {
        return mError;
    }

    public LiveData<Boolean> getLoading() {
        return mLoading;
    }

    private void setNetworkStatus() {
        mNetworkStatus.postValue(mNetworkUtil.isConnected());
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        disposables.add(
                mPopularMoviesDB.getFavoriteMovieDao().getFavoriteMovies()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::mapFavoriteMovieEntitiesToMovies)
        );
        return mFavoriteMovies;
    }

    private void mapFavoriteMovieEntitiesToMovies(List<FavoriteMovieEntity> favoriteMovieEntities) {
        disposables.add(
                Observable.fromCallable(() ->
                        mEntityMapper.fromEntityList(favoriteMovieEntities))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mFavoriteMovies::postValue)
        );
    }

    public void addFavoriteMovie(Movie movie) {
        disposables.add(
                Observable.fromCallable(() ->
                        mEntityMapper.toEntity(movie))
                        .subscribeOn(Schedulers.io())
                        .subscribe(favoriteMovieEntity ->
                                mPopularMoviesDB.getFavoriteMovieDao().insert(favoriteMovieEntity))
        );
    }

    public void removeFavoriteMovie(Movie movie) {
        disposables.add(
                Observable.fromCallable(() ->
                        mEntityMapper.toEntity(movie))
                        .subscribeOn(Schedulers.io())
                        .subscribe(favoriteMovieEntity ->
                                mPopularMoviesDB.getFavoriteMovieDao().delete(favoriteMovieEntity))
        );
    }

    public LiveData<PagedList<Movie>> getPopularMovies(){
        setNetworkStatus();
        listenFromPopularMovies();
        return mPopularMovieLiveData;
    }

    private void listenFromPopularMovies() {
        mError = Transformations.switchMap(mPopularMovieDataSourceFactory
                        .getPagedPopularMovieNetworkSourceMutableLiveData(),
                PagedPopularMovieNetworkSource::getError);

        mLoading = Transformations.switchMap(mPopularMovieDataSourceFactory
                .getPagedPopularMovieNetworkSourceMutableLiveData(),
                PagedPopularMovieNetworkSource::getLoading);
    }

    private void setupPopularMoviesDataSource(){
        mPopularMovieLiveData = new LivePagedListBuilder<Long, Movie>(
                mPopularMovieDataSourceFactory, mPagedListConfig)
                .build();

        listenFromPopularMovies();
    }

    public LiveData<PagedList<Movie>> getTopRatedMovies(){
        setNetworkStatus();
        listenFromTopRatedMovies();
        return mTopRatedMovieLiveData;
    }

    private void listenFromTopRatedMovies() {
        mError = Transformations.switchMap(mTopRatedMovieDataSourceFactory
                        .getTopRatedMovieNetworkSourceMutableLiveData(),
                PagedTopRatedMovieNetworkSource::getError);

        mLoading = Transformations.switchMap(mTopRatedMovieDataSourceFactory
                        .getTopRatedMovieNetworkSourceMutableLiveData(),
                PagedTopRatedMovieNetworkSource::getLoading);
    }

    private void setupTopRatedMoviesDataSource(){
        mTopRatedMovieLiveData = new LivePagedListBuilder<Long, Movie>(
                mTopRatedMovieDataSourceFactory, mPagedListConfig)
                .build();
    }

    public void resetPopularMovies(){
        if (mPopularMovieLiveData.getValue() != null){
            mPopularMovieLiveData.getValue().getDataSource().invalidate();
        }
    }

    public void resetTopRatedMovies(){
        if (mTopRatedMovieLiveData.getValue() != null){
            mTopRatedMovieLiveData.getValue().getDataSource().invalidate();
        }
    }

    public void dispose() {
        if (mPopularMovieLiveData.getValue() != null){
            ((PagedPopularMovieNetworkSource) mPopularMovieLiveData.getValue()
                    .getDataSource()).dispose();
        }

        if (mTopRatedMovieLiveData.getValue() != null){
            ((PagedTopRatedMovieNetworkSource) mTopRatedMovieLiveData.getValue()
                    .getDataSource()).dispose();
        }

        disposables.dispose();
    }

}
