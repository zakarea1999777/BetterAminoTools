package com.better.amino.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.better.amino.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommunitiesAdapter extends BaseAdapter {

    final Context context;
    final ArrayList<Map<String, Object>> communityList = new ArrayList<>();

    public CommunitiesAdapter(Context context, ArrayList<Map<String, Object>> communityList) {
        this.context = context;
        Map<String, Object> map = new HashMap<>();
        map.put("icon", "https://");
        this.communityList.addAll(communityList);
        this.communityList.add(map);
    }

    @Override
    public int getCount() {
        return communityList.size();
    }

    @Override
    public Object getItem(int i) {
        return communityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.communities_item, null);
        }

        final ImageView icon = convertView.findViewById(R.id.community_icon);
        final MaterialCardView cardView = convertView.findViewById(R.id.community_card);

        String url = "";

        try {
            url = communityList.get(position).get("icon").toString().replace("http", "https");
        } catch (Exception ignored) {
        }

        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher_round).error(R.drawable.plus);

        Glide.with(context).load(Uri.parse(url)).apply(options).into(icon);

        if (position == getCount() - 1) {
            icon.getLayoutParams().height = 100;
            icon.getLayoutParams().width = 100;
            cardView.setStrokeWidth(0);
        }

        return convertView;
    }

    public CommunitiesAdapter removeItem(int i) {
        communityList.remove(i);
        return this;
    }
}
