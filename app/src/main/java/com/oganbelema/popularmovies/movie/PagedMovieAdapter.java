package com.oganbelema.popularmovies.movie;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.oganbelema.network.model.movie.Movie;

public class PagedMovieAdapter extends PagedListAdapter<Movie, MovieViewHolder> {

    private MovieItemOnClickListener mMovieItemOnClickListener;

    public PagedMovieAdapter(@NonNull DiffUtil.ItemCallback<Movie> diffCallback) {
        super(diffCallback);
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
