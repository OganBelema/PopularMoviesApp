package com.oganbelema.network;

import com.oganbelema.network.model.movie.MovieResponse;
import com.oganbelema.network.model.review.ReviewResponse;
import com.oganbelema.network.model.trailer.TrailerResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {

    @GET("movie/popular/")
    Single<Response<MovieResponse>> getPopularMovies(@Query("page") long page);

    @GET("movie/top_rated/")
    Single<Response<MovieResponse>> getTopRatedMovies(@Query("page") long page);

    @GET("movie/{movie_id}/reviews")
    Single<Response<ReviewResponse>> getMovieReviews(@Path("movie_id") int movieId);

    @GET("movie/{movie_id}/videos")
    Single<Response<TrailerResponse>> getMovieTrailers(@Path("movie_id") int movieId);

}
