package com.better.amino.api;

import android.app.Activity;
import android.content.Intent;

import com.better.amino.activities.MainActivity;
import com.better.amino.api.utils.AccountUtils;
import com.better.amino.requests.RequestNetwork;
import com.better.amino.ui.SharedValue;
import com.better.amino.utils.Headers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Global {
    private static final String profile = "/g/s/user-profile/";
    private static final String upload = "/g/s/media/upload";
    private static final String getcoms = "/g/s/community/joined?v=1&start=0&size=100";
    // private static final String searchcoms = String.format("/g/s/community/search?language=%s&&completeKeyword=1&start=0&size=100&q=", Headers.headers.get("NDCLANG"));
    private static final String searchcoms = "/g/s/community/search?q=%s&searchId=%s&language=%s&completeKeyword=1&start=0&size=25";
    final Activity context;

    public Global(Activity context) {
        this.context = context;
        RequestNetwork.context = context;
    }

    private String UploadImage(File inputStream) {
        Map<String, Object> map = RequestNetwork.post(upload, inputStream);

        if (map != null) {
            if (map.get("api:message").toString().equals("OK")) {
                return map.get("mediaValue").toString();
            }
        }

        return null;
    }

    public boolean EditProfile(String nickname, String bio, File icon) {
        Map<String, Object> data = new HashMap<>();
        data.put("address", null);
        data.put("latitude", 0);
        data.put("longitude", 0);
        data.put("mediaList", null);
        data.put("eventSource", "UserProfileView");
        data.put("nickname", nickname);
        data.put("content", bio);

        if (icon != null) {
            data.put("icon", UploadImage(icon));
        }

        Map<String, Object> map = RequestNetwork.post(profile + AccountUtils.uid, data);

        if (map != null) {
            if (map.get("api:message").toString().equals("OK")) {
                AccountUtils.nickname = ((Map<?, ?>) map.get("userProfile")).get("nickname").toString();
                AccountUtils.bio = Objects.requireNonNullElse(((Map<?, ?>) map.get("userProfile")).get("content"), "").toString();
                AccountUtils.icon = Objects.requireNonNullElse(((Map<?, ?>) map.get("userProfile")).get("icon"), "").toString();
                SharedValue shared = new SharedValue(context);
                shared.saveString("nickname", nickname);
                shared.saveString("bio", bio);
                shared.saveString("icon", AccountUtils.icon);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Map<String, Object>> getCommunities() {
        Map<String, Object> map = RequestNetwork.get(getcoms);
        try {
            return (ArrayList<Map<String, Object>>) map.get("communityList");
        } catch (Exception e) {
            AccountUtils.logged = false;
            new SharedValue(context).saveBoolean("logged", false);
            context.startActivity(new Intent(context, MainActivity.class));
            return new ArrayList<>();
        }
    }

    public ArrayList<Map<String, Object>> searchCommunities(String q) {
        Map<String, Object> map = RequestNetwork.get(String.format(searchcoms, q, UUID.randomUUID().toString(), Headers.headers.get("NDCLANG")));
        try {
            return (ArrayList<Map<String, Object>>) map.get("communityList");
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}


