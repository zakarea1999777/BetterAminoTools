package com.better.amino.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.better.amino.R;
import com.better.amino.api.Global;
import com.better.amino.api.utils.AccountUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CircleImageView icon;
    TextInputEditText nickname;
    TextInputEditText bio;
    MaterialButton set;
    CircleImageView camera;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container,false);
        icon = view.findViewById(R.id.profile_icon);
        nickname = view.findViewById(R.id.nickname);
        bio = view.findViewById(R.id.bio);
        set = view.findViewById(R.id.set_btn);
        camera = view.findViewById(R.id.camera_icon);

        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher_round).error(R.mipmap.ic_launcher_round);
        
        String url = "";

        try {url = AccountUtils.icon.replace("http", "https");}
        catch (Exception ignored) {}
         
        Glide.with(requireContext()).load(Uri.parse(url)).apply(options).into(icon);

        nickname.setText(AccountUtils.nickname);
        bio.setText(AccountUtils.bio);

        set.setOnClickListener(vie -> {
            new Global(requireActivity()).EditProfile(nickname.getText().toString(), bio.getText().toString());
            icon.setBorderColor(Color.parseColor("#4BB543"));
            camera.setBorderColor(Color.parseColor("#4BB543"));
        });

        return view;
    }
}
