package com.oganbelema.popularmovies.trailer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.oganbelema.network.model.trailer.Trailer;
import com.oganbelema.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieTrailerAdapter extends
        RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder>{

    private List<Trailer> mTrailers = new ArrayList<>(0);

    private Disposable mDisposable;

    private Boolean diifUtilIsOperating = false;

    public interface TrailerItemOnClickListener {
        void onTrailerItemClicked(Trailer trailer);
    }

    private TrailerItemOnClickListener mTrailerItemOnClickListener;

    public void setMovieItemOnClickListener(TrailerItemOnClickListener trailerItemOnClickListener) {
        mTrailerItemOnClickListener = trailerItemOnClickListener;
    }

    @NonNull
    @Override
    public MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieTrailerViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.trailer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerViewHolder holder, int position) {
        holder.bindData(mTrailers.get(position));
    }

    @Override
    public int getItemCount() {
        if (mTrailers != null){
            return mTrailers.size();
        }

        return 0;
    }

    public void setTrailers(@NonNull final List<Trailer> trailers){

        if (diifUtilIsOperating) return;

        diifUtilIsOperating = true;

        mDisposable = Observable.fromCallable(() -> DiffUtil
                .calculateDiff(new TrailersDiffCallback(mTrailers, trailers), false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diffResult -> {
                    diffResult.dispatchUpdatesTo(MovieTrailerAdapter.this);
                    mTrailers =  trailers;
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

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trailerNameTextView)
        public TextView mTrailerNameTextView;

        public MovieTrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(Trailer trailer){
            mTrailerNameTextView.setText(trailer.getName());

            this.itemView.setOnClickListener(view ->
                    mTrailerItemOnClickListener.onTrailerItemClicked(trailer));
        }
    }
}
