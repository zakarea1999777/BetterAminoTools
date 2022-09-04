package com.better.amino.requests;

import androidx.annotation.NonNull;

import java.io.IOException;

import java8.util.concurrent.CompletableFuture;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

class CallbackFuture extends CompletableFuture<Response> implements Callback {

    /* Return Response When The Request Is Completed */

    public void onResponse(@NonNull Call call, @NonNull Response response) {
        super.complete(response);
    }

    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        super.completeExceptionally(e);
    }
}