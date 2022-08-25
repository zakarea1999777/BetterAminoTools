package com.better.amino.api;

import android.app.Activity;

import com.better.amino.api.utils.AccountUtils;
import com.better.amino.requests.RequestNetwork;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Global {
    Activity context;

    /* Global Related EndPoints */

    private static final String profile = "/g/s/user-profile/";

    public Global(Activity context){
        this.context = context;
    }

    public void EditProfile(String nickname, String bio){
        Map<String, Object> data = new HashMap<>();
        data.put("address", null);
        data.put("latitude", 0);
        data.put("longitude", 0);
        data.put("mediaList", null);
        data.put("eventSource", "UserProfileView");
        data.put("nickname", nickname);
        data.put("context", bio);

        Map<String, Object> map = RequestNetwork.post(context, profile + AccountUtils.uid, data);
        if (map != null){
            AccountUtils.nickname = ((Map<?, ?>) map.get("userProfile")).get("nickname").toString();
            AccountUtils.bio = Objects.requireNonNullElse(((Map<?, ?>) map.get("userProfile")).get("content"), "").toString();
        }
    }
}
