package com.example.android.miveh2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {
    private static PreferenceUtils yourPreference;
    private SharedPreferences sharedPreferences;

    public static PreferenceUtils getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new PreferenceUtils(context);
        }
        return yourPreference;
    }

    private PreferenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences("Miveh2AppPreference", Context.MODE_PRIVATE);
    }

    public void save(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public String get(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public void removeAll() {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }
}

