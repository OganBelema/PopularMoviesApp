package com.oganbelema.popularmovies.movie;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.oganbelema.network.model.movie.Movie;


public class MovieAdapter extends ListAdapter<Movie, MovieViewHolder> {

    private MovieItemOnClickListener mMovieItemOnClickListener;

    public MovieAdapter(MoviesDiffItemCallback moviesDiffItemCallback) {
        super(moviesDiffItemCallback);
    }

    public void setMovieItemOnClickListener(MovieItemOnClickListener movieItemOnClickListener) {
        mMovieItemOnClickListener = movieItemOnClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MovieViewHolder.getInstance(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindData(getItem(position), mMovieItemOnClickListener);
    }

}
