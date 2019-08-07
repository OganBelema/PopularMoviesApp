package com.oganbelema.network.source;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.oganbelema.network.MoviesApi;
import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.network.model.movie.MovieResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PagedPopularMovieNetworkSource extends PageKeyedDataSource<Long, Movie> {

    private final MoviesApi mMoviesApi;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Movie>> mMovies = new MutableLiveData<>();

    private final MutableLiveData<Throwable> mError = new MutableLiveData<>();

    @Inject
    public PagedPopularMovieNetworkSource(MoviesApi mMoviesApi) {
        this.mMoviesApi = mMoviesApi;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Movie> callback) {
        getPopularMoviesRemote(1, callback, null, null, (long) 2);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {
        getPopularMoviesRemote(params.key, null, callback, null, params.key + 1);
    }

    public void getPopularMoviesRemote(final long page, final LoadInitialCallback<Long, Movie> initialCallback, final LoadCallback<Long, Movie> callback, final Long previousPage, final Long nextPage) {
        mMoviesApi.getPopularMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MovieResponse>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        disposables.add(disposable);
                    }

                    @Override
                    public void onSuccess(Response<MovieResponse> popularMovieResponse) {
                        handleSuccessfulMovieRequest(initialCallback, callback, previousPage, nextPage, popularMovieResponse);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mError.postValue(error);
                    }
                });
    }

    private void handleSuccessfulMovieRequest(LoadInitialCallback<Long, Movie> loadInitialCallback, LoadCallback<Long, Movie> callback, Long previousPage, Long nextPage, Response<MovieResponse> movieResponse) {
        if (movieResponse != null) {
            if (movieResponse.isSuccessful()) {
                MovieResponse responseBody = movieResponse.body();

                if (responseBody != null) {
                    mMovies.postValue(responseBody.getMovies());
                    if (loadInitialCallback != null) {
                        loadInitialCallback.onResult(responseBody.getMovies(), previousPage, nextPage);
                    } else {
                        callback.onResult(responseBody.getMovies(), nextPage);
                    }
                }
            }
        }
    }
}
