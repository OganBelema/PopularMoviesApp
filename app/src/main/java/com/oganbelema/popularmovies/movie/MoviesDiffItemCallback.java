package com.oganbelema.popularmovies.movie;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.oganbelema.network.model.movie.Movie;

public class MoviesDiffItemCallback extends DiffUtil.ItemCallback<Movie> {

    @Override
    public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
        return oldItem.equals(newItem);
    }
}
