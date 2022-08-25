package com.better.amino.activites;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.better.amino.fragments.CommunitiesFragment;
import com.better.amino.fragments.ProfileFragment;
import com.better.amino.R;
import com.better.amino.ui.SharedValue;
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
        botNavView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;

            switch (item.getItemId()){
                case R.id.communities: fragment = new CommunitiesFragment(); break;
                case R.id.profile: fragment = new ProfileFragment(); break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
            return true;
        });

    }
}