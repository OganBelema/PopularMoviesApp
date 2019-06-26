package com.oganbelema.popularmovies.reviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.oganbelema.network.model.review.Review;
import com.oganbelema.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieReviewAdapter extends
        RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {

    private List<Review> mReviews = new ArrayList<>(0);

    private Disposable mDisposable;

    private Boolean diifUtilIsOperating = false;


    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieReviewViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewViewHolder holder, int position) {
        holder.bindData(mReviews.get(position));
    }

    @Override
    public int getItemCount() {
        if (mReviews != null){
            return mReviews.size();
        }

        return 0;
    }

    public void setReviews(@NonNull final List<Review> reviews){

        if (diifUtilIsOperating) return;

        diifUtilIsOperating = true;

        mDisposable = Observable.fromCallable(() -> DiffUtil
                .calculateDiff(new ReviewsDiffCallback(mReviews, reviews), false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diffResult -> {
                    diffResult.dispatchUpdatesTo(MovieReviewAdapter.this);
                    mReviews =  reviews;
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

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reviewTextView)
        TextView reviewTextView;

        @BindView(R.id.reviewAuthorTextView)
        TextView reviewAuthor;

        public MovieReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(Review review){
            reviewTextView.setText(review.getContent());
            reviewAuthor.setText(review.getAuthor());
        }


    }
}
