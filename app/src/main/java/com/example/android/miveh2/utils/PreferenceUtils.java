package com.example.android.miveh2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
    private static PreferenceUtils instance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor prefsEditor;
    Context context;

    public static PreferenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceUtils(context);
            sharedPreferences = context.getSharedPreferences("Miveh2AppPreference", Context.MODE_PRIVATE);
            prefsEditor = sharedPreferences.edit();
        }
        return instance;
    }

    private PreferenceUtils(Context context) {
        this.context = context;
    }

    public void save(String key, String value) {

        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }


    public void save(String key, boolean value) {

        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    public String get(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public boolean getbollean(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, false);
        }
        return false;
    }

 /*   public void save(String key, Integer value) {

        prefsEditor.putInt(key, value);
        prefsEditor.apply();
    }

    public Integer getInt(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, 0);
        }
        return 0;
    }*/

    public void removeAll() {
        prefsEditor.clear();
        prefsEditor.apply();
    }
}

