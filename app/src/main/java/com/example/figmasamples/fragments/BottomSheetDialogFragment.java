package com.example.figmasamples.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.figmasamples.databinding.FragmentBottomsheetBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class BottomSheetDialogFragment extends Fragment {

    FragmentBottomsheetBinding binding;
    private String verificationId, otp;
    private FirebaseAuth mAuth;
    public BottomSheetDialogFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BottomSheetDialogFragment newInstance(String verificationId) {
        BottomSheetDialogFragment fragment = new BottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString("verificationId", verificationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            verificationId = getArguments().getString("verificationId");
        }
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBottomsheetBinding.inflate(getLayoutInflater());

        setUpOtpEditText(binding.FirstOTPeT, binding.SecondOTPeT);
        setUpOtpEditText(binding.SecondOTPeT, binding.ThirdOTPeT);
        setUpOtpEditText(binding.ThirdOTPeT, binding.FourthOTPeT);
        setUpOtpEditText(binding.FourthOTPeT, binding.FifthOTPeT);
        setUpOtpEditText(binding.FifthOTPeT, binding.SixthOTPeT);
        setUpOtpEditText(binding.SixthOTPeT, null);

        binding.verifyOtpBtn.setOnClickListener(v -> {
            otp = getEnteredOtp();
            if (!otp.isEmpty()) {
                PhoneAuthCredential authCredential = PhoneAuthProvider.getCredential(verificationId, otp);
                signInWithCredential(authCredential);
            } else {
                Toast.makeText(getContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }

    private void setUpOtpEditText(EditText currentEditText, EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Why we needed?
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Why we needed?
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1 && nextEditText != null) {
                    nextEditText.requestFocus();
                }
            }
        });
    }

    private String getEnteredOtp() {

        return binding.FirstOTPeT.getText().toString().trim() +
                binding.SecondOTPeT.getText().toString().trim() +
                binding.ThirdOTPeT.getText().toString().trim() +
                binding.FourthOTPeT.getText().toString().trim() +
                binding.FifthOTPeT.getText().toString().trim() +
                binding.SixthOTPeT.getText().toString().trim();
    }

    private void signInWithCredential(PhoneAuthCredential authCredential) {
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               // OTP verification successful, proceed with login logic
               Toast.makeText(getContext(), "OTP verification successful", Toast.LENGTH_SHORT).show();
           } else {
               // OTP verification failed
               Toast.makeText(getContext(), "OTP verification failed", Toast.LENGTH_SHORT).show();
           }
        });
    }
}