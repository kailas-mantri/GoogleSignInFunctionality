    package com.scrimatec.scrimaauthenticate_googledata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

    public class ProfileActivity extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener {

    Button logoutBtn;
    TextView userName,userEmail,userId;
    String stringUsername, stringEmail, stringUserID;
    ImageView profileImage;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions google_sign_in_op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

            logoutBtn = findViewById(R.id.logoutBtn);
            userName = findViewById(R.id.name);
            userEmail = findViewById(R.id.email);
            userId = findViewById(R.id.userId);
            profileImage = findViewById(R.id.profileImage);

            google_sign_in_op =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this,this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API,google_sign_in_op)
                    .build();


            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                        if (status.isSuccess())
                        {
                            gotoMainActivity();
                        } else
                            Toast.makeText(getApplicationContext(),"Session not close",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

        @Override
        protected void onStart() {
            super.onStart();

            OptionalPendingResult<GoogleSignInResult> optional_pending_result= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
            if(optional_pending_result.isDone()){
                GoogleSignInResult result=optional_pending_result.get();
                handleSignInResult(result);
            } else {
                optional_pending_result.setResultCallback(new ResultCallback<GoogleSignInResult>() {

                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
        private void handleSignInResult(GoogleSignInResult result){
            if(result.isSuccess()){

                GoogleSignInAccount account=result.getSignInAccount();

                userName.setText(account.getDisplayName());
                userEmail.setText(account.getEmail());
                userId.setText(account.getId());

                stringUsername = account.getDisplayName();
                stringEmail = account.getEmail();
                stringUserID = account.getId();

                HashMap hashMap = new HashMap<String,String>();

                hashMap.put("Username",stringUsername);
                hashMap.put("Email",stringEmail);
                hashMap.put("UserID",stringUserID);

                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();

                String uid = mAuth.getCurrentUser().getUid();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Google-Users");
                reference.child(uid).setValue(hashMap);

                try { Glide.with(this).load(account.getPhotoUrl()).into(profileImage); }

                catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
                }
            }
            else { gotoMainActivity(); }
        }
        private void gotoMainActivity(){

            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}