package com.better.amino.api;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.better.amino.activites.HomeActivity;
import com.better.amino.api.utils.AccountUtils;
import com.better.amino.requests.RequestNetwork;
import com.better.amino.ui.SharedValue;
import com.better.amino.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Account {

    Activity context;

    /* Account Related EndPoints */

    private static final String login = "/g/s/auth/login";
    private static final String register = "/g/s/auth/register";
    private static final String logout = "/g/s/auth/logout";

    // TODO: Complete Those Later
    private static final String rsv = "/g/s/auth/request-security-validation";
    private static final String activate = "activate-email";
    private static final String restore = "/g/s/account/delete-request/cancel";
    private static final String delete = "/g/s/account/delete-request";
    private static final String account = "/g/s/account";
    private static final String aminoid = "/g/s/account/change-amino-id";
    private static final String resetpass = "/g/s/auth/reset-password";
    private static final String changepass = "/g/s/account/change-amino-id";

    public Account(Activity context){
        this.context = context;
    }

    public boolean isLogged(){
        return AccountUtils.logged;
    }


    public void Login(String email, String password){
        Map<String, Object> data = new HashMap<>();
        data.put("clientType", 100);
        data.put("action", "normal");
        data.put("deviceID", Utils.deviceId());
        data.put("v", 2);
        data.put("email", email);
        data.put("secret", "0 " + password);

        Map<String, Object> map = RequestNetwork.post(context, login, data);
        if (map != null){
            AccountUtils.sid = "sid=" + map.get("sid").toString();
            AccountUtils.uid = map.get("auid").toString();
            AccountUtils.nickname = ((Map<?, ?>) map.get("userProfile")).get("nickname").toString();
            AccountUtils.bio = Objects.requireNonNullElse(((Map<?, ?>) map.get("userProfile")).get("content"), "").toString();
            AccountUtils.aminoId = ((Map<?, ?>) map.get("account")).get("aminoId").toString();
            AccountUtils.icon = Objects.requireNonNullElse(((Map<?, ?>) map.get("userProfile")).get("icon"), "").toString();
            AccountUtils.logged = true;
            SharedValue shared = new SharedValue(context);
            shared.saveString("sid", AccountUtils.sid);
            shared.saveString("uid", AccountUtils.uid);
            shared.saveString("nickname", AccountUtils.nickname);
            shared.saveString("aminoId", AccountUtils.aminoId);
            shared.saveString("icon", AccountUtils.icon);
            shared.saveString("bio", AccountUtils.bio);
            shared.saveBoolean("logged", AccountUtils.logged);
            context.startActivity(new Intent(context, HomeActivity.class));
        }
    }

    public void Register(String email, String password, String nickname){
        Map<String, Object> data = new HashMap<>();
        data.put("clientType", 100);
        data.put("action", "normal");
        data.put("deviceID", Utils.deviceId());
        data.put("type", 1);
        data.put("email", email);
        data.put("identity", email);
        data.put("address", null);
        data.put("latitude", 0);
        data.put("longitude", 0);
        data.put("nickname", nickname);
        data.put("clientCallbackURL", "narviiapp://relogin");
        data.put("secret", "0 " + password);

        Map<String, Object> map = RequestNetwork.post(context, register, data);
        if (map != null){
            AccountUtils.sid = "sid=" + map.get("sid").toString();
            AccountUtils.uid = map.get("auid").toString();
            AccountUtils.nickname = ((Map<?, ?>) map.get("account")).get("nickname").toString();
            AccountUtils.aminoId = ((Map<?, ?>) map.get("account")).get("aminoId").toString();
            Log.d("SID", AccountUtils.sid);
        }
    }

    public void Logout(Activity context, String email, String password){
        Map<String, Object> data = new HashMap<>();
        data.put("clientType", 100);
        data.put("deviceID", Utils.deviceId());

        Map<String, Object> map = RequestNetwork.post(context, logout, data);
        if (map != null){
            AccountUtils.sid = "sid=" + map.get("sid").toString();
            AccountUtils.uid = map.get("auid").toString();
            AccountUtils.nickname = ((Map<?, ?>) map.get("account")).get("nickname").toString();
            AccountUtils.aminoId = ((Map<?, ?>) map.get("account")).get("aminoId").toString();
            Log.d("SID", AccountUtils.sid);
        }
    }
}
