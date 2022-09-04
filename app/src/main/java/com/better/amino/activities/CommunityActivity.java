package com.better.amino.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.better.amino.R;
import com.better.amino.fragments.community.ChatsFragment;
import com.better.amino.fragments.community.HomeFragment;
import com.google.android.material.navigationrail.NavigationRailView;

public class CommunityActivity extends AppCompatActivity {

    NavigationRailView railNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        getSupportFragmentManager().beginTransaction().replace(R.id.community_fragment, new HomeFragment()).commit();
        railNavView = findViewById(R.id.navigation_rail);
        railNavView.getMenu().findItem(R.id.home_item).setChecked(true);
        railNavView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.chats_item:
                    fragment = new ChatsFragment();
                    break;

                case R.id.home_item:
                case R.id.guidelines_item:
                case R.id.profile_item:
                    fragment = new HomeFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.community_fragment, fragment).commit();
            return true;
        });
    }
}