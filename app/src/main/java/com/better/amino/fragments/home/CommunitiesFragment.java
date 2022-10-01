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
import com.better.amino.activities.SearchCommunityActivity;
import com.better.amino.adapters.CommunitiesAdapter;
import com.better.amino.api.Community;
import com.better.amino.api.Global;
import com.better.amino.api.utils.CommunityUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Map;

public class CommunitiesFragment extends Fragment {

    GridView communityView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communities, container, false);

        communityView = view.findViewById(R.id.community_list);

        Global global = new Global(requireActivity());
        ArrayList<Map<String, Object>> communities = global.getCommunities();
        CommunitiesAdapter communitiesAdapter = new CommunitiesAdapter(requireContext(), communities);

        communityView.setAdapter(communitiesAdapter);
        ((BaseAdapter) communityView.getAdapter()).notifyDataSetChanged();

        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(requireContext())

                .setTitle(getResources().getString(R.string.warning))
                .setMessage(getResources().getString(R.string.leave_community_description))

                .setNeutralButton(getResources().getString(R.string.cancel), (dialog1, id) -> dialog1.cancel());

        communityView.setOnItemClickListener((parent, vie, position, id) -> {
            if (position != communitiesAdapter.getCount() - 1) {
                CommunityUtils.community = communities.get(position);
                startActivity(new Intent(requireActivity(), CommunityActivity.class));
            } else {
                startActivity(new Intent(requireContext(), SearchCommunityActivity.class));
            }
        });

        communityView.setOnItemLongClickListener((parent, vie, position, id) -> {
            if (position != communitiesAdapter.getCount() - 1) {
                dialog.setPositiveButton(getString(R.string.leave), (dialog1, dialogId) -> {

                    CommunityUtils.community = communities.get(position);
                    Community community = new Community(requireActivity());
                    community.LeaveCommunity();
                    communitiesAdapter.removeItem(position).notifyDataSetChanged();

                });
                dialog.show();
            }
            return true;
        });

        return view;
    }
}