package com.oganbelema.popularmovies.movie.movielist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.oganbelema.popularmovies.movie.MovieAdapter;
import com.oganbelema.popularmovies.movie.MovieViewModelFactory;
import com.oganbelema.popularmovies.R;
import com.oganbelema.popularmovies.movie.Movie;
import com.oganbelema.popularmovies.movie.MovieRepository;
import com.oganbelema.popularmovies.movie.MovieViewModel;
import com.oganbelema.popularmovies.movie.MovieResponse;
import com.oganbelema.popularmovies.movie.moviedetail.MovieDetailActivity;
import com.oganbelema.popularmovies.network.NetworkCallResult;

import java.util.List;

import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.Listener {

    private final String TAG = MovieListActivity.class.getSimpleName();

    private static final int GRID_SPAN = 2;

    private final MovieRepository mMovieRepository = new MovieRepository();

    private final MovieViewModelFactory mMovieViewModelFactory =
            new MovieViewModelFactory(mMovieRepository);

    private MovieViewModel mMovieViewModel;

    private Group mLoadingIndicatorViews;

    private RecyclerView mMoviesRecyclerView;

    private MovieAdapter mMovieAdapter;

    private boolean mPopularMovies = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies_list);

        mLoadingIndicatorViews = findViewById(R.id.loaderViews);

        mMoviesRecyclerView = findViewById(R.id.moviesRecyclerView);

        mMovieViewModel = ViewModelProviders.of(this, mMovieViewModelFactory)
                .get(MovieViewModel.class);

        mMovieAdapter = new MovieAdapter(this);

        mMoviesRecyclerView.setLayoutManager(new GridLayoutManager(this, GRID_SPAN));

        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        filterToPopularMovies();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter){
            if (mPopularMovies){
                filterToTopRatedMovies();
            } else {
                filterToPopularMovies();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void filterToTopRatedMovies() {
        mPopularMovies = false;
        setTitle(R.string.top_rated_movies);
        getTopRatedMovies();
    }

    private void filterToPopularMovies() {
        mPopularMovies = true;
        setTitle(R.string.popular_movies);
        getPopularMovies();
    }

    private void getPopularMovies(){
        showLoaderView();

        mMovieViewModel.getPopularMovies().observe(this,
                new Observer<NetworkCallResult<MovieResponse>>() {
            @Override
            public void onChanged(NetworkCallResult<MovieResponse>
                                          popularMovieResponseNetworkCallResult) {
                if (popularMovieResponseNetworkCallResult != null){

                    processMovieNetworkCallResult(popularMovieResponseNetworkCallResult);
                }
            }
        });
    }

    private void getTopRatedMovies(){
        showLoaderView();

        mMovieViewModel.getTopRatedMovies().observe(this, new Observer<NetworkCallResult<MovieResponse>>() {
            @Override
            public void onChanged(NetworkCallResult<MovieResponse> movieResponseNetworkCallResult) {
                if (movieResponseNetworkCallResult != null){
                    processMovieNetworkCallResult(movieResponseNetworkCallResult);
                }
            }
        });
    }

    private void processMovieNetworkCallResult(NetworkCallResult<MovieResponse> popularMovieResponseNetworkCallResult) {
        showMoviesView();

        Response<MovieResponse> response =
                popularMovieResponseNetworkCallResult.getResponse();

        Throwable error = popularMovieResponseNetworkCallResult.getError();

        if (response != null){
            if (response.isSuccessful()){
                MovieResponse movieResponse = response.body();

                if (movieResponse != null){
                    List<Movie> popularMovies = movieResponse.getMovies();

                    mMovieAdapter.setMovies(popularMovies);
                }
            }

        }

        if (error != null){
            Snackbar.make(mMoviesRecyclerView, getString(R.string.network_error_message),
                    Snackbar.LENGTH_LONG).show();
            Log.e(TAG, "Error fetching data", error);
        }
    }

    private void showLoaderView() {
        mMoviesRecyclerView.setVisibility(View.GONE);
        mLoadingIndicatorViews.setVisibility(View.VISIBLE);
    }

    private void showMoviesView() {
        mLoadingIndicatorViews.setVisibility(View.GONE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieItemClicked(Movie movie) {
        Intent startMovieDetailActivityIntent =
                new Intent(this, MovieDetailActivity.class);
        startMovieDetailActivityIntent.putExtra(MovieDetailActivity.MOVIE_TITLE, movie.getTitle());
        startMovieDetailActivityIntent.putExtra(MovieDetailActivity.RELEASE_DATE,
                movie.getReleaseDate());
        startMovieDetailActivityIntent.putExtra(MovieDetailActivity.MOVIE_POSTER,
                movie.getPosterPath());
        startMovieDetailActivityIntent.putExtra(MovieDetailActivity.VOTE_AVERAGE,
                movie.getVoteAverage());
        startMovieDetailActivityIntent.putExtra(MovieDetailActivity.PLOT_SYNOPSIS,
                movie.getOverview());
        startActivity(startMovieDetailActivityIntent);

    }

    @Override
    protected void onDestroy() {
        mMovieAdapter.dispose();
        super.onDestroy();
    }
}
