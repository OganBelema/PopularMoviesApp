package com.oganbelema.popularmovies.mapper;

import com.oganbelema.database.entity.FavoriteMovieEntity;
import com.oganbelema.database.mapper.FavoriteMovieEntityMapper;
import com.oganbelema.network.model.movie.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMovieEntityMapperImpl implements
        FavoriteMovieEntityMapper<FavoriteMovieEntity, Movie> {

    @Override
    public Movie fromFavouriteMovieEntity(FavoriteMovieEntity favoriteMovieEntity) {
        return new Movie(favoriteMovieEntity.getVoteCount(), favoriteMovieEntity.getId(),
                favoriteMovieEntity.getVideo(), favoriteMovieEntity.getVoteAverage(),
                favoriteMovieEntity.getTitle(), favoriteMovieEntity.getPopularity(),
                favoriteMovieEntity.getPosterPath(), favoriteMovieEntity.getOriginalLanguage(),
                favoriteMovieEntity.getOriginalTitle(), favoriteMovieEntity.getBackdropPath(),
                favoriteMovieEntity.getAdult(), favoriteMovieEntity.getOverview(),
                favoriteMovieEntity.getReleaseDate());
    }

    @Override
    public FavoriteMovieEntity toFavoriteMovieEntity(Movie movie) {
        return new FavoriteMovieEntity(movie.getVoteCount(), movie.getId(), movie.getVideo(),
                movie.getVoteAverage(), movie.getTitle(), movie.getPopularity(),
                movie.getPosterPath(), movie.getOriginalLanguage(), movie.getOriginalTitle(),
                movie.getBackdropPath(), movie.getAdult(), movie.getOverview(),
                movie.getReleaseDate());
    }

    public List<Movie> fromFavoriteMovieEntities(List<FavoriteMovieEntity> favoriteMovieEntities){
        List<Movie> movies = new ArrayList<>();

        for (FavoriteMovieEntity favoriteMovieEntity: favoriteMovieEntities){
            movies.add(fromFavouriteMovieEntity(favoriteMovieEntity));
        }

        return movies;
    }

}
