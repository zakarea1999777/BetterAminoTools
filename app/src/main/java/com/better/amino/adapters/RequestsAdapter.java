package com.better.amino.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.better.amino.R;

import java.util.ArrayList;
import java.util.Map;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;
    private ArrayList<Map<String, Object>> mData;
    private ItemClickListener mClickListener;
    private ItemLongClickListener mLongClickListener;

    public RequestsAdapter(Context context, ArrayList<Map<String, Object>> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    public void setOnRequestClickedListener(ItemClickListener onProjectSelectedListener) {
        this.mClickListener = onProjectSelectedListener;
    }

    public void setOnRequestLongClickedListener(ItemLongClickListener onProjectLongClickedListener) {
        this.mLongClickListener = onProjectLongClickedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> request = mData.get(position);
        holder.title.setText(request.get("name").toString());

        holder.background.setOnClickListener(
                v -> {
                    if (mClickListener != null && position != RecyclerView.NO_POSITION) {
                        mClickListener.onItemClick(position);
                    }
                });
        holder.background.setOnLongClickListener(
                v -> {
                    if (mLongClickListener != null
                            && position != RecyclerView.NO_POSITION) {
                        mLongClickListener.onItemLongClick(position);
                    }
                    return true;
                });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void add(Map<String, Object> data) {

        if (!mData.contains(data)) {
            mData.add(data);
            notifyItemInserted(getItemCount() - 1);
        }
    }

    public Map<String, Object> get(int position) {
        return mData.get(position);
    }

    public Map<String, Object> delete(int position) {
        return mData.remove(position);
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public interface ItemLongClickListener {
        void onItemLongClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View background;
        private final TextView title;

        ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.project_title);
            background = itemView.findViewById(R.id.background);

        }
    }
}
