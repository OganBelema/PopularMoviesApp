package com.oganbelema.popularmovies.reviews;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieReviewViewModelFactory implements ViewModelProvider.Factory {

    private final MovieReviewsRepository mMovieReviewsRepository;

    private final MovieReviewAdapter mMovieReviewAdapter;

    @Inject
    public MovieReviewViewModelFactory(MovieReviewsRepository movieReviewsRepository,
                                       MovieReviewAdapter movieReviewAdapter) {
        this.mMovieReviewsRepository = movieReviewsRepository;
        this.mMovieReviewAdapter = movieReviewAdapter;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieReviewViewModel(mMovieReviewsRepository, mMovieReviewAdapter);
    }
}
