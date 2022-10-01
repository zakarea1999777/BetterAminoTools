package com.better.amino.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.better.amino.R;
import com.better.amino.adapters.CommunitiesSearchAdapter;
import com.better.amino.api.Community;
import com.better.amino.api.Global;
import com.better.amino.api.utils.CommunityUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SearchCommunityActivity extends AppCompatActivity {

    private final long DELAY = 1000;
    CommunitiesSearchAdapter communitiesSearchAdapter;
    GridView communityView;
    TextInputEditText search;
    ArrayList<Map<String, Object>> communities;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_community);

        communityView = findViewById(R.id.community_search_list);
        search = findViewById(R.id.search_community);

        Global global = new Global(this);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null)
                    timer.cancel();
            }

            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> {
                            communities = global.searchCommunities(search.getText().toString());
                            communitiesSearchAdapter = new CommunitiesSearchAdapter(SearchCommunityActivity.this, communities);
                            communityView.setAdapter(communitiesSearchAdapter);
                            ((BaseAdapter) communityView.getAdapter()).notifyDataSetChanged();
                        });
                    }
                }, DELAY);
            }
        });

        communityView.setOnItemClickListener((parent, vie, position, id) -> {
            CommunityUtils.community = communities.get(position);
            Community community = new Community(this);
            community.JoinCommunity();
            startActivity(new Intent(this, CommunityActivity.class));
        });
    }
}