package com.better.amino.fragments.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.better.amino.R;
import com.dd.CircularProgressButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_home, container,false);
        return view;
    }
}
