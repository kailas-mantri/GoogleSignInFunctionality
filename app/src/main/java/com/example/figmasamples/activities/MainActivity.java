package com.example.figmasamples.activities;

import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.figmasamples.R;
import com.example.figmasamples.databinding.ActivityMainBinding;
import com.example.figmasamples.fragments.BottomSheetDialogFragment;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FirebaseAuth mAuth = null;
    private String verificationId, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        hiding toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

//        set the gif drawable from resources to google button
        Glide.with(this).load(R.drawable.google_icon).into(binding.googleSignImage);
        mAuth = FirebaseAuth.getInstance();

        binding.proceedBtn.setOnClickListener(v -> {
            phoneNumber = binding.countryCode.getText().toString().trim() + binding.number.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                sendOtp(phoneNumber);
            } else {
                Toast.makeText(MainActivity.this, "Please enter valid phone Number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            showOtpValidationBottomSheet();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential authCredential) {
            signInWithCredential(authCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(MainActivity.this, "Verification failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void sendOtp(String phoneNumber) {
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallBacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }

    private void showOtpValidationBottomSheet() {
        BottomSheetDialogFragment dialogFragment = BottomSheetDialogFragment.newInstance(verificationId);
        dialogFragment.requireView().getRootView();
    }

    private void signInWithCredential(PhoneAuthCredential authCredential) {
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // OTP verification successful, proceed with login logic
//                String uuid = user.getUid();
//                String cell = user.getPhoneNumber();
                Toast.makeText(MainActivity.this, "OTP verification successful", Toast.LENGTH_SHORT).show();
            } else {
                // OTP verification failed
                Toast.makeText(MainActivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}