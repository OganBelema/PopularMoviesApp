package com.oganbelema.popularmovies.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.oganbelema.network.model.review.Review;
import com.oganbelema.popularmovies.PopularMoviesApp;
import com.oganbelema.popularmovies.reviews.MovieReviewsRepository;

import java.util.List;

import javax.inject.Inject;

public class ReviewService extends Service {

    public static final String REVIEW_KEY = "reviews";

    @Inject
    MovieReviewsRepository mMovieReviewsRepository;

    @Override
    public void onCreate() {
        ((PopularMoviesApp) getApplication()).getAppComponent().inject(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<Review> reviews = intent.getParcelableArrayListExtra(REVIEW_KEY);
        mMovieReviewsRepository.saveMovieReviews(reviews);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
