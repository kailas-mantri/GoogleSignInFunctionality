package com.futuregenerations.helpinghands.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.models.AdminSharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tuyenmonkey.mkloader.MKLoader;

public class AdminLoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    TextInputEditText etEmail,etPass;
    String emailID, password;
    MKLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_password);
        loader = findViewById(R.id.loader);
        if (firebaseUser!=null) {
            startActivity(new Intent(AdminLoginActivity.this,AdminHomeActivity.class));
            finish();
        }
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    public void loginAdmin(final View view) {

        loader.setVisibility(View.VISIBLE);

        emailID = etEmail.getText().toString();
        password = etPass.getText().toString();

        if (TextUtils.isEmpty(emailID)) {
            etEmail.setError("Please Provide Admin Email");
            etEmail.requestFocus();
            loader.setVisibility(View.GONE);
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            etPass.setError("Please Provide Admin Password");
            etPass.requestFocus();
            loader.setVisibility(View.GONE);
            return;
        }
        else {
            mAuth.signInWithEmailAndPassword(emailID,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminLoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.GONE);
                        AdminSharedPrefManager.getInstance(AdminLoginActivity.this).userTheme(getResources().getString(R.string.theme_light));
                        startActivity(new Intent(AdminLoginActivity.this,AdminHomeActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(AdminLoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
