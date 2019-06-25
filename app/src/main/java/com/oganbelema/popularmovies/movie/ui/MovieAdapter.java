package com.oganbelema.popularmovies.movie.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.oganbelema.popularmovies.Constants;
import com.oganbelema.popularmovies.R;
import com.oganbelema.popularmovies.movie.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> mMovies = new ArrayList<>(0);

    private Disposable mDisposable;

    private Boolean diifUtilIsOperating = false;

    public interface MovieItemOnClickListener {
        void onMovieItemClicked(Movie movie);
    }

    private final MovieItemOnClickListener mMovieItemOnClickListener;

    public MovieAdapter(MovieItemOnClickListener movieItemOnClickListener) {
        mMovieItemOnClickListener = movieItemOnClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,  parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindData(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMovies != null){
            return mMovies.size();
        }
        return 0;
    }

    public void setMovies(@NonNull final List<Movie> movies){

        if (diifUtilIsOperating) return;

        diifUtilIsOperating = true;

        mDisposable = Observable.fromCallable(() -> DiffUtil
                .calculateDiff(new MoviesDiffCallback(mMovies, movies), false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diffResult -> {
                    diffResult.dispatchUpdatesTo(MovieAdapter.this);
                    mMovies =  movies;
                    diifUtilIsOperating = false;
                });

    }

    private void dispose() {
        if (mDisposable != null){
            mDisposable.dispose();
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        dispose();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.moviePosterImageView)
        ImageView mMoviePosterImageView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v ->
                    mMovieItemOnClickListener.onMovieItemClicked(mMovies.get(getAdapterPosition())));
        }

        void bindData(final Movie movie){
            Picasso.get().load(Constants.IMAGE_URL + movie.getPosterPath())
                    .error(R.drawable.ic_error_24dp)
                    .into(mMoviePosterImageView);
        }
    }

}
