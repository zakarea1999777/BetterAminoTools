package com.better.amino.requests;

import java.io.IOException;

import java8.util.concurrent.CompletableFuture;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

class CallbackFuture extends CompletableFuture<Response> implements Callback {

    /* Return Response When The Request Is Completed */

    public void onResponse(Call call, Response response) {
        super.complete(response);
    }
    public void onFailure(Call call, IOException e){
        super.completeExceptionally(e);
    }
}