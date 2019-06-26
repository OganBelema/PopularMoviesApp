package com.oganbelema.network;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {

    private static final String CACHE_CONTROL = "Cache-Control";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response response = chain.proceed(chain.request());

        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(2, TimeUnit.MINUTES)
                .build();

        return response.newBuilder()
                .addHeader(CACHE_CONTROL, cacheControl.toString())
                .build();


    }
}
