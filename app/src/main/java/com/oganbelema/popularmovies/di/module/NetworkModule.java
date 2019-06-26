package com.oganbelema.popularmovies.di.module;

import android.content.Context;

import com.oganbelema.popularmovies.BuildConfig;
import com.oganbelema.popularmovies.Constants;
import com.oganbelema.popularmovies.network.AuthInterceptor;
import com.oganbelema.popularmovies.network.CacheInterceptor;
import com.oganbelema.popularmovies.network.MoviesApi;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    @Named(Constants.NAMED_BASE_URL)
    public String provideBaseUrl(){
        return Constants.BASE_URL;
    }

    @Provides
    @Singleton
    @Named(Constants.NAMED_IMAGE_URL)
    public String provideImageUrl(){
        return Constants.IMAGE_URL;
    }

    @Provides
    @Singleton
    public AuthInterceptor provideAuthInterceptor(){
        return new AuthInterceptor();
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(
                BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE
        );
        return httpLoggingInterceptor;
    }

    @Provides
    @Singleton
    public Cache providesCache(Context context){
        return new Cache(new File(context.getCacheDir(), "http-cache"),
                10 * 1024 * 1024); //10 MB
    }

    @Provides
    @Singleton
    public CacheInterceptor provideCacheInterceptor(){
        return new CacheInterceptor();
    }

    @Provides
    @Singleton
    public OkHttpClient provideClient(AuthInterceptor authInterceptor,
                                      HttpLoggingInterceptor httpLoggingInterceptor,
                                      Cache cache, CacheInterceptor cacheInterceptor){
        return new OkHttpClient()
                .newBuilder()
                .addInterceptor(authInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideBaseRetrofit(@Named(Constants.NAMED_BASE_URL) String baseUrl,
                                        OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public MoviesApi provideMovieApi(Retrofit retrofit){
        return retrofit.create(MoviesApi.class);
    }

}
