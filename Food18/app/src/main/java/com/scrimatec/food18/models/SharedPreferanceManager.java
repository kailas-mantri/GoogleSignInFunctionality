package com.scrimatec.food18.models;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferanceManager {
    public static final String SHARED_PREF_MOBILE_NUMBER = "sharedprefmobilenumber";
    public static final String SHARED_PREF_NAME = "sharedpreferancemanager";
    public static final String SHARED_PREF_USER_ID = "sharedprefuserid";

    public static SharedPreferanceManager mInstance;
    public static Context mcontext;

    public SharedPreferanceManager(Context context){mcontext = context;}

    public static synchronized SharedPreferanceManager getInstance(Context context){
        if (mInstance == null) {
            mInstance = new SharedPreferanceManager(context);
        }
        return mInstance;
    }

    public void userLogin(SharedPreferanceData preferanceData){
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_MOBILE_NUMBER,preferanceData.getMobile_number());
        editor.putString(SHARED_PREF_USER_ID,preferanceData.getUser_id());
        editor.apply();
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences =mcontext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREF_MOBILE_NUMBER,null)!=null;
    }

    public SharedPreferanceData getUsers(){
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        return new SharedPreferanceData(
                sharedPreferences.getString(SHARED_PREF_MOBILE_NUMBER,null),
                sharedPreferences.getString(SHARED_PREF_USER_ID,null)
        );
    }

    public void logOut(){
        SharedPreferences sharedPreferences =mcontext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
