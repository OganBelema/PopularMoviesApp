package com.oganbelema.popularmovies.movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oganbelema.popularmovies.network.NetworkCallResult;
import com.oganbelema.popularmovies.network.ServiceGenerator;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MovieRepository {

    private MutableLiveData<NetworkCallResult<MovieResponse>> mPopularMovieNetworkCallResult
            = new MutableLiveData<>();

    private MutableLiveData<NetworkCallResult<MovieResponse>> mTopRatedMovieNetworkCallResult
            = new MutableLiveData<>();

    private Disposable mPopularMovieDisposable;

    private Disposable mTopRatedMovieDisposable;

    public LiveData<NetworkCallResult<MovieResponse>> getPopularMovieNetworkCallResult() {
        return mPopularMovieNetworkCallResult;
    }

    public LiveData<NetworkCallResult<MovieResponse>> getTopRatedMovieNetworkCallResult() {
        return mTopRatedMovieNetworkCallResult;
    }


    public void getPopularMovies(){
        final Single<Response<MovieResponse>> popularMovieResponse = ServiceGenerator.getMovieApi()
                .getPopularMovies();
        popularMovieResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MovieResponse>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mPopularMovieDisposable = disposable;
                    }

                    @Override
                    public void onSuccess(Response<MovieResponse> popularMovieResponseResponse) {
                        mPopularMovieNetworkCallResult.postValue(
                                new NetworkCallResult<>(popularMovieResponseResponse));
                    }

                    @Override
                    public void onError(Throwable error) {
                        mPopularMovieNetworkCallResult.postValue(
                                new NetworkCallResult<MovieResponse>(error));
                    }
                });
    }

    public void getTopRatedMovies(){
        final Single<Response<MovieResponse>> topRatedMoviesResponse = ServiceGenerator.getMovieApi()
                .getTopRatedMovies();
        topRatedMoviesResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MovieResponse>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mTopRatedMovieDisposable = disposable;
                    }

                    @Override
                    public void onSuccess(Response<MovieResponse> movieResponseResponse) {
                        mTopRatedMovieNetworkCallResult.postValue(
                                new NetworkCallResult<>(movieResponseResponse));
                    }

                    @Override
                    public void onError(Throwable error) {
                        mTopRatedMovieNetworkCallResult.postValue(
                                new NetworkCallResult<MovieResponse>(error));
                    }
                });
    }

    public void dispose(){
        if (mPopularMovieDisposable != null){
            mPopularMovieDisposable.dispose();
        }

        if (mTopRatedMovieDisposable != null){
            mTopRatedMovieDisposable.dispose();
        }
    }


}
