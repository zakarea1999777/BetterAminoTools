package com.better.amino.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.better.amino.fragments.requests.RequestFragment;
import com.better.amino.fragments.requests.ResponseFragment;

public class RequestVPAdapter extends FragmentStateAdapter {

    private String[] titles = new String[]{"Request", "Response"};

    public RequestVPAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new RequestFragment();
            case 1:
                return new ResponseFragment();
        }
        return new RequestFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
