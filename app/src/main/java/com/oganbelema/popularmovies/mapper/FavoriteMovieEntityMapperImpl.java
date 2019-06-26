package com.oganbelema.popularmovies.mapper;

import com.oganbelema.database.entity.FavoriteMovieEntity;
import com.oganbelema.database.mapper.EntityMapper;
import com.oganbelema.network.model.movie.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMovieEntityMapperImpl implements
        EntityMapper<FavoriteMovieEntity, Movie> {

    @Override
    public Movie fromEntity(FavoriteMovieEntity favoriteMovieEntity) {
        return new Movie(favoriteMovieEntity.getVoteCount(), favoriteMovieEntity.getId(),
                favoriteMovieEntity.getVideo(), favoriteMovieEntity.getVoteAverage(),
                favoriteMovieEntity.getTitle(), favoriteMovieEntity.getPopularity(),
                favoriteMovieEntity.getPosterPath(), favoriteMovieEntity.getOriginalLanguage(),
                favoriteMovieEntity.getOriginalTitle(), favoriteMovieEntity.getBackdropPath(),
                favoriteMovieEntity.getAdult(), favoriteMovieEntity.getOverview(),
                favoriteMovieEntity.getReleaseDate());
    }

    @Override
    public FavoriteMovieEntity toEntity(Movie movie) {
        return new FavoriteMovieEntity(movie.getVoteCount(), movie.getId(), movie.getVideo(),
                movie.getVoteAverage(), movie.getTitle(), movie.getPopularity(),
                movie.getPosterPath(), movie.getOriginalLanguage(), movie.getOriginalTitle(),
                movie.getBackdropPath(), movie.getAdult(), movie.getOverview(),
                movie.getReleaseDate());
    }

    public List<Movie> fromEntityList(List<FavoriteMovieEntity> favoriteMovieEntities){
        List<Movie> movies = new ArrayList<>();

        for (FavoriteMovieEntity favoriteMovieEntity: favoriteMovieEntities){
            movies.add(fromEntity(favoriteMovieEntity));
        }

        return movies;
    }

    @Override
    public List<FavoriteMovieEntity> toEntityList(List<Movie> movies) {
        List<FavoriteMovieEntity> favoriteMovieEntities = new ArrayList<>();

        for (Movie movie: movies){
            favoriteMovieEntities.add(toEntity(movie));
        }

        return favoriteMovieEntities;
    }
}
