package com.example.figmasamples.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.figmasamples.R;
import com.example.figmasamples.databinding.ActivityMainBinding;
import com.example.figmasamples.fragments.BottomSheetDialogFragment;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = null;
    private DatabaseReference dbReference;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        hiding toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

//        set the gif drawable from resources to google button
        Glide.with(this).load(R.drawable.google_icon).into(binding.googleSignImage);

        String ccp = binding.countryCode.getText().toString();
        String phoneNumber = ccp + binding.number.getText().toString();

        System.out.println(phoneNumber);

        binding.proceedBtn.setOnClickListener(v -> {
            if (!phoneNumber.isEmpty()) {
                sendOtp(phoneNumber);
            } else {
                Toast.makeText(MainActivity.this, "Please enter valid phone Number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOtp(String phoneNumber) {

        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this).setCallbacks(mCallBacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential authCredential) {
                signInWithCredential(authCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(MainActivity.this, "Verification failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verificationId = s;
                showOtpValidationBottomSheet();
            }
        };
    }

    private void signInWithCredential(PhoneAuthCredential authCredential) {
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // OTP verification successful, proceed with login logic
                Toast.makeText(MainActivity.this, "OTP verification successful", Toast.LENGTH_SHORT).show();
            } else {
                // OTP verification failed
                Toast.makeText(MainActivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showOtpValidationBottomSheet() {
        BottomSheetDialogFragment dialogFragment = BottomSheetDialogFragment.newInstance(verificationId);
        dialogFragment.requireView().getRootView();
    }

}