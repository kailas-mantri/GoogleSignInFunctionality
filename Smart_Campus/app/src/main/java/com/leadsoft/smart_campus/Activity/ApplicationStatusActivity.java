package com.leadsoft.smart_campus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.leadsoft.smart_campus.R;

public class ApplicationStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_status);
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}