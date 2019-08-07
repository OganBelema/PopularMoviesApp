package com.oganbelema.popularmovies.movie;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private List<Movie> mMovies = new ArrayList<>(0);

    private Disposable mDisposable;

    private Boolean diifUtilIsOperating = false;

    private MovieItemOnClickListener mMovieItemOnClickListener;

    public void setMovieItemOnClickListener(MovieItemOnClickListener movieItemOnClickListener) {
        mMovieItemOnClickListener = movieItemOnClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,  parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindData(mMovies.get(position), mMovieItemOnClickListener);
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

}
