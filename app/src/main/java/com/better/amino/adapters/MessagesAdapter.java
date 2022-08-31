package com.better.amino.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.better.amino.R;
import com.better.amino.api.utils.AccountUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private final ArrayList<Map<String, Object>> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private final Context context;

    // data is passed into the constructor
    public MessagesAdapter(Context context, ArrayList<Map<String, Object>> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.message_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> message = mData.get(position);
        Map<String, Object> author = (Map<String, Object>) message.get("author");

        String url = "";

        try {url = author.get("icon").toString().replace("http", "https");}
        catch (Exception ignored){}

        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher_round).error(R.mipmap.ic_launcher_round);

        if (message.get("uid").toString().equals(AccountUtils.uid)) {
            // My Message
            holder.sentMessage.setVisibility(View.VISIBLE);
            holder.sentIcon.setVisibility(View.VISIBLE);
            holder.sentNickname.setVisibility(View.VISIBLE);

            holder.receivedMessage.setVisibility(View.GONE);
            holder.receivedIcon.setVisibility(View.GONE);
            holder.receivedNickname.setVisibility(View.GONE);

            holder.sentMessage.setText((CharSequence) message.get("content"));
            holder.sentNickname.setText((CharSequence) author.get("nickname"));
            Glide.with(context).load(Uri.parse(url)).apply(options).into(holder.sentIcon);
        } else {
            holder.receivedIcon.setVisibility(View.VISIBLE);
            holder.receivedMessage.setVisibility(View.VISIBLE);
            holder.receivedNickname.setVisibility(View.VISIBLE);

            holder.sentMessage.setVisibility(View.GONE);
            holder.sentIcon.setVisibility(View.GONE);
            holder.sentNickname.setVisibility(View.GONE);

            holder.receivedMessage.setText((CharSequence) message.get("content"));
            holder.receivedNickname.setText((CharSequence) author.get("nickname"));
            Glide.with(context).load(Uri.parse(url)).apply(options).into(holder.receivedIcon);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final MaterialTextView sentMessage;
        final MaterialTextView receivedMessage;
        final MaterialTextView sentNickname;
        final MaterialTextView receivedNickname;
        final CircleImageView sentIcon;
        final CircleImageView receivedIcon;

        ViewHolder(View itemView) {
            super(itemView);
            sentMessage = itemView.findViewById(R.id.sentMessage);
            receivedMessage = itemView.findViewById(R.id.receivedMessage);
            sentIcon = itemView.findViewById(R.id.sentIcon);
            receivedIcon = itemView.findViewById(R.id.receivedIcon);
            sentNickname = itemView.findViewById(R.id.sentNickname);
            receivedNickname = itemView.findViewById(R.id.receivedNickname);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAbsoluteAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Map<String, Object> getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void add(Map<String, Object> data){
        mData.add(data);
        notifyItemInserted(getItemCount() - 1);
    }
}