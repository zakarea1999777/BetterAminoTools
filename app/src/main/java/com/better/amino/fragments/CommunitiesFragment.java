package com.better.amino.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.better.amino.R;
import com.better.amino.adapters.CommunitiesAdapter;
import com.better.amino.api.Community;
import com.better.amino.databinding.FragmentCommunitiesBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunitiesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    GridView communityView;
    FragmentCommunitiesBinding binding;

    public CommunitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunitiesFragment newInstance(String param1, String param2) {
        CommunitiesFragment fragment = new CommunitiesFragment();
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
        View view = inflater.inflate(R.layout.fragment_communities, container,false);

        binding = FragmentCommunitiesBinding.inflate(getLayoutInflater());
        communityView = view.findViewById(R.id.community_list);

        Community community = new Community();
        CommunitiesAdapter communitiesAdapter = new CommunitiesAdapter(requireContext(), community.getCommunities());

        //binding.communityList.setAdapter(communitiesAdapter);
        //((BaseAdapter) binding.communityList.getAdapter()).notifyDataSetChanged();
        //binding.communityList.invalidate();

        communityView.setAdapter(communitiesAdapter);
        ((BaseAdapter) communityView.getAdapter()).notifyDataSetChanged();

        return view;
    }

}