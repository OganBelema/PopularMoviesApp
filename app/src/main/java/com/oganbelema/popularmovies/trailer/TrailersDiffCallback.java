package com.oganbelema.popularmovies.trailer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.oganbelema.network.model.trailer.Trailer;

import java.util.List;

public class TrailersDiffCallback extends DiffUtil.Callback {

    @Nullable
    private final List<Trailer> oldTrailerList;

    @NonNull
    private final List<Trailer> newTrailerList;

    public TrailersDiffCallback(@Nullable List<Trailer> oldTrailerList,
                               @NonNull List<Trailer> newTrailerList) {
        this.oldTrailerList = oldTrailerList;
        this.newTrailerList = newTrailerList;
    }

    @Override
    public int getOldListSize() {
        if (oldTrailerList != null){
            return oldTrailerList.size();
        }

        return 0;
    }

    @Override
    public int getNewListSize() {
        return newTrailerList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldTrailerList != null && newTrailerList != null){
            return oldTrailerList.get(oldItemPosition).getId()
                    .equals(newTrailerList.get(newItemPosition).getId());
        }

        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldTrailerList != null && newTrailerList != null){
            return oldTrailerList.get(oldItemPosition).equals(newTrailerList.get(newItemPosition));
        }

        return false;
    }
}
