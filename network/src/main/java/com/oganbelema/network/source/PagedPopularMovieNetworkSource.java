package com.oganbelema.network.source;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.oganbelema.network.MoviesApi;
import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.network.model.movie.MovieResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class PagedPopularMovieNetworkSource extends PageKeyedDataSource<Long, Movie> {

    private final MoviesApi mMoviesApi;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Throwable> mError = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mLoading = new MutableLiveData<>();


    public PagedPopularMovieNetworkSource(MoviesApi moviesApi) {
        mMoviesApi = moviesApi;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, Movie> callback) {
        getPopularMoviesRemote(1, callback, null, null, (long) 2);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull final LoadCallback<Long, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull final LoadCallback<Long, Movie> callback) {
        getPopularMoviesRemote(params.key, null, callback, null, params.key + 1);
    }

    private void getPopularMoviesRemote(final long page, final LoadInitialCallback<Long, Movie> initialCallback, final LoadCallback<Long, Movie> callback, final Long previousPage, final Long nextPage) {
        mMoviesApi.getPopularMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MovieResponse>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        disposables.add(disposable);
                        mLoading.postValue(true);
                    }

                    @Override
                    public void onSuccess(Response<MovieResponse> popularMovieResponse) {
                        handleSuccessfulMovieRequest(initialCallback, callback, previousPage, nextPage, popularMovieResponse);
                        mLoading.postValue(false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mLoading.postValue(false);
                        mError.postValue(error);
                    }
                });
    }

    private void handleSuccessfulMovieRequest(LoadInitialCallback<Long, Movie> loadInitialCallback, LoadCallback<Long, Movie> callback, Long previousPage, Long nextPage, final Response<MovieResponse> movieResponse) {
        if (movieResponse != null) {
            if (movieResponse.isSuccessful()) {
                final MovieResponse responseBody = movieResponse.body();

                if (responseBody != null) {
                    if (loadInitialCallback != null) {
                        loadInitialCallback.onResult(responseBody.getMovies(), previousPage, nextPage);
                    } else {
                        callback.onResult(responseBody.getMovies(), nextPage);
                    }
                }
            }
        }
    }

    public LiveData<Throwable> getError() {
        return mError;
    }

    public LiveData<Boolean> getLoading() {
        return mLoading;
    }

    public void dispose(){
        disposables.dispose();
    }
}
