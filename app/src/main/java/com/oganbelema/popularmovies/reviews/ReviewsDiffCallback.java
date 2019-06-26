package com.oganbelema.popularmovies.reviews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.oganbelema.network.model.review.Review;

import java.util.List;

public class ReviewsDiffCallback extends DiffUtil.Callback {

    @Nullable
    private final List<Review> oldReviewList;

    @NonNull
    private final List<Review> newReviewList;

    public ReviewsDiffCallback(@Nullable List<Review> oldReviewList,
                               @NonNull List<Review> newReviewList) {
        this.oldReviewList = oldReviewList;
        this.newReviewList = newReviewList;
    }

    @Override
    public int getOldListSize() {
        if (oldReviewList != null){
            return oldReviewList.size();
        }

        return 0;
    }

    @Override
    public int getNewListSize() {
        return newReviewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldReviewList != null && newReviewList != null){
            return oldReviewList.get(oldItemPosition).getId()
                    .equals(newReviewList.get(newItemPosition).getId());
        }

        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldReviewList != null && newReviewList != null){
            return oldReviewList.get(oldItemPosition).equals(newReviewList.get(newItemPosition));
        }

        return false;
    }
}
