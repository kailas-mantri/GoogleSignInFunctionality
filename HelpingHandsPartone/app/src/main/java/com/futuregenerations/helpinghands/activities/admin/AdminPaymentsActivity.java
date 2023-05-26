package com.futuregenerations.helpinghands.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.models.AdminPaymentsUserListAdapter;
import com.futuregenerations.helpinghands.models.UserPaymentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class AdminPaymentsActivity extends AppCompatActivity {

    TextView textView;
    ListView listView;
    MKLoader loader;
    SwipeRefreshLayout swipeRefreshLayout;
    List<UserPaymentModel> paymentModelList;
    DatabaseReference reference;
    List<String> count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_payments);

        reference = FirebaseDatabase.getInstance().getReference("Payments");
        textView = findViewById(R.id.admin_text_no_payment);
        listView = findViewById(R.id.admin_payment_user_list);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        paymentModelList = new ArrayList<>();
        count = new ArrayList<>();
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);

        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                child = listView;
                return child.canScrollVertically(-1);
            }
        });

        onSwipeRefreshed();
        getUserData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToNextActivity(position);
            }
        });
    }

    private void goToNextActivity(int position) {
        Intent intent = new Intent(AdminPaymentsActivity.this,AdminPaymentDetailsActivity.class);
        intent.putExtra("userID",paymentModelList.get(position).getUserDetails().getUserId());
        intent.putExtra("userName",paymentModelList.get(position).getUserDetails().getUserName());
        startActivity(intent);
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
                    getUserData();
                }
            });
        }
    }

    private void getUserData() {
        paymentModelList.clear();
        count.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot finalSnapshot : snapshot.getChildren()) {
                            UserPaymentModel model = finalSnapshot.getValue(UserPaymentModel.class);
                            paymentModelList.add(model);
                            long num = 0;
                            num = snapshot.getChildrenCount();
                            String value = String.valueOf(num);
                            count.add(value);
                            break;
                        }
                    }

                    AdminPaymentsUserListAdapter adapter = new AdminPaymentsUserListAdapter(AdminPaymentsActivity.this,paymentModelList,count);
                    listView.setAdapter(adapter);
                    textView.setVisibility(View.GONE);
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminPaymentsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                textView.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
