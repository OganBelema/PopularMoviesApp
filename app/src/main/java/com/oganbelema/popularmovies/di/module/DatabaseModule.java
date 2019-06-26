package com.oganbelema.popularmovies.di.module;

import android.content.Context;

import androidx.room.Room;

import com.oganbelema.database.PopularMoviesDB;
import com.oganbelema.database.entity.FavoriteMovieEntity;
import com.oganbelema.database.mapper.FavoriteMovieEntityMapper;
import com.oganbelema.popularmovies.mapper.FavoriteMovieEntityMapperImpl;
import com.oganbelema.popularmovies.remote.model.movie.Movie;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    PopularMoviesDB providePopularMoviesDB(Context context){
        return Room.databaseBuilder(context, PopularMoviesDB.class, "popular_movies.db")
                .build();
    }

    @Provides
    @Singleton
    FavoriteMovieEntityMapper<FavoriteMovieEntity, Movie> provideFavoriteMovieEntityMapper(){
        return new FavoriteMovieEntityMapperImpl();
    }

}
