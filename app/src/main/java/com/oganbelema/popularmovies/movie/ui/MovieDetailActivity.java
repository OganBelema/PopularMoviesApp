package com.oganbelema.popularmovies.movie.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.oganbelema.popularmovies.Constants;
import com.oganbelema.popularmovies.PopularMoviesApp;
import com.oganbelema.popularmovies.R;
import com.oganbelema.popularmovies.movie.model.Movie;
import com.oganbelema.popularmovies.movie.viewmodel.MovieDetailViewModel;
import com.oganbelema.popularmovies.movie.viewmodel.MovieDetailViewModelFactory;
import com.oganbelema.popularmovies.service.FavoriteService;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE = "movie";

    @Inject @Named(Constants.NAMED_IMAGE_URL)
    public String imageUrl;

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

    @Inject
    public MovieDetailViewModelFactory mMovieDetailViewModelFactory;

    private MovieDetailViewModel mMovieDetailViewModel;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        ((PopularMoviesApp) getApplication()).getAppComponent().inject(this);

        Intent movieIntent = getIntent();

        if (movieIntent != null){
            mMovie = movieIntent.getParcelableExtra(MOVIE);

            displayDataOnView(mMovie);
        }

        mMovieDetailViewModel = ViewModelProviders.of(this, mMovieDetailViewModelFactory)
                .get(MovieDetailViewModel.class);
    }

    private void displayDataOnView(@Nullable Movie movie) {
        if (movie != null){
            setTitle(movie.getTitle());

            Picasso.get().load(imageUrl  + movie.getPosterPath())
                    .into(mMoviePosterImageView);

            mMovieTitleTextView.setText(movie.getTitle());
            mMovieReleaseDateTextView.setText(getString(R.string.release_date, movie.getReleaseDate()));
            mVoteAverageTextView.setText(getString(R.string.vote_average, movie.getVoteAverage()));
            mMovieOverviewTextView.setText(movie.getOverview());
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
                if (mMovie != null){
                    favoriteMovie(mMovie);
                }
                return true;

            case R.id.action_un_favorite:
                if (mMovie != null){
                    unFavoriteMovie(mMovie);
                }

        }

        return super.onOptionsItemSelected(item);
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
}
