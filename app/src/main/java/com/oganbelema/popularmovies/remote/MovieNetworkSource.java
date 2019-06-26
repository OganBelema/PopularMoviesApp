package com.oganbelema.popularmovies.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oganbelema.popularmovies.network.MoviesApi;
import com.oganbelema.popularmovies.remote.model.movie.Movie;
import com.oganbelema.popularmovies.remote.model.movie.MovieResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@Singleton
public class MovieNetworkSource {

    private final MoviesApi mMoviesApi;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Movie>> mMovies = new MutableLiveData<>();

    private final MutableLiveData<Throwable> mError = new MutableLiveData<>();

    @Inject
    public MovieNetworkSource(MoviesApi mMoviesApi) {
        this.mMoviesApi = mMoviesApi;
    }

    public void getTopRatedMoviesRemote() {
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
                        handleSuccessfulMovieRequest(topRatedMovieResponse);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mError.postValue(error);
                    }
                });
    }

    public void getPopularMoviesRemote() {
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
                        handleSuccessfulMovieRequest(popularMovieResponse);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mError.postValue(error);
                    }
                });
    }

    private void handleSuccessfulMovieRequest(Response<MovieResponse> movieResponse) {
        if (movieResponse != null) {
            if (movieResponse.isSuccessful()) {
                MovieResponse responseBody = movieResponse.body();

                if (responseBody != null) {
                    mMovies.postValue(responseBody.getMovies());
                }
            }
        }
    }

    public MutableLiveData<Throwable> getError() {
        return mError;
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovies;
    }

    public void dispose(){
        disposables.dispose();
    }
}
