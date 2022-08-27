package com.better.amino.api;

import android.app.Activity;

import com.better.amino.api.utils.AccountUtils;
import com.better.amino.requests.RequestNetwork;
import com.better.amino.ui.SharedValue;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Global {
    Activity context;

    /* Global Related EndPoints */

    private static final String profile = "/g/s/user-profile/";
    private static final String upload = "/g/s/media/upload";

    public Global(Activity context){
        this.context = context;
    }

    private String UploadImage(File inputStream){
        Map<String, Object> map = RequestNetwork.post(context, upload, inputStream);

        if (map != null){
            if (map.get("api:message").toString().equals("OK")){
                return map.get("mediaValue").toString();
            }
        }

        return null;
    }

    public boolean EditProfile(String nickname, String bio, File icon){
        Map<String, Object> data = new HashMap<>();
        data.put("address", null);
        data.put("latitude", 0);
        data.put("longitude", 0);
        data.put("mediaList", null);
        data.put("eventSource", "UserProfileView");
        data.put("nickname", nickname);
        data.put("content", bio);

        if (icon != null){data.put("icon", UploadImage(icon));}

        System.out.println(new Gson().toJson(data));

        Map<String, Object> map = RequestNetwork.post(context, profile + AccountUtils.uid, data);

        if (map != null){
            if (map.get("api:message").toString().equals("OK")){
                AccountUtils.nickname = ((Map<?, ?>) map.get("userProfile")).get("nickname").toString();
                AccountUtils.bio = Objects.requireNonNullElse(((Map<?, ?>) map.get("userProfile")).get("content"), "").toString();
                AccountUtils.icon = Objects.requireNonNullElse(((Map<?, ?>) map.get("userProfile")).get("icon"), "").toString();
                new SharedValue(context).saveString("nickname", nickname);
                new SharedValue(context).saveString("bio", bio);
                new SharedValue(context).saveString("icon", AccountUtils.icon);
                return true;
            }
        }
        return false;
    }
}
