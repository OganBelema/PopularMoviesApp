package com.oganbelema.popularmovies.movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oganbelema.popularmovies.network.MoviesApi;
import com.oganbelema.popularmovies.network.NetworkCallResult;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@Singleton
public class MovieRepository {

    private final MoviesApi mMoviesApi;

    private MutableLiveData<NetworkCallResult<MovieResponse>> mPopularMovieNetworkCallResult
            = new MutableLiveData<>();

    private MutableLiveData<NetworkCallResult<MovieResponse>> mTopRatedMovieNetworkCallResult
            = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MovieRepository(MoviesApi mMoviesApi) {
        this.mMoviesApi = mMoviesApi;
    }

    public LiveData<NetworkCallResult<MovieResponse>> getPopularMovieNetworkCallResult() {
        return mPopularMovieNetworkCallResult;
    }

    public LiveData<NetworkCallResult<MovieResponse>> getTopRatedMovieNetworkCallResult() {
        return mTopRatedMovieNetworkCallResult;
    }


    public void getPopularMovies(){
        final Single<Response<MovieResponse>> popularMovieResponse = mMoviesApi.getPopularMovies();
        popularMovieResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MovieResponse>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        disposables.add(disposable);
                    }

                    @Override
                    public void onSuccess(Response<MovieResponse> popularMovieResponseResponse) {
                        mPopularMovieNetworkCallResult.postValue(
                                new NetworkCallResult<>(popularMovieResponseResponse));
                    }

                    @Override
                    public void onError(Throwable error) {
                        mPopularMovieNetworkCallResult.postValue(
                                new NetworkCallResult<>(error));
                    }
                });
    }

    public void getTopRatedMovies(){
        final Single<Response<MovieResponse>> topRatedMoviesResponse = mMoviesApi.getTopRatedMovies();
        topRatedMoviesResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MovieResponse>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        disposables.add(disposable);
                    }

                    @Override
                    public void onSuccess(Response<MovieResponse> movieResponseResponse) {
                        mTopRatedMovieNetworkCallResult.postValue(
                                new NetworkCallResult<>(movieResponseResponse));
                    }

                    @Override
                    public void onError(Throwable error) {
                        mTopRatedMovieNetworkCallResult.postValue(
                                new NetworkCallResult<>(error));
                    }
                });
    }

    public void dispose(){
        if (disposables != null){
            disposables.dispose();
        }
    }


}
