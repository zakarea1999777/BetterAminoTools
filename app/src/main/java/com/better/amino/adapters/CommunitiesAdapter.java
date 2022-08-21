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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommunitiesAdapter extends BaseAdapter {

    Context context;
    ArrayList<Map<String, Object>> communityList;

    public CommunitiesAdapter(Context context, ArrayList<Map<String, Object>> communityList) {
        this.context = context;
        this.communityList = communityList;
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

        if (convertView == null){
            convertView = inflater.inflate(R.layout.communities_item, null);
        }

        final ImageView icon = (ImageView) convertView.findViewById(R.id.community_icon);
        
        try{String url = communityList.get((int) position).get("icon").toString().replace("http", "https");}
        catch (Exception ignored){icon.setImageResource(R.mipmap.ic_launcher_round);}

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(context).load(Uri.parse(url)).apply(options).into(icon);
        return convertView;
    }
}
