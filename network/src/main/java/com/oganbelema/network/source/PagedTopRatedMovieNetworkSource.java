package com.oganbelema.network.source;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;


import com.oganbelema.network.MoviesApi;
import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.network.model.movie.MovieResponse;

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
public class PagedTopRatedMovieNetworkSource extends PageKeyedDataSource<Long, Movie> {

    private final MoviesApi mMoviesApi;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Movie>> mMovies = new MutableLiveData<>();

    private final MutableLiveData<Throwable> mError = new MutableLiveData<>();

    @Inject
    public PagedTopRatedMovieNetworkSource(MoviesApi mMoviesApi) {
        this.mMoviesApi = mMoviesApi;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Movie> callback) {
        getTopRatedMoviesRemote(1, callback,null, null, (long) 2);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {
        getTopRatedMoviesRemote(params.key, null, callback, null, params.key + 1);
    }

    public void getTopRatedMoviesRemote(final long currentPage, final LoadInitialCallback<Long, Movie> initialCallback, final LoadCallback<Long, Movie> callback, final Long previousPage, final Long nextPage) {
        mMoviesApi.getTopRatedMovies(currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MovieResponse>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        disposables.add(disposable);
                    }

                    @Override
                    public void onSuccess(Response<MovieResponse> topRatedMovieResponse) {
                        handleSuccessfulMovieRequest(initialCallback, callback, previousPage, nextPage, topRatedMovieResponse);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mError.postValue(error);
                    }
                });
    }

    private void handleSuccessfulMovieRequest(LoadInitialCallback<Long, Movie> initialCallback, LoadCallback<Long, Movie> callback, Long previousPage, Long nextPage, Response<MovieResponse> movieResponse) {
        if (movieResponse != null) {
            if (movieResponse.isSuccessful()) {
                MovieResponse responseBody = movieResponse.body();

                if (responseBody != null) {
                    mMovies.postValue(responseBody.getMovies());
                    if (initialCallback != null){
                        initialCallback.onResult(responseBody.getMovies(),previousPage, nextPage);
                    } else {
                        callback.onResult(responseBody.getMovies(), nextPage);
                    }

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
