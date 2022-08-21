package com.better.amino.ui;

import android.app.Activity;
import android.content.SharedPreferences;

public class SharedValue {
    private Activity context;

    public SharedValue(Activity activity){
        context = activity;
    }

    public void saveString(String name, String value){
        SharedPreferences shared = context.getSharedPreferences("shared", 0);
        SharedPreferences.Editor sharedEditor = shared.edit();
        sharedEditor.putString(name, value);
        sharedEditor.apply();
    }

    public void saveBoolean(String name, boolean value){
        SharedPreferences shared = context.getSharedPreferences("shared", 0);
        SharedPreferences.Editor sharedEditor = shared.edit();
        sharedEditor.putBoolean(name, value);
        sharedEditor.apply();
    }

    public String getString(String name){
        SharedPreferences shared = context.getSharedPreferences("shared", 0);
        return shared.getString(name, "");
    }

    public boolean getBoolean(String name){
        SharedPreferences shared = context.getSharedPreferences("shared", 0);
        return shared.getBoolean(name, false);
    }

    public void removeValue(String name){
        SharedPreferences shared = context.getSharedPreferences("shared", 0);
        SharedPreferences.Editor sharedEditor = shared.edit();
        sharedEditor.remove(name);
        sharedEditor.apply();
    }
}
