package com.futuregenerations.helpinghands.activities.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.models.UserPaymentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

public class AdminDashboardActivity extends AppCompatActivity {

    UserPaymentModel paymentModel;
    DatabaseReference userReference, amountReference, organizationReference;
    long userCount = 0, organizationCount = 0, donationCount = 0;
    TextView textViewUser, textViewAmount, textViewOrganization;
    RelativeLayout layout;
    MKLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        amountReference = FirebaseDatabase.getInstance().getReference("Payments");
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        organizationReference = FirebaseDatabase.getInstance().getReference("Organizations");
        textViewUser = findViewById(R.id.text_total_users);
        textViewAmount = findViewById(R.id.text_total_donation);
        textViewOrganization = findViewById(R.id.text_total_organization);
        layout = findViewById(R.id.admin_dashboard_layout);
        loader = findViewById(R.id.loader);
        layout.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        getAllData();
    }

    private void getAllData() {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userCount = dataSnapshot.getChildrenCount();
                    textViewUser.setText(String.valueOf(userCount));
                }
                else {
                    textViewUser.setText("0");
                }
                getDonationCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDonationCount() {
        amountReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot finalSnapshot : snapshot.getChildren()) {
                            paymentModel = finalSnapshot.getValue(UserPaymentModel.class);
                            String amount = paymentModel.getPaymentDetails().getDonationAmount();
                            long count = Long.parseLong(amount);
                            donationCount = donationCount + count;
                        }
                    }
                    textViewAmount.setText(String.valueOf(donationCount));
                }
                else {
                    textViewAmount.setText("0");
                }
                getOrganizationCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOrganizationCount() {
        organizationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        long count = snapshot.getChildrenCount();
                        organizationCount = organizationCount + count;
                    }
                    textViewOrganization.setText(String.valueOf(organizationCount));
                    loader.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                }
                else {
                    textViewOrganization.setText("0");
                    loader.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
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
}
