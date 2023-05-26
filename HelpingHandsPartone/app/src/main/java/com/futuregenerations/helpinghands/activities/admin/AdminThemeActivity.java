package com.futuregenerations.helpinghands.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.models.AdminSharedPrefManager;

public class AdminThemeActivity extends AppCompatActivity {

    RelativeLayout layoutLight, layoutDark, layoutSystem;
    RadioButton buttonLight, buttonDark, buttonSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_theme);

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
        String themeMode = AdminSharedPrefManager.getInstance(this).getTheme();

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
        AdminSharedPrefManager.getInstance(AdminThemeActivity.this).userTheme(getResources().getString(R.string.theme_light));
    }

    private void setDarkTheme() {
        buttonLight.setChecked(false);
        buttonDark.setChecked(true);
        buttonSystem.setChecked(false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        AdminSharedPrefManager.getInstance(AdminThemeActivity.this).userTheme(getResources().getString(R.string.theme_dark));
    }

    private void setSystemTheme() {
        buttonLight.setChecked(false);
        buttonDark.setChecked(false);
        buttonSystem.setChecked(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AdminSharedPrefManager.getInstance(AdminThemeActivity.this).userTheme(getResources().getString(R.string.theme_system));
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
