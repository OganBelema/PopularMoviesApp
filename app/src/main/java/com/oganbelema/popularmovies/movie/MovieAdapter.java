package com.oganbelema.popularmovies.movie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.oganbelema.popularmovies.R;
import com.oganbelema.popularmovies.network.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> mMovies = new ArrayList<>(0);

    private Disposable mDisposable;

    public interface Listener {
        void onMovieItemClicked(Movie movie);
    }

    private final Listener mListener;

    public MovieAdapter(Listener listener) {
        mListener = listener;
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

        mDisposable = Observable.fromCallable(new Callable<DiffUtil.DiffResult>() {
            @Override
            public DiffUtil.DiffResult call() {
                return DiffUtil
                        .calculateDiff(new MoviesDiffCallback(mMovies, movies), false);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DiffUtil.DiffResult>() {
                    @Override
                    public void accept(DiffUtil.DiffResult diffResult) {
                        mMovies = (ArrayList<Movie>) movies;
                        diffResult.dispatchUpdatesTo(MovieAdapter.this);
                    }
                });

    }

    public void dispose() {
        if (mDisposable != null){
            mDisposable.dispose();
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mMoviePosterImageView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mMoviePosterImageView = itemView.findViewById(R.id.moviePosterImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMovieItemClicked(mMovies.get(getAdapterPosition()));
                }
            });
        }

        void bindData(final Movie movie){
            Picasso.get().load(ServiceGenerator.IMAGE_URL + movie.getPosterPath())
                    .error(R.drawable.ic_error_24dp)
                    .into(mMoviePosterImageView);
        }
    }

}
