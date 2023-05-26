package com.futuregenerations.helpinghands.activities.users;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.futuregenerations.helpinghands.models.FeedbackDataModel;
import com.futuregenerations.helpinghands.models.FeedbackUserDataModel;
import com.futuregenerations.helpinghands.models.UserDataModel;
import com.futuregenerations.helpinghands.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FeedbackActivity extends AppCompatActivity {

    EditText editTextFeedback;

    DatabaseReference reference,userReference, feedbackReference;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    MKLoader loader;

    String userID = "";
    String feedback="";
    String userName, userEmail, userImage;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loader = findViewById(R.id.loader);

        editTextFeedback = findViewById(R.id.etFeedback);

        reference = FirebaseDatabase.getInstance().getReference().child("FeedbackUser");
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        feedbackReference = FirebaseDatabase.getInstance().getReference("Feedback");

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        userID = firebaseUser.getUid();
        getFirebaseUser();
    }

    private void getFirebaseUser() {
        userReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserDataModel model = dataSnapshot.getValue(UserDataModel.class);
                    userImage = model.getUserImage();
                    userEmail = model.getUserEmail();
                    userName = model.getUserName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    public void submitFeedback(View view) {

        feedback = editTextFeedback.getText().toString();

        if (TextUtils.isEmpty(feedback)) {
            Toast.makeText(this, "Please write a feedback before submitting", Toast.LENGTH_SHORT).show();
        }
        else {
            loader.setVisibility(View.VISIBLE);
            StoreFeedbackUser();
        }
    }

    private void StoreFeedbackUser() {
        FeedbackUserDataModel userDataModel = new FeedbackUserDataModel(userName,userEmail,userImage,userID);
        reference.child(userID).setValue(userDataModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                StoreFeedback();
            }
        });
    }

    private void StoreFeedback() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String currentTime = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
        String formattedDate = df.format(c.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String todayDate = dateFormat.format(c.getTime());
        String todayTime = new SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(new Date());

        String passValue = formattedDate+currentTime;

        FeedbackDataModel feedbackDataModel = new FeedbackDataModel(feedback,todayDate,todayTime);
        feedbackReference.child(userID).child(passValue).setValue(feedbackDataModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FeedbackActivity.this, "Feedback Submitted Successfully", Toast.LENGTH_SHORT).show();
                editTextFeedback.setText("");
                loader.setVisibility(View.GONE);
            }
        });
    }
}
