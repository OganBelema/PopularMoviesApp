package com.oganbelema.popularmovies.movie;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.popularmovies.Constants;
import com.oganbelema.popularmovies.R;
import com.oganbelema.popularmovies.databinding.MovieItemBinding;
import com.squareup.picasso.Picasso;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private final MovieItemBinding mMovieItemBinding;

    MovieViewHolder(@NonNull MovieItemBinding movieItemBinding) {
        super(movieItemBinding.getRoot());
        mMovieItemBinding = movieItemBinding;
    }

    void bindData(final Movie movie, MovieItemOnClickListener movieItemOnClickListener){
        if (movie != null && movieItemOnClickListener != null) {
            Picasso.get().load(Constants.IMAGE_URL + movie.getPosterPath())
                    .error(R.drawable.ic_error_24dp)
                    .placeholder(R.drawable.loading_anim)
                    .into(mMovieItemBinding.moviePosterImageView);

            itemView.setOnClickListener(v ->
                    movieItemOnClickListener.onMovieItemClicked(movie));
        }
    }
}
