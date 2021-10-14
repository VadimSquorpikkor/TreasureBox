package com.squorpikkor.app.treasurebox;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveLoad {

    static SharedPreferences mPrefManager = PreferenceManager.getDefaultSharedPreferences(App.getContext());

    public static void save(String key, String param) {
        mPrefManager.edit().putString(key, param).apply();
    }

    public static String getString(String key) {
        if (mPrefManager.contains(key)) return mPrefManager.getString(key, "");
        return "";
    }
}
