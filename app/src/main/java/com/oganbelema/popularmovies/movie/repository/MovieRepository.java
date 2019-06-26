package com.oganbelema.popularmovies.movie.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oganbelema.database.PopularMoviesDB;
import com.oganbelema.database.entity.FavoriteMovieEntity;
import com.oganbelema.database.mapper.FavoriteMovieEntityMapper;
import com.oganbelema.popularmovies.movie.model.Movie;
import com.oganbelema.popularmovies.movie.model.MovieResponse;
import com.oganbelema.popularmovies.network.MoviesApi;
import com.oganbelema.popularmovies.network.NetworkUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@Singleton
public class MovieRepository {

    private final NetworkUtil mNetworkUtil;

    private final MoviesApi mMoviesApi;

    private final PopularMoviesDB mPopularMoviesDB;

    private final FavoriteMovieEntityMapper<FavoriteMovieEntity, Movie> mFavoriteMovieEntityMapper;

    private MutableLiveData<List<Movie>> mMovies = new MutableLiveData<>();

    private MutableLiveData<Throwable> mError = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<Boolean> mNetworkStatus = new MutableLiveData<>();

    @Inject
    public MovieRepository(NetworkUtil networkUtil, MoviesApi mMoviesApi,
                           PopularMoviesDB popularMoviesDB,
                           FavoriteMovieEntityMapper<FavoriteMovieEntity, Movie>
                                   favoriteMovieEntityMapper) {
        this.mNetworkUtil = networkUtil;
        this.mMoviesApi = mMoviesApi;
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
            getPopularMoviesRemote();
        }
    }

    private void setNetworkStatus() {
        mNetworkStatus.postValue(mNetworkUtil.isConnected());
    }

    public void getTopRatedMovies() {
        setNetworkStatus();

        if (mNetworkUtil.isConnected()) {
            getTopRatedMoviesRemote();
        }
    }

    public void getFavoriteMovies() {
        disposables.add(
                mPopularMoviesDB.getFavoriteMovieDao().getFavoriteMovies()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::mapToFavoriteMovieEntitiesToMovies)
        );
    }

    private void mapToFavoriteMovieEntitiesToMovies(List<FavoriteMovieEntity> favoriteMovieEntities) {
        disposables.add(
                Observable.fromCallable(() ->
                        mFavoriteMovieEntityMapper.fromFavoriteMovieEntities(favoriteMovieEntities))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(movies -> mMovies.postValue(movies))
        );
    }

    private void getPopularMoviesRemote() {
        mMoviesApi.getPopularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MovieResponse>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        disposables.add(disposable);
                    }

                    @Override
                    public void onSuccess(Response<MovieResponse> popularMovieResponse) {
                        handleSuccessfulPopularMovieRequest(popularMovieResponse);
                    }

                    @Override
                    public void onError(Throwable error) {
                         mError.postValue(error);
                    }
                });
    }

    private void handleSuccessfulPopularMovieRequest(Response<MovieResponse> movieResponse) {
        if (movieResponse != null) {
            if (movieResponse.isSuccessful()) {
                MovieResponse popularMovies = movieResponse.body();

                if (popularMovies != null) {
                    mMovies.postValue(popularMovies.getMovies());
                }
            }
        }
    }

    private void getTopRatedMoviesRemote() {
        mMoviesApi.getTopRatedMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MovieResponse>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        disposables.add(disposable);
                    }

                    @Override
                    public void onSuccess(Response<MovieResponse> topRatedMovieResponse) {
                        handleSuccessfulPopularMovieRequest(topRatedMovieResponse);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mError.postValue(error);
                    }
                });
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
        return mError;
    }

    public void dispose() {
        if (disposables != null) {
            disposables.dispose();
        }
    }


}
