package com.oganbelema.popularmovies.movie.moviedetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.oganbelema.popularmovies.R;
import com.oganbelema.popularmovies.network.ServiceGenerator;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_TITLE = "movie_title";
    public static final String RELEASE_DATE = "movie_release_date";
    public static final String MOVIE_POSTER = "movie_poster";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String PLOT_SYNOPSIS = "plot_synopsis";

    private ImageView mMoviePosterImageView;
    private TextView mMovieTitleTextView;
    private TextView mMovieReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mMovieOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMoviePosterImageView = findViewById(R.id.moviePosterImageView);
        mMovieTitleTextView = findViewById(R.id.movieTitleTextView);
        mMovieReleaseDateTextView = findViewById(R.id.releaseDateTextView);
        mVoteAverageTextView = findViewById(R.id.voteAverageTextView);
        mMovieOverviewTextView = findViewById(R.id.movieOverviewTextView);

        Intent movieIntent = getIntent();

        if (movieIntent != null){
            String movieTitle = movieIntent.getStringExtra(MOVIE_TITLE);
            String moviePosterPath = movieIntent.getStringExtra(MOVIE_POSTER);
            String releaseDate = movieIntent.getStringExtra(RELEASE_DATE);
            double voteAverage = movieIntent.getDoubleExtra(VOTE_AVERAGE, 0);
            String movieOverview = movieIntent.getStringExtra(PLOT_SYNOPSIS);

            displayDataOnView(movieTitle, moviePosterPath, releaseDate, voteAverage, movieOverview);
        }
    }

    private void displayDataOnView(String movieTitle, String moviePosterPath, String releaseDate,
                                   double voteAverage, String movieOverview) {
        setTitle(movieTitle);

        Picasso.get().load(ServiceGenerator.IMAGE_URL  + moviePosterPath)
                .into(mMoviePosterImageView);
        mMovieTitleTextView.setText(movieTitle);
        mMovieReleaseDateTextView.setText(getString(R.string.release_date, releaseDate));
        mVoteAverageTextView.setText(getString(R.string.vote_average, voteAverage));
        mMovieOverviewTextView.setText(movieOverview);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
