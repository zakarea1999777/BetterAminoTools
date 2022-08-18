package com.better.amino.ui;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.better.amino.R;
import com.google.android.material.textview.MaterialTextView;

public class ToastManager {

    /* Show Toast Message */

    public static void makeToast(Activity context, String text){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_layout, null);
        MaterialTextView textView = (MaterialTextView) layout.findViewById(R.id.textView);
        textView.setText(text);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
