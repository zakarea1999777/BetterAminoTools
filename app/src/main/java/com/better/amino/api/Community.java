package com.better.amino.api;

import android.app.Activity;
import android.util.Log;

import com.better.amino.api.utils.AccountUtils;
import com.better.amino.requests.RequestNetwork;
import com.better.amino.ui.SharedValue;
import com.better.amino.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Community {

    Activity context;

    /* Account Related EndPoints */

    private static final String getcoms = "/g/s/community/joined?v=1&start=0&size=100";

    // TODO: Complete Those Later

    public ArrayList<Map<String, Object>> getCommunities() {

        Map<String, Object> map = RequestNetwork.get(context, getcoms);
        ArrayList<Map<String, Object>> communities = (ArrayList<Map<String, Object>>) map.get("communityList");
        return communities;

    }
}
