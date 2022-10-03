package com.better.amino.api;

import android.app.Activity;

import com.better.amino.api.utils.AccountUtils;
import com.better.amino.api.utils.ChatUtils;
import com.better.amino.api.utils.CommunityUtils;
import com.better.amino.requests.RequestNetwork;
import com.better.amino.ui.ToastManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Community {
    private static String community;

    /* Community Related EndPoints */
    private static String chat;
    private static String getchats;
    private static String getmsgs;
    private static String sendmsg;
    private static String sendcoins;
    private static String leavecom;
    private static String joincom;
    private static String leavechat;
    final Activity context;

    // TODO: Complete Those Later

    public Community(Activity context) {
        this.context = context;
        RequestNetwork.context = context;
        if (CommunityUtils.community.get("ndcId") != null) {
            community = "/x" + ((Double) CommunityUtils.community.get("ndcId")).intValue();
        }
        chat = "/s/chat/thread/" + ChatUtils.chatId;
        getchats = community + "/s/chat/thread?type=joined-me&start=0&size=100";
        getmsgs = community + chat + "/message?v=2&pagingType=t&size=100";
        sendmsg = community + chat + "/message";
        sendcoins = community + chat + "/tipping";
        leavecom = community + "/s/community/leave";
        joincom = community + "/s/community/join";
        leavechat = community + chat + "/member/" + AccountUtils.uid;
    }

    public ArrayList<Map<String, Object>> getChats() {
        Map<String, Object> map = RequestNetwork.get(getchats);
        if (map != null) {
            return (ArrayList<Map<String, Object>>) map.get("threadList");
        } else {
            return new ArrayList<>();
        }
    }

    public ArrayList<Map<String, Object>> getMessages() {
        Map<String, Object> map = RequestNetwork.get(getmsgs);
        if (map != null) {
            return (ArrayList<Map<String, Object>>) map.get("messageList");
        } else {
            return new ArrayList<>();
        }
    }

    public boolean SendMessage(String message, int msgType) {
        Map<String, Object> data = new HashMap<>();
        data.put("content", message);
        data.put("type", msgType);
        Map<String, Object> map = RequestNetwork.post(sendmsg, data);
        return map != null;
    }

    public boolean SendCoins(int coins, String uid) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> tippingContext = new HashMap<>();

        if (uid == null) {
            uid = UUID.randomUUID().toString();
        }
        tippingContext.put("transactionId", uid);

        data.put("coins", coins);
        data.put("tippingContext", tippingContext);

        Map<String, Object> map = RequestNetwork.post(sendcoins, data);
        return map != null;
    }

    public void LeaveCommunity() {
        RequestNetwork.post(leavecom);
    }

    public void JoinCommunity() {
        Map<String, Object> map = RequestNetwork.post(joincom, new HashMap<>());
        if (map != null) {
            ToastManager.makeToast(context, "Community Joined");
        }
    }

    public void LeaveChat() {
        Map<String, Object> map = RequestNetwork.delete(leavechat);
    }
}
