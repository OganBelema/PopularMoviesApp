package com.oganbelema.network.source;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oganbelema.network.MoviesApi;
import com.oganbelema.network.model.trailer.Trailer;
import com.oganbelema.network.model.trailer.TrailerResponse;

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
public class MovieTrailersNetworkSource {

    private final MoviesApi mMoviesApi;

    private final MutableLiveData<List<Trailer>> mTrailers = new MutableLiveData<>();

    private final MutableLiveData<Throwable> mError = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MovieTrailersNetworkSource(MoviesApi mMoviesApi) {
        this.mMoviesApi = mMoviesApi;
    }

    public LiveData<List<Trailer>> getTrailers(int movieId) {
        mMoviesApi.getMovieTrailers(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<TrailerResponse>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        disposables.add(disposable);
                    }

                    @Override
                    public void onSuccess(Response<TrailerResponse> trailerResponse) {
                        handleSuccessfulRequest(trailerResponse);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mError.postValue(error);
                    }
                });
        return mTrailers;
    }

    private void handleSuccessfulRequest(Response<TrailerResponse> trailerResponse) {
        if (trailerResponse != null) {
            if (trailerResponse.isSuccessful()) {
                TrailerResponse trailerResponseBody = trailerResponse.body();

                if (trailerResponseBody != null) {
                    mTrailers.postValue(trailerResponseBody.getResults());
                }
            }
        }
    }

    public LiveData<Throwable> getError() {
        return mError;
    }

    public void dispose(){
        disposables.dispose();
    }
}
