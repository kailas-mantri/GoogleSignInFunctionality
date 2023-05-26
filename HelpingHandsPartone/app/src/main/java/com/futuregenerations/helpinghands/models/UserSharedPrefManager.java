package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSharedPrefManager {

    public static final String USER_SHARED_PREF_SELECTED_THEME = "usersharedprefselectedtheme";
    public static final String USER_SHARED_PREF_NAME = "usersharedprefmanager";
    public static final String USER_SHARED_PREF_NOTIFICATION_STATUS = "usersharedprefnotificationstatus";

    private static UserSharedPrefManager mInstance;
    private static Context mContext;

    public UserSharedPrefManager(Context context) {
        mContext = context;
    }

    public static synchronized UserSharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance =new UserSharedPrefManager(context);
        }
        return mInstance;
    }

    public void userData(UserSharedPrefData data) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_SHARED_PREF_SELECTED_THEME,data.getThemeName());
        editor.putString(USER_SHARED_PREF_NOTIFICATION_STATUS,data.getNotificationStatus());
        editor.apply();
    }

    public UserSharedPrefData getData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_SHARED_PREF_NAME,Context.MODE_PRIVATE);

        return new UserSharedPrefData(
                sharedPreferences.getString(USER_SHARED_PREF_SELECTED_THEME,null),
                sharedPreferences.getString(USER_SHARED_PREF_NOTIFICATION_STATUS,null)
        );
    }

    public void clearData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
