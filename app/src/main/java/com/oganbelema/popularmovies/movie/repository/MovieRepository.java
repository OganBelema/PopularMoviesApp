package com.oganbelema.popularmovies.movie.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oganbelema.database.PopularMoviesDB;
import com.oganbelema.database.entity.FavoriteMovieEntity;
import com.oganbelema.database.mapper.EntityMapper;
import com.oganbelema.network.NetworkUtil;
import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.network.source.MovieNetworkSource;

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

    private final MovieNetworkSource mMovieNetworkSource;

    private final PopularMoviesDB mPopularMoviesDB;

    private final EntityMapper<FavoriteMovieEntity, Movie> mEntityMapper;

    private LiveData<List<Movie>> mMovies = new MutableLiveData<>();

    private final MutableLiveData<List<Movie>> mFavoriteMovies = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<Boolean> mNetworkStatus = new MutableLiveData<>();

    @Inject
    public MovieRepository(NetworkUtil networkUtil, MovieNetworkSource movieNetworkSource,
                           PopularMoviesDB popularMoviesDB,
                           EntityMapper<FavoriteMovieEntity, Movie>
                                   entityMapper) {
        this.mNetworkUtil = networkUtil;
        this.mMovieNetworkSource = movieNetworkSource;
        this.mPopularMoviesDB = popularMoviesDB;
        this.mEntityMapper = entityMapper;
    }

    public LiveData<Boolean> getNetworkStatus() {
        return mNetworkStatus;
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovies;
    }


    public void getPopularMovies() {
        setNetworkStatus();

        if (mNetworkUtil.isConnected()) {
            mMovieNetworkSource.getPopularMoviesRemote();
            mMovies = mMovieNetworkSource.getMovies();
        }
    }

    private void setNetworkStatus() {
        mNetworkStatus.postValue(mNetworkUtil.isConnected());
    }

    public void getTopRatedMovies() {
        setNetworkStatus();

        if (mNetworkUtil.isConnected()) {
            mMovieNetworkSource.getTopRatedMoviesRemote();
            mMovies = mMovieNetworkSource.getMovies();
        }
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

    public MutableLiveData<Throwable> getError() {
        return mMovieNetworkSource.getError();
    }

    public void dispose() {
            mMovieNetworkSource.dispose();
            disposables.dispose();
    }

}
