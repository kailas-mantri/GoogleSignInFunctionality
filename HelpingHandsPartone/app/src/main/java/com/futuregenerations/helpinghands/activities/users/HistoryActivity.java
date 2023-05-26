package com.futuregenerations.helpinghands.activities.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.models.PaymentHistoryAdapter;
import com.futuregenerations.helpinghands.models.UserPaymentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    RelativeLayout layout;
    TextView textViewAmount, textViewCount, textViewLastOn;
    MKLoader loader;
    ListView listView;
    UserPaymentModel paymentModel;
    List<UserPaymentModel> paymentModelList;
    Toolbar toolbar;
    DatabaseReference reference;
    String userID;

    TextView textViewOrganizationName, textViewOrganizationCategory, textViewTransactionID, textViewTransactionAmount
            , textViewTransactionDate, textViewTransactionEmail, textViewTransactionTime, textViewButtonClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        layout = findViewById(R.id.history_main_layout);
        textViewAmount = findViewById(R.id.history_donated_amount);
        textViewCount = findViewById(R.id.history_donated_count);
        textViewLastOn = findViewById(R.id.history_last_donated);
        loader = findViewById(R.id.loader);
        listView = findViewById(R.id.list_payment_history);
        paymentModelList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Payments");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser!=null) {
            userID = firebaseUser.getUid();
        }

        layout.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        getPaymentHistory();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDialogDetails(position);
            }
        });
    }

    private void getDialogDetails(int position) {
        ViewGroup viewGroup = (ViewGroup)findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(HistoryActivity.this).inflate(R.layout.custom_payment_dialog_box,viewGroup,false);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
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
                                    if (!paymentModelList.isEmpty()) {
                                        retrieveHistoryData();
                                    }
                                    PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(HistoryActivity.this,paymentModelList);
                                    listView.setAdapter(adapter);
                                    layout.setVisibility(View.VISIBLE);
                                    loader.setVisibility(View.GONE);
                                }
                            }
                            else {
                                layout.setVisibility(View.VISIBLE);
                                loader.setVisibility(View.GONE);
                                textViewAmount.setText("N/A");
                                textViewCount.setText("N/A");
                                textViewLastOn.setText("N/A");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(HistoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            layout.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.GONE);
                            textViewAmount.setText("N/A");
                            textViewCount.setText("N/A");
                            textViewLastOn.setText("N/A");
                        }
                    });
                }
                else {
                    layout.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    textViewAmount.setText("N/A");
                    textViewCount.setText("N/A");
                    textViewLastOn.setText("N/A");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HistoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                layout.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                textViewAmount.setText("N/A");
                textViewCount.setText("N/A");
                textViewLastOn.setText("N/A");
            }
        });
    }

    private void retrieveHistoryData() {
        int count = paymentModelList.size();
        int i;
        String value;
        int addNum,totalAmount = 0;
        for (i=0;i<count;i++) {
            value = paymentModelList.get(i).getPaymentDetails().getDonationAmount();
            addNum = Integer.parseInt(value);
            totalAmount = totalAmount + addNum;
        }
        textViewAmount.setText(String.valueOf(totalAmount));

        textViewCount.setText(String.valueOf(count));

        int position = count-1;
        String lastDate = paymentModelList.get(position).getPaymentDetails().getDonationDate();
        textViewLastOn.setText(lastDate);
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
