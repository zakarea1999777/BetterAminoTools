package com.better.amino.fragments.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.better.amino.R;
import com.better.amino.activities.ChatActivity;
import com.better.amino.adapters.ChatsAdapter;
import com.better.amino.api.Community;
import com.better.amino.api.utils.ChatUtils;
import com.better.amino.api.utils.CommunityUtils;

import java.util.ArrayList;
import java.util.Map;

public class ChatsFragment extends Fragment {

    GridView chatsView;

    // FragmentCommunitiesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_chats, container,false);

        // binding = FragmentCommunitiesBinding.inflate(getLayoutInflater());

        chatsView = view.findViewById(R.id.chats_list);

        Community community = new Community(requireActivity());
        ArrayList<Map<String, Object>> chats = community.getChats();
        ChatsAdapter chatsAdapter = new ChatsAdapter(requireContext(),chats);

        chatsView.setAdapter(chatsAdapter);
        ((BaseAdapter) chatsView.getAdapter()).notifyDataSetChanged();

        chatsView.setOnItemClickListener((parent, vie, position, id) -> {
            ChatUtils.chatId = chats.get(position).get("threadId").toString();
            startActivity(new Intent(requireActivity(), ChatActivity.class));
        });

        return view;
    }

}