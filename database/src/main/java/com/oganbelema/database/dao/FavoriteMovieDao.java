package com.oganbelema.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.oganbelema.database.entity.FavoriteMovieEntity;

import java.util.List;

import io.reactivex.Flowable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface FavoriteMovieDao {

    @Insert(onConflict = REPLACE)
    void insert(FavoriteMovieEntity favoriteMovieEntity);

    @Query("SELECT * FROM favourite_movies")
    Flowable<List<FavoriteMovieEntity>> getFavoriteMovies();

    @Delete
    void delete(FavoriteMovieEntity favoriteMovieEntity);
}
