package com.better.amino.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.better.amino.R;
import com.better.amino.adapters.RequestVPAdapter;
import com.better.amino.databinding.ActivityRequestBinding;
import com.better.amino.requests.DevelopersRequestNetwork;
import com.better.amino.utils.FileUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {

    private final String[] titles = new String[]{"Request", "Response"};
    Map<String, Object> response;
    private ActivityRequestBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RequestVPAdapter requestVPAdapter;
    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        topAppBar = findViewById(R.id.topAppBar);

        DevelopersRequestNetwork.context = this;

        Intent intent = getIntent();

        ArrayList<Map<String, Object>> arrayList;
        arrayList = new Gson().fromJson(FileUtils.read(this, "requests.json"), new TypeToken<ArrayList<Map<String, Object>>>() {
        }.getType());

        int i = 0;
        for (Map<String, Object> item : arrayList) {
            if (item.get("name") == intent.getStringExtra("name")) {
                break;
            }
            i++;
        }
        i = i - 1;
        Map<String, Object> ma = arrayList.get(i);
        binding.autoCompleteTextView.setText(ma.get("method").toString());
        binding.textInputEditText.setText(ma.get("url").toString());
        response = (Map<String, Object>) ma.get("response");

        requestVPAdapter = new RequestVPAdapter(this);
        viewPager.setAdapter(requestVPAdapter);

        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setText(titles[position]))).attach();

        String[] methods = getResources().getStringArray(R.array.methods);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.method_item, methods);
        binding.autoCompleteTextView.setAdapter(arrayAdapter);

        int finalI = i;
        topAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.send:

                    arrayList.clear();
                    arrayList.addAll(new Gson().fromJson(FileUtils.read(this, "requests.json"), new TypeToken<ArrayList<Map<String, Object>>>() {
                    }.getType()));
                    Map<String, Object> m = arrayList.get(finalI);
                    m.remove("url");
                    m.remove("method");
                    m.put("method", binding.autoCompleteTextView.getText().toString());
                    m.put("url", binding.textInputEditText.getText().toString());
                    m.put("response", response);
                    arrayList.remove(finalI);
                    arrayList.add(m);
                    FileUtils.create(this, "requests.json", new Gson().toJson(arrayList));

                    Map<String, Object> req = arrayList.get(finalI);
                    Map<String, Object> requestData = new HashMap<>();

                    switch (req.get("method").toString()) {
                        case "POST":
                            for (Map<String, Object> dataItem : (ArrayList<Map<String, Object>>) req.get("data")) {
                                requestData.put(dataItem.get("key").toString(), dataItem.get("value").toString());
                            }
                            response = DevelopersRequestNetwork.post(req.get("url").toString(), requestData);
                            break;
                        case "GET":
                            response = DevelopersRequestNetwork.get(req.get("url").toString());
                        case "DELETE":
                            response = DevelopersRequestNetwork.delete(req.get("url").toString());
                    }

                    viewPager.setAdapter(requestVPAdapter);
                    viewPager.setCurrentItem(1, true);

                    arrayList.clear();
                    arrayList.addAll(new Gson().fromJson(FileUtils.read(this, "requests.json"), new TypeToken<ArrayList<Map<String, Object>>>() {
                    }.getType()));
                    Map<String, Object> map1 = arrayList.get(finalI);
                    map1.remove("url");
                    map1.remove("method");
                    map1.put("method", binding.autoCompleteTextView.getText().toString());
                    map1.put("url", binding.textInputEditText.getText().toString());
                    map1.put("response", response);
                    arrayList.remove(finalI);
                    arrayList.add(map1);
                    FileUtils.create(this, "requests.json", new Gson().toJson(arrayList));

                    return true;
                case R.id.save:
                    arrayList.clear();
                    arrayList.addAll(new Gson().fromJson(FileUtils.read(this, "requests.json"), new TypeToken<ArrayList<Map<String, Object>>>() {
                    }.getType()));
                    Map<String, Object> map = arrayList.get(finalI);
                    map.remove("url");
                    map.remove("method");
                    map.put("method", binding.autoCompleteTextView.getText().toString());
                    map.put("url", binding.textInputEditText.getText().toString());
                    map.put("response", response);
                    arrayList.remove(finalI);
                    arrayList.add(map);
                    FileUtils.create(this, "requests.json", new Gson().toJson(arrayList));
                    return true;
            }
            return false;
        });

        binding.textInputLayout.getEditText().setOnClickListener(v -> {
            viewPager.setCurrentItem(0, true);
        });

        ((AutoCompleteTextView) binding.textInputLayout.getEditText()).setOnItemClickListener((adapterView, view1, position, id) -> {
            String selectedValue = arrayAdapter.getItem(position).toString();

            if (selectedValue.equals("POST")) {
                viewPager.findViewById(R.id.linearDefault).setVisibility(View.VISIBLE);
            } else {

                viewPager.findViewById(R.id.linearDefault).setVisibility(View.GONE);
            }
        });
    }

    public String getResponse() {
        return new Gson().toJson(response);
    }
}