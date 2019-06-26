package com.oganbelema.network.source;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.oganbelema.network.MoviesApi;
import com.oganbelema.network.model.review.Review;
import com.oganbelema.network.model.review.ReviewResponse;

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
public class MovieReviewNetworkSource {

    private final MoviesApi mMoviesApi;

    private final MutableLiveData<List<Review>> mMovieReviews = new MutableLiveData<>();

    private final MutableLiveData<Throwable> mError = new MutableLiveData<>();

    private final CompositeDisposable mDisposables = new CompositeDisposable();

    @Inject
    public MovieReviewNetworkSource(MoviesApi mMoviesApi) {
        this.mMoviesApi = mMoviesApi;
    }

    public LiveData<List<Review>> getReviewsRemote(int movieId){
        mMoviesApi.getMovieReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<ReviewResponse>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mDisposables.add(disposable);
                    }

                    @Override
                    public void onSuccess(Response<ReviewResponse> reviewResponse) {
                        handleRequestSuccess(reviewResponse);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mError.postValue(error);
                    }
                });
        return mMovieReviews;
    }

    private void handleRequestSuccess(Response<ReviewResponse> reviewResponse) {
        if (reviewResponse != null){
            if (reviewResponse.isSuccessful()){
                ReviewResponse reviewResponseBody = reviewResponse.body();

                if (reviewResponseBody != null){
                    mMovieReviews.postValue(reviewResponseBody.getResults());
                }
            }
        }
    }

    public MutableLiveData<Throwable> getError() {
        return mError;
    }
}
