package com.better.amino.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.better.amino.R;
import com.better.amino.activities.CommunityActivity;
import com.better.amino.adapters.CommunitiesAdapter;
import com.better.amino.api.Global;
import com.better.amino.api.utils.CommunityUtils;

public class CommunitiesFragment extends Fragment {

    GridView communityView;

    // FragmentCommunitiesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communities, container,false);

        // binding = FragmentCommunitiesBinding.inflate(getLayoutInflater());

        communityView = view.findViewById(R.id.community_list);

        Global community = new Global(requireActivity());
        CommunitiesAdapter communitiesAdapter = new CommunitiesAdapter(requireContext(), community.getCommunities());

        communityView.setAdapter(communitiesAdapter);
        ((BaseAdapter) communityView.getAdapter()).notifyDataSetChanged();

        communityView.setOnItemClickListener((parent, vie, position, id) -> {
            CommunityUtils.community = community.getCommunities().get(position);
            startActivity(new Intent(requireActivity(), CommunityActivity.class));
        });

        return view;
    }

}