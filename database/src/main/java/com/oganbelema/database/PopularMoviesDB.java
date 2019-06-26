package com.oganbelema.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.oganbelema.database.dao.FavoriteMovieDao;
import com.oganbelema.database.dao.MovieReviewDao;
import com.oganbelema.database.dao.MovieTrailerDao;
import com.oganbelema.database.entity.FavoriteMovieEntity;
import com.oganbelema.database.entity.MovieReviewEntity;
import com.oganbelema.database.entity.MovieTrailerEntity;

@Database(entities = {FavoriteMovieEntity.class, MovieReviewEntity.class, MovieTrailerEntity.class},
        version = 1, exportSchema = false)
public abstract class PopularMoviesDB extends RoomDatabase {

    public abstract FavoriteMovieDao getFavoriteMovieDao();

    public abstract MovieReviewDao getMovieReviewDao();

    public abstract MovieTrailerDao getMovieTrailerDao();
}
