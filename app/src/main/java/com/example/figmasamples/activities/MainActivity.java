package com.example.figmasamples.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.figmasamples.R;
import com.example.figmasamples.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    ActivityMainBinding binding;
    private FirebaseAuth mAuth = null;
    private String verificationId, phoneNumber=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
//        hiding toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

//        set the gif drawable from resources to google button
        Glide.with(this).load(R.drawable.google_icon).into(binding.googleSignImage);

//        binding.proceedBtn.setOnClickListener(v -> {
//            phoneNumber = binding.countryCode.getText().toString().trim() + binding.number.getText().toString().trim();
//            if (!phoneNumber.isEmpty()) {
//                sendOtp(phoneNumber);
//            } else {
//                Toast.makeText(MainActivity.this, "Please enter valid phone Number", Toast.LENGTH_SHORT).show();
//            }
//        });

        // Google SignIn
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        GoogleSignInClient signInClient = GoogleSignIn.getClient(this, signInOptions);

        binding.googleSignUp.setOnClickListener(v-> signInWithGoogle(signInClient));

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    private void signInWithGoogle(GoogleSignInClient signInClient) {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            verificationId = s;
//            showOtpValidationBottomSheet();
//        }
//
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential authCredential) {
//            signInWithCredential(authCredential);
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(MainActivity.this, "Verification failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    private void sendOtp(String phoneNumber) {
//        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mAuth)
//                .setPhoneNumber(phoneNumber)
//                .setTimeout(60L, TimeUnit.SECONDS)
//                .setActivity(this)
//                .setCallbacks(mCallBacks)
//                .build();
//        PhoneAuthProvider.verifyPhoneNumber(authOptions);
//    }
//
//    private void showOtpValidationBottomSheet() {
//        BottomSheetDialogFragment dialogFragment = BottomSheetDialogFragment.newInstance(verificationId);
//        dialogFragment.requireView().getRootView();
//    }
//
//    private void signInWithCredential(PhoneAuthCredential authCredential) {
//        mAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // OTP verification successful, proceed with login logic
////                String uuid = user.getUid();
////                String cell = user.getPhoneNumber();
//                Toast.makeText(MainActivity.this, "OTP verification successful", Toast.LENGTH_SHORT).show();
//            } else {
//                // OTP verification failed
//                Toast.makeText(MainActivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            if (signInAccountTask.isSuccessful()) {
                String s = "Google Sign in successful";
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                    finish();
                                    Toast.makeText(MainActivity.this, "Firebase authentication successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Authentication Failed :"+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}