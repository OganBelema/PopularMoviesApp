package com.oganbelema.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.oganbelema.database.entity.MovieReviewEntity;

import java.util.List;

import io.reactivex.Flowable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieReviewDao {

    @Insert(onConflict = REPLACE)
    void insertMovieReviews(List<MovieReviewEntity> reviews);

    @Query("SELECT * FROM movie_review WHERE movie_id = :movieId")
    Flowable<List<MovieReviewEntity>> getReviews(int movieId);

}
