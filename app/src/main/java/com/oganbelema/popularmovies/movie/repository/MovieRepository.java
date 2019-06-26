package com.oganbelema.popularmovies.movie.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oganbelema.database.PopularMoviesDB;
import com.oganbelema.database.entity.FavoriteMovieEntity;
import com.oganbelema.database.mapper.FavoriteMovieEntityMapper;
import com.oganbelema.popularmovies.remote.MovieNetworkSource;
import com.oganbelema.popularmovies.remote.model.movie.Movie;
import com.oganbelema.popularmovies.network.NetworkUtil;

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

    private final FavoriteMovieEntityMapper<FavoriteMovieEntity, Movie> mFavoriteMovieEntityMapper;

    private LiveData<List<Movie>> mMovies = new MutableLiveData<>();

    private final MutableLiveData<List<Movie>> mFavoriteMovies = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<Boolean> mNetworkStatus = new MutableLiveData<>();

    @Inject
    public MovieRepository(NetworkUtil networkUtil, MovieNetworkSource movieNetworkSource,
                           PopularMoviesDB popularMoviesDB,
                           FavoriteMovieEntityMapper<FavoriteMovieEntity, Movie>
                                   favoriteMovieEntityMapper) {
        this.mNetworkUtil = networkUtil;
        this.mMovieNetworkSource = movieNetworkSource;
        this.mPopularMoviesDB = popularMoviesDB;
        this.mFavoriteMovieEntityMapper = favoriteMovieEntityMapper;
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
                        .subscribe(this::mapToFavoriteMovieEntitiesToMovies)
        );
        return mFavoriteMovies;
    }

    private void mapToFavoriteMovieEntitiesToMovies(List<FavoriteMovieEntity> favoriteMovieEntities) {
        disposables.add(
                Observable.fromCallable(() ->
                        mFavoriteMovieEntityMapper.fromFavoriteMovieEntities(favoriteMovieEntities))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mFavoriteMovies::postValue)
        );
    }

    public void addFavoriteMovie(Movie movie) {
        disposables.add(
                Observable.fromCallable(() ->
                        mFavoriteMovieEntityMapper.toFavoriteMovieEntity(movie))
                        .subscribeOn(Schedulers.computation())
                        .subscribe(favoriteMovieEntity ->
                                mPopularMoviesDB.getFavoriteMovieDao().insert(favoriteMovieEntity))
        );
    }

    public void removeFavoriteMovie(Movie movie) {
        disposables.add(
                Observable.fromCallable(() ->
                        mFavoriteMovieEntityMapper.toFavoriteMovieEntity(movie))
                        .subscribeOn(Schedulers.computation())
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
