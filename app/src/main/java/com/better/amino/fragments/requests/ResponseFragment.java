package com.better.amino.fragments.requests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.better.amino.R;
import com.better.amino.activities.RequestActivity;
import com.google.android.material.textview.MaterialTextView;

public class ResponseFragment extends Fragment {

    MaterialTextView textResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_response, container, false);

        textResponse = view.findViewById(R.id.textResponse);

        RequestActivity activity = (RequestActivity) getActivity();
        String response = activity.getResponse();

        textResponse.setText(response);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        RequestActivity activity = (RequestActivity) getActivity();
        String response = activity.getResponse();

        textResponse.setText(response);
    }
}