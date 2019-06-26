package com.oganbelema.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.oganbelema.database.entity.MovieTrailerEntity;

import java.util.List;

import io.reactivex.Flowable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieTrailerDao {

    @Insert(onConflict = REPLACE)
    void insertMovieTrailers(List<MovieTrailerEntity> movieTrailers);

    @Query("SELECT * FROM movie_trailer WHERE movie_id = :movieId")
    Flowable<List<MovieTrailerEntity>> getMovieTrailers(int movieId);
}
