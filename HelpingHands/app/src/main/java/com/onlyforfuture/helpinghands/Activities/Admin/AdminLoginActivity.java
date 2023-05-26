package com.onlyforfuture.helpinghands.Activities.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.onlyforfuture.helpinghands.R;

public class AdminLoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    String email, pass;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.admin_login_email);
        etPassword =findViewById(R.id.admin_login_password);
    }

    public void admin_login_to_home(View view) {

//        email = etEmail.getText().toString();
//        pass = etPassword.getText().toString();
//
//        if (email.equals("") || pass.equals("")){
//            Toast.makeText(this, "Please fill the Correct details", Toast.LENGTH_SHORT).show();
//            Log.e("AdminLogin","FALSE");
//        }
//        else {
//            mAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                @Override
//                public void onSuccess(AuthResult authResult) {
//                    startActivity(new Intent(AdminLoginActivity.this, AdminHomeActivity.class));
//                    finish();
//                }
//            });
//        }

        startActivity(new Intent(AdminLoginActivity.this, AdminHomeActivity.class));
        finish();
    }
}
