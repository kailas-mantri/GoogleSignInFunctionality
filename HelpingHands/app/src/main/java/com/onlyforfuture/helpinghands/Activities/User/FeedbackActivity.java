package com.onlyforfuture.helpinghands.Activities.User;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onlyforfuture.helpinghands.Models.StoreFirebaseFeedback;
import com.onlyforfuture.helpinghands.Models.StoreFirebaseUser;
import com.onlyforfuture.helpinghands.Models.StoreFirebaseUserFeedback;
import com.onlyforfuture.helpinghands.R;
import com.tuyenmonkey.mkloader.MKLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FeedbackActivity extends AppCompatActivity {

    EditText editTextFeedback;
    Button submit_feedback;

    DatabaseReference reference, feedbackReference, userReference;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    MKLoader loader;
    String uID = "";

    String userName, userImage, userID, userEmail,feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        loader = findViewById(R.id.loader);

        editTextFeedback = findViewById(R.id.etFeedback);
        submit_feedback = findViewById(R.id.btnSubmitFeedback);

        feedbackReference = FirebaseDatabase.getInstance().getReference("FeedbackUser");
        reference = FirebaseDatabase.getInstance().getReference("Feedback");
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        uID = firebaseUser.getUid();

        submit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = editTextFeedback.getText().toString();
                if (TextUtils.isEmpty(feedback)) {
                    Toast.makeText(FeedbackActivity.this, "Please write a feedback before submitting", Toast.LENGTH_SHORT).show();
                }
                else {
                    loader.setVisibility(View.VISIBLE);
                    getUserData();
                }
            }
        });
    }

    private void getUserData() {
        userReference.child(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    StoreFirebaseUser getData = dataSnapshot.getValue(StoreFirebaseUser.class);
                    userName = getData.getUserName();
                    userID = getData.getUserId();
                    userImage = getData.getUserImage();
                    userEmail = getData.getUserEmail();
                    checkReference();
                }
                else {
                    Toast.makeText(FeedbackActivity.this, "Session Timed Out Please Login Again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FeedbackActivity.this, "Failed to load data... Please try again", Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }
        });
    }

    private void checkReference() {
        StoreFirebaseUserFeedback userFeedback = new StoreFirebaseUserFeedback(userName,userEmail,userID,userImage);
        feedbackReference.child(userID).setValue(userFeedback).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                storeFeedback();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FeedbackActivity.this, "Falied To Submit Feedback", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeFeedback() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
        String currentTime = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
        String formattedDate = df.format(c.getTime());
        String passValue = formattedDate+currentTime;
        Date date = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        String saveDate = dateFormat.format(date);
        String saveTime = timeFormat.format(date);

        StoreFirebaseFeedback firebaseFeedback = new StoreFirebaseFeedback(feedback,saveDate,saveTime);
        reference.child(userID).child(passValue).setValue(firebaseFeedback).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FeedbackActivity.this, "Feedback Submitted Successfully", Toast.LENGTH_SHORT).show();
                editTextFeedback.setText("");
                loader.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FeedbackActivity.this, "Failed To Submit Feedback...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }

}



