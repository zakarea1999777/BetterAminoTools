package com.better.amino.fragments.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import com.better.amino.R;
import com.better.amino.api.Global;
import com.better.amino.api.utils.AccountUtils;
import com.better.amino.ui.AnimationManager;
import com.better.amino.utils.FileUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dd.CircularProgressButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    CircleImageView icon;
    TextInputEditText nickname;
    TextInputEditText bio;
    CircularProgressButton set;
    CircleImageView camera;
    File image = null;
    String filepath;

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
        set.setProgress(0);

        set.setOnClickListener(vie -> {
            set.setProgress(0);
            if (new Global(requireActivity()).EditProfile(nickname.getText().toString(), bio.getText().toString(), image)){AnimationManager.simulateSuccessProgress(set);}
            else {AnimationManager.simulateErrorProgress(set);}
        });

        icon.setOnClickListener(vie -> {
            profileFragmentResultLauncher.launch(new Intent(Intent.ACTION_PICK).setType("image/*"), ActivityOptionsCompat.makeBasic());
        });

        return view;
    }


    ActivityResultLauncher<Intent> profileFragmentResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();

                try {
                    InputStream inputStream = requireContext().getContentResolver().openInputStream(data.getData());
                    icon.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                    inputStream.close();

                    String filePath = FileUtils.GetRealPathFromUri(data.getData(), requireActivity());
                    this.filepath = filePath;
                    image = new File(filePath);
                }
                catch (IOException ignored) {}
            }
        });
}
