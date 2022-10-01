package com.better.amino.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.better.amino.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Map;

public class ChatsAdapter extends BaseAdapter {

    final Context context;
    final ArrayList<Map<String, Object>> chatsList;

    public ChatsAdapter(Context context, ArrayList<Map<String, Object>> communityList) {
        this.context = context;
        this.chatsList = communityList;
    }

    @Override
    public int getCount() {
        return chatsList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chats_item, null);
        }

        final ShapeableImageView icon = convertView.findViewById(R.id.chat_icon);
        final MaterialTextView title = convertView.findViewById(R.id.title);

        String url = "";

        try {
            url = chatsList.get(position).get("icon").toString().replace("http", "https");
        } catch (Exception ignored) {
        }

        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher_round).error(R.mipmap.ic_launcher_round);

        Glide.with(context).load(Uri.parse(url)).apply(options).into(icon);

        try {
            title.setText(chatsList.get(position).get("title").toString());
        } catch (Exception ignored) {
        }
        return convertView;
    }

    public ChatsAdapter removeItem(int i) {
        chatsList.remove(i);
        return this;
    }
}
