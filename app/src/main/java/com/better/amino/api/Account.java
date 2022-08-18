package com.better.amino.api;


import android.app.Activity;
import android.util.Log;

import com.better.amino.api.utils.AccountUtils;
import com.better.amino.requests.RequestNetwork;
import com.better.amino.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/* Account Related EndPoints */

public class Account {

    private static String login = "/g/s/auth/login";

    // TODO: Complete Those Later
    private static String register = "/g/s/auth/register";
    private static String logout = "/g/s/auth/logout";
    private static String rsv = "/g/s/auth/request-security-validation";
    private static String activate = "activate-email";
    private static String restore = "/g/s/account/delete-request/cancel";
    private static String delete = "/g/s/account/delete-request";
    private static String account = "/g/s/account";
    private static String aminoid = "/g/s/account/change-amino-id";
    private static String resetpass = "/g/s/auth/reset-password";
    private static String changepass = "/g/s/account/change-amino-id";


    public static void Login(Activity context, String email, String password){
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
            AccountUtils.nickname = ((Map<?, ?>) map.get("account")).get("nickname").toString();
            AccountUtils.aminoId = ((Map<?, ?>) map.get("account")).get("aminoId").toString();
            Log.d("SID", AccountUtils.sid);
        }
    }
}
