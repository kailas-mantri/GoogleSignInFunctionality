package com.futuregenerations.helpinghands.activities.admin;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.activities.users.HistoryActivity;
import com.futuregenerations.helpinghands.models.PaymentHistoryAdapter;
import com.futuregenerations.helpinghands.models.UserPaymentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class AdminPaymentDetailsActivity extends AppCompatActivity {

    TextView textViewTitle;
    String name,userID;

    SwipeRefreshLayout swipeRefreshLayout;
    MKLoader loader;
    ListView listView;
    UserPaymentModel paymentModel;
    List<UserPaymentModel> paymentModelList;
    DatabaseReference reference;
    Toolbar toolbar;

    TextView textViewOrganizationName, textViewOrganizationCategory, textViewTransactionID, textViewTransactionAmount
            , textViewTransactionDate, textViewTransactionEmail, textViewTransactionTime, textViewButtonClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_payment_details);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        textViewTitle = findViewById(R.id.textTitle);
        loader = findViewById(R.id.loader);
        listView = findViewById(R.id.admin_list_payment_history);
        paymentModelList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Payments");

        Intent intent = getIntent();
        name = intent.getStringExtra("userName");
        userID = intent.getStringExtra("userID");

        textViewTitle.setText(name);

        swipeRefreshLayout.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                child = listView;
                return child.canScrollVertically(-1);
            }
        });
        onSwipeRefreshed();
        getPaymentHistory();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDialogDetails(position);
            }
        });
    }

    private void onSwipeRefreshed() {
        if (swipeRefreshLayout.canChildScrollUp()) {
            swipeRefreshLayout.setEnabled(false);
        }
        else {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(true);
                    getPaymentHistory();
                }
            });
        }
    }

    private void getPaymentHistory() {
        paymentModelList.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    paymentModel = snapshot.getValue(UserPaymentModel.class);
                                    paymentModelList.add(paymentModel);
                                    PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(AdminPaymentDetailsActivity.this,paymentModelList);
                                    listView.setAdapter(adapter);
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                                    loader.setVisibility(View.GONE);
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                            else {
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                                loader.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AdminPaymentDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.GONE);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
                else {
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminPaymentDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    private void getDialogDetails(int position) {
        ViewGroup viewGroup = (ViewGroup)findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(AdminPaymentDetailsActivity.this).inflate(R.layout.custom_payment_dialog_box,viewGroup,false);

        textViewButtonClose = dialogView.findViewById(R.id.btn_ok);
        textViewOrganizationCategory = dialogView.findViewById(R.id.dialog_organization_category);
        textViewOrganizationName = dialogView.findViewById(R.id.dialog_organization_name);
        textViewTransactionAmount = dialogView.findViewById(R.id.dialog_amount);
        textViewTransactionDate = dialogView.findViewById(R.id.dialog_date);
        textViewTransactionEmail = dialogView.findViewById(R.id.dialog_email);
        textViewTransactionID = dialogView.findViewById(R.id.dialog_transaction_id);
        textViewTransactionTime = dialogView.findViewById(R.id.dialog_time);

        textViewOrganizationCategory.setText(paymentModelList.get(position).getOrganizationDetails().getOrganizationCategory());
        textViewOrganizationName.setText(paymentModelList.get(position).getOrganizationDetails().getOrganizationTitle());
        textViewTransactionAmount.setText(paymentModelList.get(position).getPaymentDetails().getDonationAmount());
        textViewTransactionDate.setText(paymentModelList.get(position).getPaymentDetails().getDonationDate());
        textViewTransactionEmail.setText(paymentModelList.get(position).getUserDetails().getUserEmail());
        textViewTransactionID.setText(paymentModelList.get(position).getPaymentDetails().getTransactionID());
        textViewTransactionTime.setText(paymentModelList.get(position).getPaymentDetails().getDonationTime());

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPaymentDetailsActivity.this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        textViewButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }
}
