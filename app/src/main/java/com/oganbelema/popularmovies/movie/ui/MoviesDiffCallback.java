package com.oganbelema.popularmovies.movie.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.oganbelema.popularmovies.movie.model.Movie;

import java.util.List;

public class MoviesDiffCallback extends DiffUtil.Callback {

    @Nullable
    private final List<Movie> oldMovieList;

    @NonNull
    private final List<Movie> newMovieList;

    public MoviesDiffCallback(@Nullable List<Movie> oldMovieList, @NonNull List<Movie> newMovieList) {
        this.oldMovieList = oldMovieList;
        this.newMovieList = newMovieList;
    }

    @Override
    public int getOldListSize() {
        if (oldMovieList != null){
            return oldMovieList.size();
        }

        return 0;
    }

    @Override
    public int getNewListSize() {
        if (newMovieList != null){
            return newMovieList.size();
        }

        return 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        if (oldMovieList != null && newMovieList != null){
            return oldMovieList.get(oldItemPosition).getId()
                    .equals(newMovieList.get(newItemPosition).getId());
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        if (oldMovieList != null && newMovieList != null){
            return oldMovieList.get(oldItemPosition).equals(newMovieList.get(newItemPosition));
        }
        return false;
    }
}
