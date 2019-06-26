package com.oganbelema.popularmovies.network;

import com.oganbelema.popularmovies.remote.model.movie.MovieResponse;
import com.oganbelema.popularmovies.remote.model.review.ReviewResponse;
import com.oganbelema.popularmovies.remote.model.trailer.TrailerResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MoviesApi {

    @GET("movie/popular/")
    Single<Response<MovieResponse>> getPopularMovies();

    @GET("movie/top_rated/")
    Single<Response<MovieResponse>> getTopRatedMovies();

    @GET("movie/{movie_id}/reviews/")
    Single<ReviewResponse> getMovieReviews(@Path("movie_id") int movieId);

    @GET("movie/{movie_id}/videos/")
    Single<TrailerResponse> getMovieTrailers(@Path("movie_id") int movieId);

}
