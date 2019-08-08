package com.oganbelema.popularmovies.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.snackbar.Snackbar;
import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.network.model.review.Review;
import com.oganbelema.network.model.trailer.Trailer;
import com.oganbelema.popularmovies.Constants;
import com.oganbelema.popularmovies.PopularMoviesApp;
import com.oganbelema.popularmovies.R;
import com.oganbelema.popularmovies.databinding.ActivityMovieDetailBinding;
import com.oganbelema.popularmovies.reviews.MovieReviewViewModel;
import com.oganbelema.popularmovies.reviews.MovieReviewViewModelFactory;
import com.oganbelema.popularmovies.service.FavoriteService;
import com.oganbelema.popularmovies.service.ReviewService;
import com.oganbelema.popularmovies.service.TrailerService;
import com.oganbelema.popularmovies.trailer.MovieTrailerAdapter;
import com.oganbelema.popularmovies.trailer.MovieTrailerViewModel;
import com.oganbelema.popularmovies.trailer.MovieTrailerViewModelFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class MovieDetailActivity extends AppCompatActivity implements MovieTrailerAdapter.TrailerItemOnClickListener {

    private final String TAG = MovieDetailActivity.class.getSimpleName();

    public static final String MOVIE = "movie";

    @Inject @Named(Constants.NAMED_IMAGE_URL)
    public String imageUrl;

    @Inject
    public MovieReviewViewModelFactory mMovieReviewViewModelFactory;

    @Inject
    public MovieTrailerViewModelFactory mMovieTrailerViewModelFactory;

    private MovieReviewViewModel mMovieReviewViewModel;

    private MovieTrailerViewModel mMovieTrailerViewModel;

    private Movie mMovie;

    private List<Review> mReviews;

    private List<Trailer> mTrailers;
    private ActivityMovieDetailBinding mActivityMovieDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMovieDetailBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_movie_detail);

        ((PopularMoviesApp) getApplication()).getAppComponent().inject(this);

        Intent movieIntent = getIntent();

        if (movieIntent != null){
            mMovie = movieIntent.getParcelableExtra(MOVIE);

            displayDataOnView(mMovie);
        }

        mMovieReviewViewModel = ViewModelProviders.of(this, mMovieReviewViewModelFactory)
                .get(MovieReviewViewModel.class);

        mMovieTrailerViewModel = ViewModelProviders.of(this, mMovieTrailerViewModelFactory)
                .get(MovieTrailerViewModel.class);

        mMovieReviewViewModel.getMovieReviews(mMovie.getId()).observe(this, reviews -> {
            if (reviews != null){

                mReviews = reviews;

                mMovieReviewViewModel.getMovieReviewAdapter().setReviews(reviews);

                if (!mMovieReviewViewModel.getNetworkStatus()){
                    showMessageWithSnackbar(R.string.offline_movie_review);
                }
            }
        });

        mMovieTrailerViewModel.getMovieTrailers(mMovie.getId()).observe(this, trailers -> {
            if (trailers != null){

                mTrailers = trailers;

                mMovieTrailerViewModel.getMovieTrailerAdapter().setTrailers(trailers);

                if (!mMovieTrailerViewModel.getNetworkStatus()){
                    showMessageWithSnackbar(R.string.offline_movie_trailers);
                }
            }
        });

        mMovieReviewViewModel.getError().observe(this, error -> {
            if (error != null){
                Log.e(TAG, error.getLocalizedMessage(), error);
                showMessageWithSnackbar(R.string.fetching_reviews_error);
            }
        });

        mMovieTrailerViewModel.getError().observe(this, error -> {
            if (error != null){
                Log.e(TAG, error.getLocalizedMessage(), error);
                showMessageWithSnackbar(R.string.fetching_trailer_error);
            }
        });

        setUpMovieReviewRecyclerView();

        setupMovieTrailerRecyclerView();

    }

    private void setupMovieTrailerRecyclerView() {
        mActivityMovieDetailBinding.trailersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mActivityMovieDetailBinding.trailersRecyclerView.setAdapter(mMovieTrailerViewModel.getMovieTrailerAdapter());
        mMovieTrailerViewModel.getMovieTrailerAdapter().setMovieItemOnClickListener(this);
    }

    private void setUpMovieReviewRecyclerView() {
        mActivityMovieDetailBinding.reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mActivityMovieDetailBinding.reviewsRecyclerView.setAdapter(mMovieReviewViewModel.getMovieReviewAdapter());
    }

    private void showMessageWithSnackbar(int stringResourceId) {
        Snackbar.make(mActivityMovieDetailBinding.getRoot(), getString(stringResourceId),
                Snackbar.LENGTH_LONG).show();
    }

    private void displayDataOnView(@Nullable Movie movie) {
        if (movie != null){
            setTitle(movie.getTitle());

            Picasso.get().load(imageUrl  + movie.getPosterPath())
                    .into(mActivityMovieDetailBinding.moviePosterImageView);

            mActivityMovieDetailBinding.movieTitleTextView.setText(movie.getTitle());
            mActivityMovieDetailBinding.releaseDateTextView.setText(getString(R.string.release_date,
                    movie.getReleaseDate()));
            mActivityMovieDetailBinding.voteAverageTextView.setText(getString(R.string.vote_average, movie.getVoteAverage()));
            mActivityMovieDetailBinding.movieOverviewTextView.setText(movie.getOverview());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_favorite:
                favoriteAction();
                return true;

            case R.id.action_un_favorite:
                if (mMovie != null){
                    unFavoriteMovie(mMovie);
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void favoriteAction() {
        if (mMovie != null){
            favoriteMovie(mMovie);
        }

        if (mReviews != null){
            saveReviews(mReviews);
        }

        if (mTrailers != null){
            saveTrailers(mTrailers);
        }
    }

    private void favoriteMovie(Movie movie){
        Intent favoriteMovieIntent = new Intent(this, FavoriteService.class);
        favoriteMovieIntent.putExtra(FavoriteService.MOVIE_KEY, movie);
        favoriteMovieIntent.setAction(FavoriteService.FAVORITE_ACTION);
        startService(favoriteMovieIntent);
    }

    private void unFavoriteMovie(Movie movie){
        Intent favoriteMovieIntent = new Intent(this, FavoriteService.class);
        favoriteMovieIntent.putExtra(FavoriteService.MOVIE_KEY, movie);
        favoriteMovieIntent.setAction(FavoriteService.UN_FAVORITE_ACTION);
        startService(favoriteMovieIntent);
    }

    private void saveReviews(List<Review> reviews){
        ArrayList<Review> reviewArrayList = new ArrayList<>(reviews);
        Intent reviewIntent = new Intent(this, ReviewService.class);
        reviewIntent.putParcelableArrayListExtra(ReviewService.REVIEW_KEY, reviewArrayList);
        startService(reviewIntent);
    }

    private void saveTrailers(List<Trailer> trailers){
        ArrayList<Trailer> trailerArrayList = new ArrayList<>(trailers);
        Intent trailerIntent = new Intent(this, TrailerService.class);
        trailerIntent.putParcelableArrayListExtra(TrailerService.TRAILERS_KEY, trailerArrayList);
        startService(trailerIntent);
    }

    @Override
    public void onTrailerItemClicked(Trailer trailer) {
        Intent watchTrailerIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
        startActivity(watchTrailerIntent);
    }
}
