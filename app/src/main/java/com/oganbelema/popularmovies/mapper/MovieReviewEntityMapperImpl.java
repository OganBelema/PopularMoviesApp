package com.oganbelema.popularmovies.mapper;

import com.oganbelema.database.entity.MovieReviewEntity;
import com.oganbelema.database.mapper.EntityMapper;
import com.oganbelema.network.model.review.Review;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewEntityMapperImpl implements EntityMapper<MovieReviewEntity, Review> {

    private final static int UNSET_MOVIE_ID = -1;

    private int mMovieId = UNSET_MOVIE_ID;

    public void setMovieId(int movieId){
        mMovieId = movieId;
    }

    @Override
    public Review fromEntity(MovieReviewEntity entity) {
        return new Review(entity.getAuthor(), entity.getContent(), entity.getId(), entity.getUrl());
    }

    @Override
    public MovieReviewEntity toEntity(Review model) {
        if (mMovieId == UNSET_MOVIE_ID)
            throw new IllegalArgumentException("Movie id cannot be null");

        return new MovieReviewEntity(mMovieId, model.getAuthor(), model.getContent(), model.getId(),
                model.getUrl());
    }

    @Override
    public List<Review> fromEntityList(List<MovieReviewEntity> entities) {
        List<Review> reviews = new ArrayList<>();

        for (MovieReviewEntity movieReviewEntity: entities){
            reviews.add(fromEntity(movieReviewEntity));
        }

        return reviews;
    }

    @Override
    public List<MovieReviewEntity> toEntityList(List<Review> reviews) {
        List<MovieReviewEntity> movieReviewEntities = new ArrayList<>();

        for (Review movieReview: reviews){
            movieReviewEntities.add(toEntity(movieReview));
        }

        return movieReviewEntities;
    }
}
