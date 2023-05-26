package com.example.figmasamples;

import android.app.Dialog;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.figmasamples.databinding.ActivityMainBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
    private FirebaseAuth mAuth;
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

        binding.proceedBtn.setOnClickListener(v -> {
            if (!binding.cellNumber.getText().toString().isEmpty()) {
                String phoneNumber = binding.cellNumber.getText().toString();
                showOtpDialog();
                sendOtp(phoneNumber);
            } else {
                Toast.makeText(MainActivity.this, "Please enter valid phone Number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //    This method is used have an otp to users phoneNumber.
    private void sendOtp(String number) {

//         PhoneNumber to have, Timeout and it's unit, for callback binding, on verificationState changed callback.
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.MILLISECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks).build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);

    }

    //    callback method to phoneAuth provider.
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // This method is called when user receive otp.
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            signInWithCredentials(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // this method is used when otp is sent from firebase.
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationId = s;
//          Storing the otp unique id into String variable.
//            showOtpDialog();
        }
    };

    private void showOtpDialog() {
        Dialog bottomSheetDialog = new BottomSheetDialog(getApplicationContext());
        bottomSheetDialog.setContentView(R.layout.otp_verification_dialog);
        bottomSheetDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.show();

        EditText firstOtp, secondOtp, thirdOtp, fourthOtp, fifthOtp, sixthOtp;
        AppCompatButton verifyOtp;

        firstOtp = bottomSheetDialog.findViewById(R.id.FirstOTPeT);
        secondOtp = bottomSheetDialog.findViewById(R.id.SecondOTPeT);
        thirdOtp = bottomSheetDialog.findViewById(R.id.ThirdOTPeT);
        fourthOtp = bottomSheetDialog.findViewById(R.id.FourthOTPeT);
        fifthOtp = bottomSheetDialog.findViewById(R.id.FifthOTPeT);
        sixthOtp = bottomSheetDialog.findViewById(R.id.sixOTPeT);
        verifyOtp = bottomSheetDialog.findViewById(R.id.verifyOtpBtn);

        verifyOtp.setOnClickListener(view -> {
            if ((firstOtp != null) && (secondOtp != null) && (thirdOtp != null) && (fourthOtp != null) && (fifthOtp != null) && (sixthOtp != null)) {
                String otp = firstOtp.getText().toString() + secondOtp.getText().toString() + thirdOtp.getText().toString() + fourthOtp.getText().toString() + fifthOtp.getText().toString() + sixthOtp.getText().toString();
                if (otp.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                    signInWithCredentials(credential);
                    bottomSheetDialog.dismiss();
                }
            }
        });

    }

    private void signInWithCredentials(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                FirebaseUser user = task.getResult().getUser();
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Authentication Failed" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}