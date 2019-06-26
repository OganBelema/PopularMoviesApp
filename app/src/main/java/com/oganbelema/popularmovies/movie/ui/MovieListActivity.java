package com.oganbelema.popularmovies.movie.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.oganbelema.popularmovies.PopularMoviesApp;
import com.oganbelema.popularmovies.movie.FilterOptions;
import com.oganbelema.popularmovies.movie.viewmodel.MovieListViewModelFactory;
import com.oganbelema.popularmovies.R;
import com.oganbelema.popularmovies.remote.model.movie.Movie;
import com.oganbelema.popularmovies.movie.repository.MovieRepository;
import com.oganbelema.popularmovies.movie.viewmodel.MovieListViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.MovieItemOnClickListener {

    private final String TAG = MovieListActivity.class.getSimpleName();

    private static final int GRID_SPAN_PORTRAIT = 2;

    private static final int GRID_SPAN_HORIZONTAL = 3;

    @Inject
    public MovieRepository mMovieRepository;

    @Inject
    public MovieListViewModelFactory mMovieListViewModelFactory;

    private MovieListViewModel mMovieListViewModel;

    @BindView(R.id.loaderViews)
    Group mLoadingIndicatorViews;

    @BindView(R.id.errorViews)
    Group mErrorViews;

    @BindView(R.id.moviesRecyclerView)
    RecyclerView mMoviesRecyclerView;

    @BindView(R.id.errorTextView)
    TextView errorTextView;

    @BindView(R.id.noMoviesTextView)
    TextView mNoMoviesTextView;

    private MovieAdapter mMovieAdapter;

    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies_list);

        ButterKnife.bind(this);

        ((PopularMoviesApp) getApplication()).getAppComponent().inject(this);

        mMovieListViewModel = ViewModelProviders.of(this, mMovieListViewModelFactory)
                .get(MovieListViewModel.class);

        mMovieAdapter = mMovieListViewModel.getMovieAdapter();

        mMovieAdapter.setMovieItemOnClickListener(this);

        mGridLayoutManager = new GridLayoutManager(this, GRID_SPAN_PORTRAIT);

        mMoviesRecyclerView.setLayoutManager(mGridLayoutManager);

        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        mMovieListViewModel.getFilterOptions().observe(this, this::handleFilterOption);

        mMovieListViewModel.getError().observe(this, error -> {
            if (error != null){
                showErrorView();
                Log.e(TAG, error.getLocalizedMessage(), error);
            }
        });

        errorTextView.setOnClickListener(view -> {
            FilterOptions option = mMovieListViewModel.getRawFilterOption();

            if (option == null){
                option = FilterOptions.POPULAR_MOVIES;
            }

            handleFilterOption(option);
        });

        mMovieListViewModel.getNetworkStatus().observe(this, networkStatus -> {
            if (!networkStatus){
                filterToFavoriteMovies();
                Snackbar.make(mMoviesRecyclerView, getString(R.string.offline_message),
                        Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void handleFilterOption(FilterOptions filterOptions) {
        switch (filterOptions) {
            case POPULAR_MOVIES:
                filterToPopularMovies();
                break;
            case TOP_RATED_MOVIES:
                filterToTopRatedMovies();
                break;
            case FAVORITE_MOVIES:
                filterToFavoriteMovies();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridLayoutManager.setSpanCount(GRID_SPAN_HORIZONTAL);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridLayoutManager.setSpanCount(GRID_SPAN_PORTRAIT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                if (mMovieListViewModel.getFilterOptions().getValue() == FilterOptions.POPULAR_MOVIES) {
                    mMovieListViewModel.setFilterOption(FilterOptions.TOP_RATED_MOVIES);
                } else {
                    mMovieListViewModel.setFilterOption(FilterOptions.POPULAR_MOVIES);
                }
                return true;

            case R.id.action_filter_favorite:
                mMovieListViewModel.setFilterOption(FilterOptions.FAVORITE_MOVIES);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void filterToTopRatedMovies() {
        setTitle(R.string.top_rated_movies);
        getTopRatedMovies();
    }

    private void filterToPopularMovies() {
        setTitle(R.string.popular_movies);
        getPopularMovies();
    }

    private void filterToFavoriteMovies() {
        setTitle(R.string.favorite_movies);
        getFavoriteMovies();
    }

    private void getPopularMovies() {
        showLoaderView();

        mMovieListViewModel.getPopularMovies().observe(this, popularMovies -> {
                    if (popularMovies != null) {
                        displayMovies(popularMovies);
                    }
                });
    }

    private void getTopRatedMovies() {
        showLoaderView();

        mMovieListViewModel.getTopRatedMovies().observe(this, topRatedMovies -> {
            if (topRatedMovies != null) {
                displayMovies(topRatedMovies);
            }
        });
    }

    private void getFavoriteMovies(){
        showLoaderView();

        mMovieListViewModel.getFavoriteMovies().observe(this, movies -> {
            if (mMovieListViewModel.getRawFilterOption().equals(FilterOptions.FAVORITE_MOVIES)){
                displayMovies(movies);
            }
        });
    }

    private void displayMovies(List<Movie> movies) {
        if (movies != null && !movies.isEmpty()){
            showMoviesView();
            mMovieAdapter.setMovies(movies);
        } else {
            showNoMoviesView();
        }
    }

    private void showLoaderView() {
        mMoviesRecyclerView.setVisibility(View.GONE);
        mErrorViews.setVisibility(View.GONE);
        mNoMoviesTextView.setVisibility(View.GONE);
        mLoadingIndicatorViews.setVisibility(View.VISIBLE);
    }

    private void showMoviesView() {
        mErrorViews.setVisibility(View.GONE);
        mLoadingIndicatorViews.setVisibility(View.GONE);
        mNoMoviesTextView.setVisibility(View.GONE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorView(){
        mMoviesRecyclerView.setVisibility(View.GONE);
        mLoadingIndicatorViews.setVisibility(View.GONE);
        mNoMoviesTextView.setVisibility(View.GONE);
        mErrorViews.setVisibility(View.VISIBLE);

    }

    private void showNoMoviesView(){
        mMoviesRecyclerView.setVisibility(View.GONE);
        mLoadingIndicatorViews.setVisibility(View.GONE);
        mErrorViews.setVisibility(View.GONE);
        mNoMoviesTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieItemClicked(Movie movie) {
        Intent startMovieDetailActivityIntent =
                new Intent(this, MovieDetailActivity.class);
        startMovieDetailActivityIntent.putExtra(MovieDetailActivity.MOVIE, movie);
        startActivity(startMovieDetailActivityIntent);

    }

}
