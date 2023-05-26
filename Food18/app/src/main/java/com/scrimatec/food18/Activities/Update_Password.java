package com.scrimatec.food18.Activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.scrimatec.food18.R;

public class Update_Password extends AppCompatActivity {

    TextInputEditText old_pass, new_pass, confirm_pass;
    Button update_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__password);

        old_pass = findViewById(R.id.old_password);
        new_pass = findViewById(R.id.custom_change_password);
        confirm_pass = findViewById(R.id.custom_change_confirm_password);

        update_button = findViewById(R.id.update_password);

//        update_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }
}
