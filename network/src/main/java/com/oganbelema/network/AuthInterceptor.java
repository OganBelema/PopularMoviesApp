package com.oganbelema.network;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        HttpUrl originalHttpUrl = originalRequest.url();

        HttpUrl newHttpUrl = originalHttpUrl.newBuilder().addQueryParameter("api_key",
                "INSERT API KEY").build();

        Request newRequest = originalRequest.newBuilder().url(newHttpUrl).build();

        return chain.proceed(newRequest);
    }
}
