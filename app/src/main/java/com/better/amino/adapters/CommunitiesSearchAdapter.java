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
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Map;

public class CommunitiesSearchAdapter extends BaseAdapter {

    final Context context;
    ArrayList<Map<String, Object>> communityList = new ArrayList<>();

    public CommunitiesSearchAdapter(Context context, ArrayList<Map<String, Object>> communityList) {
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
        Map<String, Object> community = communityList.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.communities_search_item, null);
        }

        final ImageView icon = convertView.findViewById(R.id.community_searched_icon);
        final MaterialTextView name = convertView.findViewById(R.id.name);
        final MaterialTextView description = convertView.findViewById(R.id.description);

        name.setText(community.get("name").toString());
        description.setText(community.get("tagline").toString());

        String url = "";

        try {
            url = community.get("icon").toString().replace("http", "https");
        } catch (Exception ignored) {
        }

        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher_round).error(R.drawable.plus);

        Glide.with(context).load(Uri.parse(url)).apply(options).into(icon);

        return convertView;
    }
}
