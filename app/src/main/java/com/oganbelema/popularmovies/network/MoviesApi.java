package com.oganbelema.popularmovies.network;

import com.oganbelema.popularmovies.movie.MovieResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface MoviesApi {

    @GET("movie/popular/")
    Single<Response<MovieResponse>> getPopularMovies();

    @GET("movie/top_rated/")
    Single<Response<MovieResponse>> getTopRatedMovies();

}
