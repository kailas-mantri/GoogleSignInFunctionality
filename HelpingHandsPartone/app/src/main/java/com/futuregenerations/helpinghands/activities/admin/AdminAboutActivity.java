package com.futuregenerations.helpinghands.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.futuregenerations.helpinghands.R;

public class AdminAboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_about);
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
