package com.better.amino.api;

import android.app.Activity;

import com.better.amino.requests.RequestNetwork;

import java.util.ArrayList;
import java.util.Map;

public class Community {

    Activity context;

    /* Account Related EndPoints */

    private static final String getcoms = "/g/s/community/joined?v=1&start=0&size=100";

    // TODO: Complete Those Later

    public Community(Activity context){
        this.context = context;
    }

    public ArrayList<Map<String, Object>> getCommunities() {

        Map<String, Object> map = RequestNetwork.get(context, getcoms);
        ArrayList<Map<String, Object>> communities = (ArrayList<Map<String, Object>>) map.get("communityList");
        return communities;

    }
}
