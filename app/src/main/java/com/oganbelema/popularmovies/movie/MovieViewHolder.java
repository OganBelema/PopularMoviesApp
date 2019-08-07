package com.oganbelema.popularmovies.movie;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.popularmovies.Constants;
import com.oganbelema.popularmovies.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.moviePosterImageView)
    ImageView mMoviePosterImageView;

    MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bindData(final Movie movie, MovieItemOnClickListener movieItemOnClickListener){
        if (movie != null && movieItemOnClickListener != null) {
            Picasso.get().load(Constants.IMAGE_URL + movie.getPosterPath())
                    .error(R.drawable.ic_error_24dp)
                    .into(mMoviePosterImageView);

            itemView.setOnClickListener(v ->
                    movieItemOnClickListener.onMovieItemClicked(movie));
        }
    }
}
