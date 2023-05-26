package com.futuregenerations.helpinghands.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.models.AdminSharedPrefManager;
import com.futuregenerations.helpinghands.models.UserSharedPrefManager;

public class MainActivity extends AppCompatActivity {

    public static final int SPLASH_TIME_OUT = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String themeMode = AdminSharedPrefManager.getInstance(this).getTheme();
        String userThemeMode = UserSharedPrefManager.getInstance(this).getData().getThemeName();

        if (TextUtils.isEmpty(themeMode) && TextUtils.isEmpty(userThemeMode)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }
        else if (!TextUtils.isEmpty(themeMode) && TextUtils.isEmpty(userThemeMode)) {
            setAdminTheme(themeMode);
            Toast.makeText(this, "Admin", Toast.LENGTH_SHORT).show();
        }
        else if (!TextUtils.isEmpty(userThemeMode) && TextUtils.isEmpty(themeMode)) {
            setUserTheme(userThemeMode);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        },SPLASH_TIME_OUT);
    }

    private void setUserTheme(String userThemeMode) {
        if (TextUtils.equals(userThemeMode,getResources().getString(R.string.theme_dark))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else if (TextUtils.equals(userThemeMode,getResources().getString(R.string.theme_system))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        else if (TextUtils.isEmpty(userThemeMode)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void setAdminTheme(String themeMode) {
        if (TextUtils.equals(themeMode,getResources().getString(R.string.theme_dark))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else if (TextUtils.equals(themeMode,getResources().getString(R.string.theme_system))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        else if (TextUtils.isEmpty(themeMode)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
