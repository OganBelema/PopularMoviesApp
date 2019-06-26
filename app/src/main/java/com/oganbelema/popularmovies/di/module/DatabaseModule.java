package com.oganbelema.popularmovies.di.module;

import android.content.Context;

import androidx.room.Room;

import com.oganbelema.database.PopularMoviesDB;
import com.oganbelema.database.entity.FavoriteMovieEntity;
import com.oganbelema.database.entity.MovieReviewEntity;
import com.oganbelema.database.entity.MovieTrailerEntity;
import com.oganbelema.database.mapper.EntityMapper;
import com.oganbelema.network.model.movie.Movie;
import com.oganbelema.network.model.review.Review;
import com.oganbelema.network.model.trailer.Trailer;
import com.oganbelema.popularmovies.mapper.FavoriteMovieEntityMapperImpl;
import com.oganbelema.popularmovies.mapper.MovieReviewEntityMapperImpl;
import com.oganbelema.popularmovies.mapper.MovieTrailerEntityMapperImpl;

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
    EntityMapper<FavoriteMovieEntity, Movie> provideFavoriteMovieEntityMapper(){
        return new FavoriteMovieEntityMapperImpl();
    }

    @Provides
    @Singleton
    EntityMapper<MovieReviewEntity, Review> provideMovieReviewEntityMapper(){
        return new MovieReviewEntityMapperImpl();
    }

    @Provides
    @Singleton
    EntityMapper<MovieTrailerEntity, Trailer> provideMovieTrailerEntityMapper(){
        return new MovieTrailerEntityMapperImpl();
    }

}
