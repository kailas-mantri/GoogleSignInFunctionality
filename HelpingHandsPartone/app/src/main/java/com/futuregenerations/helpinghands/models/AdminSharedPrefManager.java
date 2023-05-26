package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.content.SharedPreferences;

public class AdminSharedPrefManager {

    public static final String SHARED_PREF_SELECTED_THEME = "sharedprefselectedtheme";
    public static final String SHARED_PREF_NAME = "adminsharedprefmanager";

    private static AdminSharedPrefManager mInstance;
    private static Context mContext;

    public AdminSharedPrefManager(Context context) {
        mContext = context;
    }

    public static synchronized AdminSharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance =new AdminSharedPrefManager(context);
        }
        return mInstance;
    }

    public void userTheme(String themeName) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_SELECTED_THEME,themeName);
        editor.apply();
    }

    public String getTheme() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREF_SELECTED_THEME,null);
    }

    public void clearTheme() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
