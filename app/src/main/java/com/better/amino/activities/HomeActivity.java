package com.better.amino.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.better.amino.R;
import com.better.amino.fragments.home.CommunitiesFragment;
import com.better.amino.fragments.home.DeveloperFragment;
import com.better.amino.fragments.home.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView botNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new CommunitiesFragment()).commit();
        botNavView = findViewById(R.id.bottom_bar);
        botNavView.getMenu().findItem(R.id.communities).setChecked(true);
        botNavView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.communities:
                    fragment = new CommunitiesFragment();
                    break;
                case R.id.profile:
                    fragment = new ProfileFragment();
                    break;
                case R.id.developers:
                    fragment = new DeveloperFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
            return true;
        });

    }
}