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

import com.futuregenerations.helpinghands.models.AdminFeedbackUserListAdapter;
import com.futuregenerations.helpinghands.models.FeedbackUserDataModel;
import com.futuregenerations.helpinghands.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class AdminFeedbackUserActivity extends AppCompatActivity {

    MKLoader loader;
    ListView listView;
    TextView textView;
    List<FeedbackUserDataModel> dataModelList;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_feedback_user);

        dataModelList = new ArrayList<>();
        listView = findViewById(R.id.admin_feedback_list_view);
        textView = findViewById(R.id.admin_text_no_feedback);
        loader = findViewById(R.id.loader);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        loader.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("FeedbackUser");

        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                child = listView;
                return child.canScrollVertically(-1);
            }
        });

        onSwipeRefreshed();
        getFeedbackUserData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userID = dataModelList.get(position).getUserID();
                Intent intent = new Intent(AdminFeedbackUserActivity.this,AdminGetFeedbackActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
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
                    getFeedbackUserData();
                }
            });
        }
    }

    private void getFeedbackUserData() {
        dataModelList.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FeedbackUserDataModel dataModel = snapshot.getValue(FeedbackUserDataModel.class);
                        dataModelList.add(dataModel);
                    }
                    AdminFeedbackUserListAdapter adapter = new AdminFeedbackUserListAdapter(AdminFeedbackUserActivity.this,dataModelList);
                    listView.setAdapter(adapter);
                    textView.setVisibility(View.GONE);
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminFeedbackUserActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                textView.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
