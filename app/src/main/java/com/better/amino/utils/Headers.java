package com.better.amino.utils;

import android.os.Build;

import com.better.amino.api.utils.AccountUtils;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Headers {
    private static Map<String, String> headers = new HashMap<>();

    /* Generate Request Headers */

    public static Map<String, String> GetHeaders(String data){
        headers.put("NDCDEVICEID", Utils.deviceId());
        headers.put("NDC-MSG-SIG", Utils.signature(data));

        if (AccountUtils.sid != null){headers.put("NDCAUTH", AccountUtils.sid);}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            headers.put("Accept-Language", Locale.getDefault(Locale.Category.DISPLAY).toString().replace("_", "-"));
            headers.put("NDCLANG", Array.get(Locale.getDefault(Locale.Category.DISPLAY).toString().split("_"), 0).toString());
        }

        return headers;
    }
}
