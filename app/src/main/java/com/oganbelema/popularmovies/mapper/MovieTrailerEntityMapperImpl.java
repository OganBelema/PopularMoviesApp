package com.oganbelema.popularmovies.mapper;

import com.oganbelema.database.entity.MovieTrailerEntity;
import com.oganbelema.database.mapper.EntityMapper;
import com.oganbelema.network.model.trailer.Trailer;

import java.util.ArrayList;
import java.util.List;

public class MovieTrailerEntityMapperImpl implements EntityMapper<MovieTrailerEntity, Trailer> {

    private final static int UNSET_MOVIE_ID = -1;

    private int mMovieId = UNSET_MOVIE_ID;

    public void setMovieId(int movieId){
        mMovieId = movieId;
    }

    @Override
    public Trailer fromEntity(MovieTrailerEntity entity) {
        return new Trailer(entity.getId(), entity.getIso6391(), entity.getIso31661(), entity.getKey(),
                entity.getName(), entity.getSite(), entity.getSize(), entity.getType());
    }

    @Override
    public MovieTrailerEntity toEntity(Trailer model) {
        if (mMovieId == UNSET_MOVIE_ID)
            throw new IllegalArgumentException("Movie id cannot be null");


        return new MovieTrailerEntity(mMovieId, model.getId(), model.getIso6391(), model.getIso31661(),
                model.getKey(), model.getName(), model.getSite(), model.getSize(), model.getType());
    }

    @Override
    public List<Trailer> fromEntityList(List<MovieTrailerEntity> entities) {
        List<Trailer> trailers = new ArrayList<>();

        for (MovieTrailerEntity movieTrailerEntity: entities){
            trailers.add(fromEntity(movieTrailerEntity));
        }

        return trailers;
    }

    @Override
    public List<MovieTrailerEntity> toEntityList(List<Trailer> trailers) {
        List<MovieTrailerEntity> movieTrailerEntities = new ArrayList<>();

        for (Trailer trailer: trailers){
            movieTrailerEntities.add(toEntity(trailer));
        }

        return movieTrailerEntities;
    }
}
