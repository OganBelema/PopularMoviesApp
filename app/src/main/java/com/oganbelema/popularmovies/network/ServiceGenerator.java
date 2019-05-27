package com.oganbelema.popularmovies.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static MoviesApi sMoviesApi = null;

    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/original/";

    public static MoviesApi getMovieApi() {
        if (sMoviesApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient()
                            .newBuilder()
                            .addInterceptor(new AuthInterceptor())
                            .build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            sMoviesApi = retrofit.create(MoviesApi.class);
        }

        return sMoviesApi;
    }
}
