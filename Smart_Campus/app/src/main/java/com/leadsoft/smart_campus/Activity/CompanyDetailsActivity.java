package com.leadsoft.smart_campus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.leadsoft.smart_campus.R;

public class CompanyDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
    }

    public void image_back(View view) {
        onBackPressed();
    }
}