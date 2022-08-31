package com.better.amino.api;

import android.app.Activity;

import com.better.amino.api.utils.ChatUtils;
import com.better.amino.api.utils.CommunityUtils;
import com.better.amino.requests.RequestNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Community {

    final Activity context;

    /* Community Related EndPoints */

    private static String getchats;
    private static String sendmsg;

    // TODO: Complete Those Later

    public Community(Activity context){
        this.context = context;

        String community = "/x" + ((Double) CommunityUtils.community.get("ndcId")).intValue();
        String chat = "/s/chat/thread/" + ChatUtils.chatId;

        getchats = community + "/s/chat/thread?type=joined-me&start=0&size=100";
        sendmsg = community + chat + "/message";
    }

    public ArrayList<Map<String, Object>> getChats() {
        Map<String, Object> map = RequestNetwork.get(context, getchats);

        if (map != null) {
            return (ArrayList<Map<String, Object>>) map.get("threadList");}
            else {return new ArrayList<>();
        }
    }

    public boolean SendMessage(String message){
        Map<String, Object> data = new HashMap<>();
        data.put("content", message);
        data.put("type", 0);

        Map<String, Object> map = RequestNetwork.post(context, sendmsg, data);

        return map != null;
    }
}
