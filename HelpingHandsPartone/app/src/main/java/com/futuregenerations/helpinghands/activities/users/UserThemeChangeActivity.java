package com.futuregenerations.helpinghands.activities.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.activities.admin.AdminThemeActivity;
import com.futuregenerations.helpinghands.models.AdminSharedPrefManager;
import com.futuregenerations.helpinghands.models.UserSharedPrefData;
import com.futuregenerations.helpinghands.models.UserSharedPrefManager;

public class UserThemeChangeActivity extends AppCompatActivity {

    RelativeLayout layoutLight, layoutDark, layoutSystem;
    RadioButton buttonLight, buttonDark, buttonSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_theme_change);

        layoutSystem = findViewById(R.id.layout_system);
        layoutDark = findViewById(R.id.layout_dark);
        layoutLight = findViewById(R.id.layout_light);

        buttonSystem = findViewById(R.id.radio_button_system);
        buttonDark = findViewById(R.id.radio_button_dark);
        buttonLight = findViewById(R.id.radio_button_light);

        getSelectedTheme();

        layoutLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLightTheme();
            }
        });

        layoutDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDarkTheme();
            }
        });

        layoutSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSystemTheme();
            }
        });

    }

    private void getSelectedTheme() {
        String themeMode = UserSharedPrefManager.getInstance(this).getData().getThemeName();

        if (TextUtils.equals(themeMode,getResources().getString(R.string.theme_dark))) {
            setDarkTheme();
        }
        else if (TextUtils.equals(themeMode,getResources().getString(R.string.theme_system))) {
            setSystemTheme();
        }
        else if (TextUtils.isEmpty(themeMode)){
            setLightTheme();
        }
        else {
            setLightTheme();
        }
    }

    private void setLightTheme() {
        buttonLight.setChecked(true);
        buttonDark.setChecked(false);
        buttonSystem.setChecked(false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        String notification = UserSharedPrefManager.getInstance(this).getData().getNotificationStatus();
        UserSharedPrefData data = new UserSharedPrefData(getResources().getString(R.string.theme_light),notification);
        UserSharedPrefManager.getInstance(UserThemeChangeActivity.this).userData(data);
    }

    private void setDarkTheme() {
        buttonLight.setChecked(false);
        buttonDark.setChecked(true);
        buttonSystem.setChecked(false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        String notification = UserSharedPrefManager.getInstance(this).getData().getNotificationStatus();
        UserSharedPrefData data = new UserSharedPrefData(getResources().getString(R.string.theme_dark),notification);
        UserSharedPrefManager.getInstance(UserThemeChangeActivity.this).userData(data);
    }

    private void setSystemTheme() {
        buttonLight.setChecked(false);
        buttonDark.setChecked(false);
        buttonSystem.setChecked(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        String notification = UserSharedPrefManager.getInstance(this).getData().getNotificationStatus();
        UserSharedPrefData data = new UserSharedPrefData(getResources().getString(R.string.theme_system),notification);
        UserSharedPrefManager.getInstance(UserThemeChangeActivity.this).userData(data);
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
