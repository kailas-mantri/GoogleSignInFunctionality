package com.scrimatec.food18.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.scrimatec.food18.R;

import java.util.Objects;

public class Change_password extends AppCompatActivity {

    TextInputEditText password, Confirm_password;
    Button savepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        password = findViewById(R.id.custom_password);
        Confirm_password = findViewById(R.id.custom_confirm_password);
        savepassword = findViewById(R.id.save_password);

        String pass = Objects.requireNonNull(password.getText()).toString();
        String cPass = Objects.requireNonNull(Confirm_password.getText()).toString();

        if (cPass.equals(pass)){
            savepassword.setClickable(true);
            if (savepassword.isClickable()) {
                savepassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Change_password.this, "password updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Change_password.this,HomeActivity.class));
                        finish();
                    }
                });
            }
        }

    }
}
