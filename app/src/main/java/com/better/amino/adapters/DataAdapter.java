package com.better.amino.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatImageButton;

import com.better.amino.R;
import com.better.amino.utils.FileUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class DataAdapter extends BaseAdapter {

    final Context context;
    final ArrayList<Map<String, Object>> dataList = new ArrayList<>();
    ArrayList<Map<String, Object>> arrayList;
    int pos;

    public DataAdapter(Context context, ArrayList<Map<String, Object>> lst, ArrayList<Map<String, Object>> arrayList, int pos) {
        this.context = context;
        this.dataList.addAll(lst);
        this.arrayList = arrayList;
        this.pos = pos;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.data_item, null);
        }

        TextInputEditText key = convertView.findViewById(R.id.textInputEditTextKey);
        TextInputEditText value = convertView.findViewById(R.id.textInputEditTextValue);
        AppCompatImageButton remove = convertView.findViewById(R.id.remove);

        key.setText(dataList.get(position).get("key").toString());
        value.setText(dataList.get(position).get("value").toString());

        remove.setOnClickListener(v -> {
            dataList.remove(position);
            ((ArrayList<Map<String, Object>>) arrayList.get(this.pos).get("data")).remove(position);
            FileUtils.create(this.context, "requests.json", new Gson().toJson(arrayList));
            this.notifyDataSetChanged();
        });

        return convertView;
    }

    public DataAdapter addItem(Map<String, Object> item) {
        dataList.add(item);
        return this;
    }

    public DataAdapter removeItem(int i) {
        dataList.remove(i);
        return this;
    }
}
