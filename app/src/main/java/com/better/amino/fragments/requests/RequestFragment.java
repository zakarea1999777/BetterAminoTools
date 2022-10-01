package com.better.amino.fragments.requests;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.better.amino.R;
import com.better.amino.adapters.DataAdapter;
import com.better.amino.utils.FileUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestFragment extends Fragment {

    AutoCompleteTextView autoCompleteTextView;
    TextInputLayout textInputLayout;
    TextInputLayout textInputLayoutData;
    ListView listView;
    FloatingActionButton fab;
    DataAdapter dataAdapter;
    TextInputEditText textInputEditTextKey;
    TextInputEditText textInputEditTextValue;
    LinearLayout linearLayout;
    LinearLayout linearDefault;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        textInputLayout = view.findViewById(R.id.textInputLayout);
        textInputLayoutData = view.findViewById(R.id.textInputLayoutData);
        listView = view.findViewById(R.id.listView);
        fab = view.findViewById(R.id.fab);
        textInputEditTextKey = view.findViewById(R.id.textInputEditTextKey);
        textInputEditTextValue = view.findViewById(R.id.textInputEditTextValue);
        linearLayout = view.findViewById(R.id.linear);
        linearDefault = view.findViewById(R.id.linearDefault);

        Intent intent = getActivity().getIntent();

        ArrayList<Map<String, Object>> arrayList;
        arrayList = new Gson().fromJson(FileUtils.read(requireContext(), "requests.json"), new TypeToken<ArrayList<Map<String, Object>>>() {
        }.getType());

        int i = 0;
        for (Map<String, Object> item : arrayList) {
            if (item.get("name") == intent.getStringExtra("name")) {
                break;
            }
            i++;
        }
        i = i - 1;
        dataAdapter = new DataAdapter(requireContext(), (ArrayList<Map<String, Object>>) arrayList.get(i).get("data"), arrayList, i - 1);
        listView.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        autoCompleteTextView.setText(arrayList.get(i).get("dataType").toString());

        if (arrayList.get(i).get("method").toString().equals("POST")) {
            linearDefault.setVisibility(View.VISIBLE);
        } else {
            linearDefault.setVisibility(View.GONE);
        }

        if (arrayList.get(i).get("dataType").toString().equals("JSON")) {
            listView.setVisibility(View.VISIBLE);
            textInputLayoutData.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.GONE);
            textInputLayoutData.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }

        String[] data_types = getResources().getStringArray(R.array.data_types);
        ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.method_item, data_types);
        autoCompleteTextView.setAdapter(arrayAdapter);

        int finalI = i;
        ((AutoCompleteTextView) textInputLayout.getEditText()).setOnItemClickListener((adapterView, view1, position, id) -> {
            String selectedValue = arrayAdapter.getItem(position).toString();
            arrayList.get(finalI).remove("dataType");

            if (selectedValue.equals("JSON")) {
                listView.setVisibility(View.VISIBLE);
                textInputLayoutData.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                arrayList.get(finalI).put("dataType", "JSON");
            } else {
                listView.setVisibility(View.GONE);
                textInputLayoutData.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                arrayList.get(finalI).put("dataType", "JSON String");
            }
            FileUtils.create(requireContext(), "requests.json", new Gson().toJson(arrayList));
        });

        fab.setOnClickListener(view1 -> {
            Map<String, Object> map = new HashMap<>();
            map.put("key", textInputEditTextKey.getText().toString());
            map.put("value", textInputEditTextValue.getText().toString());
            dataAdapter.addItem(map);
            dataAdapter.notifyDataSetChanged();
            ((ArrayList<Map<String, Object>>) arrayList.get(finalI).get("data")).add(map);
            FileUtils.create(requireContext(), "requests.json", new Gson().toJson(arrayList));
        });

        return view;
    }
}