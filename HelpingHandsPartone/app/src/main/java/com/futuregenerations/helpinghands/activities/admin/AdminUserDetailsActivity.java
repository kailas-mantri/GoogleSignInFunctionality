package com.futuregenerations.helpinghands.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.models.UserDataModel;
import com.futuregenerations.helpinghands.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

public class AdminUserDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    RelativeLayout relativeLayout;
    CoordinatorLayout coordinatorLayout;
    MKLoader loader;
    ImageView imageView;
    TextView textViewName, textViewDate, textViewAddress, textViewCity, textViewState, textViewMobile, textViewEmail, textViewID;
    String intentDataID;
    String userID, userName, userEmail, userDate, userMobile, userAddress, userCity, userState, userImageURL;
    DatabaseReference reference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent getIntent = getIntent();
        intentDataID = getIntent.getStringExtra("userID");

        coordinatorLayout = findViewById(R.id.admin_user_details_main_layout);
        relativeLayout = findViewById(R.id.admin_user_details_relative_layout);
        loader = findViewById(R.id.loader);

        loader.setVisibility(View.VISIBLE);
        coordinatorLayout.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);

        imageView = findViewById(R.id.admin_user_details_user_image);
        textViewName = findViewById(R.id.admin_user_details_text_full_name);
        textViewAddress = findViewById(R.id.admin_user_details_text_user_address);
        textViewCity = findViewById(R.id.admin_user_details_text_user_city);
        textViewDate = findViewById(R.id.admin_user_details_text_date_of_birth);
        textViewEmail = findViewById(R.id.admin_user_details_text_user_email);
        textViewState = findViewById(R.id.admin_user_details_text_user_state);
        textViewMobile = findViewById(R.id.admin_user_details_text_user_mobile);
        textViewID = findViewById(R.id.admin_user_details_text_user_id);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        getUserDetails();
    }

    private void getUserDetails() {
        reference.child(intentDataID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserDataModel dataModel = dataSnapshot.getValue(UserDataModel.class);
                    userAddress = dataModel.getUserAddress();
                    userCity = dataModel.getUserCity();
                    userDate = dataModel.getUserDate();
                    userEmail = dataModel.getUserEmail();
                    userID = dataModel.getUserId();
                    userImageURL = dataModel.getUserImage();
                    userMobile = dataModel.getUserMobile();
                    userName = dataModel.getUserName();
                    userState = dataModel.getUserState();
                    getDetailsToView();
                }
                else {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(AdminUserDetailsActivity.this, "Unable to get details. Please try again later...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminUserDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDetailsToView() {
        textViewID.setText(userID);
        if (TextUtils.isEmpty(userMobile)) {
            textViewMobile.setText("N/A");
        }
        else {
            textViewMobile.setText(userMobile);
        }
        if (TextUtils.isEmpty(userState)) {
            textViewState.setText("N/A");
        }
        else {
            textViewState.setText(userState);
        }
        if (TextUtils.isEmpty(userCity)) {
            textViewCity.setText("N/A");
        }
        else {
            textViewCity.setText(userCity);
        }
        if (TextUtils.isEmpty(userDate)) {
            textViewDate.setText("N/A");
        }
        else {
            textViewDate.setText(userDate);
        }
        if (TextUtils.isEmpty(userAddress)) {
            textViewAddress.setText("N/A");
        }
        else {
            textViewAddress.setText(userAddress);
        }
        textViewEmail.setText(userEmail);
        textViewName.setText(userName);
        toolbar.setTitle(userName);
        String oldImageQuality = "=s96-c";
        String newImageQuality = "";
        String userImage = userImageURL.replace(oldImageQuality,newImageQuality);
        Glide.with(AdminUserDetailsActivity.this)
                .load(userImage)
                .into(imageView);

        loader.setVisibility(View.GONE);
        coordinatorLayout.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
