package com.better.amino.requests;

import android.app.Activity;

import com.better.amino.ui.IntentManager;
import com.better.amino.ui.ToastManager;
import com.better.amino.utils.Headers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestNetwork {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_JPG = MediaType.parse("application/octet-stream");
    private static final OkHttpClient client = new OkHttpClient();
    private static final String api = "https://service.narvii.com/api/v1";
    public static Activity context;
    private static Request request;

    /* Request Method POST */

    public static Map<String, Object> post(String url, Map<String, Object> data) {
        String responseJson = "";
        Map<String, Object> responseBody = null;
        String apimessage;
        int statuscode;

        data.put("timestamp", System.currentTimeMillis() * 1000 / 1000);
        String json = new Gson().toJson(data);
        RequestBody body = RequestBody.create(new Gson().toJson(data), JSON);
        okhttp3.Headers.Builder headerBuilder = new okhttp3.Headers.Builder();

        for (Map.Entry<String, String> entry : Headers.GetHeaders(json).entrySet()) {
            headerBuilder.add(entry.getKey(), entry.getValue());
        }

        request = new Request.Builder().url(api + url).post(body).headers(headerBuilder.build()).build();
        CallbackFuture future = new CallbackFuture();
        client.newCall(request).enqueue(future);

        try {

            responseJson = future.get().body().string();
            responseBody = new Gson().fromJson(responseJson, new TypeToken<HashMap<String, Object>>() {
            }.getType());
            statuscode = ((Double) responseBody.get("api:statuscode")).intValue();
            apimessage = responseBody.get("api:message").toString();

            switch (statuscode) {
                case 0:
                    break;
                case 270:
                    IntentManager.goToUrl(context, responseBody.get("url").toString());
                    return null;
                default:
                    ToastManager.makeToast(context, apimessage);
                    return null;
            }
        } catch (IOException | InterruptedException | ExecutionException |
                 IllegalStateException ignored) {
            ToastManager.makeToast(context, responseJson);
        }
        return responseBody;
    }

    public static Map<String, Object> post(String url, File inputStream) {
        String responseJson = "";
        Map<String, Object> responseBody = null;
        String apimessage;
        int statuscode;

        try {
            okhttp3.Headers.Builder headerBuilder = new okhttp3.Headers.Builder();

            for (Map.Entry<String, String> entry : Headers.GetHeaders((int) inputStream.length()).entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }

            RequestBody body = RequestBody.create(inputStream, MEDIA_TYPE_JPG);

            request = new Request.Builder().url(api + url).post(body).headers(headerBuilder.build()).build();

            CallbackFuture future = new CallbackFuture();
            client.newCall(request).enqueue(future);

            responseJson = future.get().body().string();
            responseBody = new Gson().fromJson(responseJson, new TypeToken<HashMap<String, Object>>() {
            }.getType());
            statuscode = ((Double) responseBody.get("api:statuscode")).intValue();
            apimessage = responseBody.get("api:message").toString();


            if (statuscode != 0) {
                ToastManager.makeToast(context, "Failed to Upload Image: " + apimessage);
                return null;
            }
        } catch (IOException | InterruptedException | ExecutionException |
                 IllegalStateException e) {
            e.printStackTrace();
            ToastManager.makeToast(context, "Failed to Upload Image: " + responseJson);
        }

        return responseBody;
    }

    public static Map<String, Object> post(String url) {
        return post(url, new HashMap<>());
    }

    /* Request Method GET */

    public static Map<String, Object> get(String url) {
        String responseJson = "";
        Map<String, Object> responseBody = null;
        String apimessage;
        int statuscode;

        okhttp3.Headers.Builder headerBuilder = new okhttp3.Headers.Builder();

        for (Map.Entry<String, String> entry : Headers.GetHeaders(null).entrySet()) {
            headerBuilder.add(entry.getKey(), entry.getValue());
        }

        request = new Request.Builder().url(api + url).get().headers(headerBuilder.build()).build();
        CallbackFuture future = new CallbackFuture();
        client.newCall(request).enqueue(future);

        try {
            responseJson = future.get().body().string();
            responseBody = new Gson().fromJson(responseJson, new TypeToken<HashMap<String, Object>>() {
            }.getType());
            statuscode = ((Double) responseBody.get("api:statuscode")).intValue();
            apimessage = responseBody.get("api:message").toString();

            switch (statuscode) {
                case 0:
                    break;
                case 105:
                    ToastManager.makeToast(context, "Session is Expired");
                    return null;
                default:
                    ToastManager.makeToast(context, apimessage);
                    return null;
            }
        } catch (IOException | InterruptedException | ExecutionException |
                 IllegalStateException ignored) {
            ToastManager.makeToast(context, responseJson);
        }
        return responseBody;
    }

    /* Request Method DELETE */

    public static Map<String, Object> delete(String url) {
        String responseJson = "";
        Map<String, Object> responseBody = null;
        String apimessage;
        int statuscode;

        okhttp3.Headers.Builder headerBuilder = new okhttp3.Headers.Builder();

        for (Map.Entry<String, String> entry : Headers.GetHeaders("").entrySet()) {
            headerBuilder.add(entry.getKey(), entry.getValue());
        }

        request = new Request.Builder().url(api + url).delete().headers(headerBuilder.build()).build();
        CallbackFuture future = new CallbackFuture();
        client.newCall(request).enqueue(future);

        try {

            responseJson = future.get().body().string();
            responseBody = new Gson().fromJson(responseJson, new TypeToken<HashMap<String, Object>>() {
            }.getType());
            statuscode = ((Double) responseBody.get("api:statuscode")).intValue();
            apimessage = responseBody.get("api:message").toString();

            if (statuscode != 0) {
                ToastManager.makeToast(context, apimessage);
                return null;
            }
        } catch (IOException | InterruptedException | ExecutionException |
                 IllegalStateException ignored) {
            ToastManager.makeToast(context, responseJson);
        }
        return responseBody;
    }
}

