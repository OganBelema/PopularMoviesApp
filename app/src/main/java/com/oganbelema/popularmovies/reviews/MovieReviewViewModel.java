package com.oganbelema.popularmovies.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.oganbelema.network.model.review.Review;

import java.util.List;

public class MovieReviewViewModel extends ViewModel {

    private final MovieReviewsRepository mMovieReviewsRepository;

    private final MovieReviewAdapter mMovieReviewAdapter;

    public MovieReviewViewModel(MovieReviewsRepository movieReviewsRepository, MovieReviewAdapter
                                movieReviewAdapter) {
        this.mMovieReviewsRepository = movieReviewsRepository;
        this.mMovieReviewAdapter = movieReviewAdapter;
    }

    public Boolean getNetworkStatus(){
        return mMovieReviewsRepository.getNetworkStatus().getValue();
    }

    public MovieReviewAdapter getMovieReviewAdapter(){
        return mMovieReviewAdapter;
    }

    public LiveData<List<Review>> getMovieReviews(int movieId){
        return mMovieReviewsRepository.getMovieReviews(movieId);
    }

    public LiveData<Throwable> getError(){
        return mMovieReviewsRepository.getError();
    }

    @Override
    protected void onCleared() {
        mMovieReviewsRepository.dispose();
        super.onCleared();
    }
}
