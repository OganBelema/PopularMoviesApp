package com.oganbelema.popularmovies.movie.moviedetail;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.oganbelema.popularmovies.R;
import com.oganbelema.popularmovies.movie.Movie;
import com.oganbelema.popularmovies.network.ServiceGenerator;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE = "movie";

    @BindView(R.id.moviePosterImageView)
    ImageView mMoviePosterImageView;

    @BindView(R.id.movieTitleTextView)
    TextView mMovieTitleTextView;

    @BindView(R.id.releaseDateTextView)
    TextView mMovieReleaseDateTextView;

    @BindView(R.id.voteAverageTextView)
    TextView mVoteAverageTextView;

    @BindView(R.id.movieOverviewTextView)
    TextView mMovieOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent movieIntent = getIntent();

        if (movieIntent != null){
            Movie movie = movieIntent.getParcelableExtra(MOVIE);

            displayDataOnView(movie);
        }
    }

    private void displayDataOnView(@Nullable Movie movie) {
        if (movie != null){
            setTitle(movie.getTitle());

            Picasso.get().load(ServiceGenerator.IMAGE_URL  + movie.getPosterPath())
                    .into(mMoviePosterImageView);
            mMovieTitleTextView.setText(movie.getTitle());
            mMovieReleaseDateTextView.setText(getString(R.string.release_date, movie.getReleaseDate()));
            mVoteAverageTextView.setText(getString(R.string.vote_average, movie.getVoteAverage()));
            mMovieOverviewTextView.setText(movie.getOverview());
        }
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
