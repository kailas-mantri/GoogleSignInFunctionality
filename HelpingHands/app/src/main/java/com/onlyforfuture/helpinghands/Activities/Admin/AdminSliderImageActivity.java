package com.onlyforfuture.helpinghands.Activities.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.onlyforfuture.helpinghands.R;

public class AdminSliderImageActivity extends AppCompatActivity {

    Toolbar toolbar;

    ImageView uploadimage;
    TextInputEditText texttitle, textid, textdescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_slider_image);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        texttitle = findViewById(R.id.admin_slider_images_title);
        textid = findViewById(R.id.admin_slider_images_id);
        textdescription = findViewById(R.id.admin_slider_image_description);
        uploadimage = findViewById(R.id.admin_slider_images_upload);
    }

    public void admin_slider_image_upload(View view) {

    }
}
