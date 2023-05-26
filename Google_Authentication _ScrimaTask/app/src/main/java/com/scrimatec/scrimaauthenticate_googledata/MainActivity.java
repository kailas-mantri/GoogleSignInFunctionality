package com.scrimatec.scrimaauthenticate_googledata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 1;
    SignInButton signInButton;
    GoogleSignInClient googleApiClient;
    TextView view;
    String name, email, token;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view=findViewById(R.id.email);
        signInButton = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();

        //this where we start the Firebase state listener to listener for whether user sign in or not
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            //if the user is Signed in we call helper method to save the user details to firebase
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                //User is Signed In
                Toast.makeText(MainActivity.this, "Sign_in"+user.getUid(), Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this, "Signed_out", Toast.LENGTH_SHORT).show();
            }

            GoogleSignInOptions google_sign_in_op =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            googleApiClient = GoogleSignIn.getClient(MainActivity.this, google_sign_in_op);

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent signInIntent = googleApiClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });
            }
        };
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){

        if(result.isSuccess()){
             GoogleSignInAccount account = result.getSignInAccount();
             token = account.getIdToken();
             name = account.getDisplayName();
             email = account.getEmail();

            // you can store user data to SharedPreference
            AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
            firebaseAuthWithGoogle(credential);

        }else{

            // Google Sign In failed, update UI appropriately
            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }
    private void firebaseAuthWithGoogle(AuthCredential credential){
    mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
    {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

            FirebaseUser user =mAuth.getCurrentUser();

            if(task.isSuccessful()){

                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                gotoProfile();
            }
            else
                {
                Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
                task.getException().printStackTrace();
                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        }
        });
    }

    private void gotoProfile(){

        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {

        super.onStart();
        if (authStateListener != null){
            FirebaseAuth.getInstance().signOut();
        }
        mAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {

        super.onStop();

        if (authStateListener != null){
            mAuth.removeAuthStateListener(authStateListener);
        }

    }
}