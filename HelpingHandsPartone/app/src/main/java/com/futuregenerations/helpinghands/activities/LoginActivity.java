package com.futuregenerations.helpinghands.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.futuregenerations.helpinghands.activities.admin.AdminHomeActivity;
import com.futuregenerations.helpinghands.activities.admin.AdminLoginActivity;
import com.futuregenerations.helpinghands.activities.users.HomeActivity;
import com.futuregenerations.helpinghands.models.AdminSharedPrefManager;
import com.futuregenerations.helpinghands.models.UserDataModel;
import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.models.UserSharedPrefData;
import com.futuregenerations.helpinghands.models.UserSharedPrefManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;

    MKLoader loader;

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;

    DatabaseReference reference;

    private final String userId = "2y6TjvqXCjYcZktnC887xKcu9ef2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loader = findViewById(R.id.loader);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user!=null) {
            uid = user.getUid();
            if (uid.equals(userId)) {
                startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        }
    }

    public void googleLogin(View view) {
        signIn();
    }

    public void adminLogin(View view) {
        startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class));
        finish();
    }

    @Override

    public void onStart() {
        super.onStart();

    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);

                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }

            } catch (ApiException e) {

                Log.w(TAG, "Google sign in failed", e);

            }

        }

    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        loader.setVisibility(View.VISIBLE);

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            reference = FirebaseDatabase.getInstance().getReference("Users");

                            if (user != null) {
                                String userName = acct.getDisplayName();
                                String userEmail = acct.getEmail();
                                String userImage = String.valueOf(acct.getPhotoUrl());
                                String userId = user.getUid();
                                checkUser(userName,userEmail,userImage,userId);

                            }


                        } else {

                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                            loader.setVisibility(View.GONE);
                        }
                    }

                });

    }

    private void checkUser(final String userName, final String userEmail, final String userImage, final String userId) {
        DatabaseReference checkReference = FirebaseDatabase.getInstance().getReference("Users");

        checkReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)) {
                    loader.setVisibility(View.GONE);
                    UserSharedPrefData data = new UserSharedPrefData(getResources().getString(R.string.theme_light),getResources().getString(R.string.allow_notification));
                    UserSharedPrefManager.getInstance(LoginActivity.this).userData(data);
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    finish();
                }
                else {
                    UserDataModel storeFirebaseUser = new UserDataModel(userId,userName,userImage,userEmail,"","","","","");
                    reference.child(userId).setValue(storeFirebaseUser);
                    loader.setVisibility(View.GONE);
                    UserSharedPrefData data = new UserSharedPrefData(getResources().getString(R.string.theme_light),getResources().getString(R.string.allow_notification));
                    UserSharedPrefManager.getInstance(LoginActivity.this).userData(data);
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
}
