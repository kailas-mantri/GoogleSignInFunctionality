package com.example.figmasamples.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.figmasamples.R;
import com.example.figmasamples.databinding.ActivityMainBinding;
import com.example.figmasamples.fragments.BottomSheetDialogFragment;
import com.example.figmasamples.model.StoreFirebaseUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "GoogleSignIn :", PHONETAG = "PhoneAuthProvider :";
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    DatabaseReference reference;
    ActivityMainBinding binding;
    private String verificationId, phoneNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();

        Glide.with(this).load(R.drawable.google_icon).into(binding.googleSignImage);        // set the gif drawable from resources to google button

        binding.proceedBtn.setOnClickListener(v -> {
            phoneNumber = binding.countryCode.getText().toString().trim() + binding.number.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                sendOtp(phoneNumber);
            } else {
                Toast.makeText(MainActivity.this, "Please enter valid phone Number", Toast.LENGTH_SHORT).show();
            }
        });

        //PhoneAuthProvider onVerificationState change callback
//        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
////                signInWithPhoneAuthCredential(credential);
//                Log.d(PHONETAG, "onVerificationCompleted: "+credential);
//                showOtpValidationBottomSheet();
//            }
//
//            @Override
//            public void onVerificationFailed(@NonNull FirebaseException e) {
//                Log.d(PHONETAG, "onVerificationFailed", e);
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    Log.d(PHONETAG, "onVerificationFailed", e);
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    Log.d(PHONETAG, "onVerificationFailed", e);
//                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException)
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
//                super.onCodeSent(s, token);
//                Log.d(PHONETAG, "onCodeSent: "+s);
//                verificationId = s;
//                mResendToken = token;
//            }
//        };

        // Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.googleSignUp.setOnClickListener(v -> signInWithGoogle(mGoogleSignInClient));

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Please provide the user", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "no currentUser found");
        }
    }

    private void sendOtp(String phoneNumber) {
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS).setActivity(this)
                .setCallbacks(mCallBacks).build();
        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }

    private void showOtpValidationBottomSheet() {
        BottomSheetDialogFragment dialogFragment = BottomSheetDialogFragment.newInstance(verificationId, phoneNumber);
        dialogFragment.requireView().getRootView();
    }

    private void signInWithGoogle(GoogleSignInClient inClient) {
        Intent signInIntent = inClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(accountTask);
        } else {
            Log.e(TAG, "Else Block check with request Code");
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> signInAccountTask) {
        try {
            GoogleSignInAccount inAccount = signInAccountTask.getResult(ApiException.class);
            Log.e(TAG, "Firebase Authorization with Google: "+inAccount.getIdToken());
            firebaseAuthWithGoogle(inAccount.getIdToken());
        } catch (ApiException e) {
            Log.e(TAG, "signResult:failed code= " + e.getMessage());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential mAuthCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(mAuthCredential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("Users");

                if (user != null) {
                    String userName = user.getDisplayName();
                    String userEmail = user.getEmail();
                    String userId = user.getUid();
                    checkUser(userId, userName, userEmail);
                }

            } else {
                Log.e(TAG, "Sign in with is failed", task.getException());
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUser(String userId, String userName, String userEmail) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userId)) {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    Log.d(TAG, "Successful login");
                    finish();
                } else {
                    StoreFirebaseUser firebaseUser = new StoreFirebaseUser(userId, userName, userEmail);
                    dbReference.child(userId).setValue(firebaseUser);
                    Log.d(TAG, "Sign in with is Successful");
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
