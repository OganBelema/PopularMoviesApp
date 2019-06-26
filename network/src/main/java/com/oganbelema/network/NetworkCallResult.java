package com.oganbelema.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import retrofit2.Response;

public class NetworkCallResult<T> {

    @Nullable
    private Response<T> response;

    @Nullable
    private Throwable error;

    public NetworkCallResult(@NonNull Response<T> response) {
        this.response = response;
    }

    public NetworkCallResult(@NonNull Throwable error) {
        this.error = error;
    }

    @Nullable
    public Response<T> getResponse() {
        return response;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }
}
