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

import com.futuregenerations.helpinghands.models.AdminGetFeedbackListAdapter;
import com.futuregenerations.helpinghands.models.FeedbackDataModel;
import com.futuregenerations.helpinghands.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class AdminGetFeedbackActivity extends AppCompatActivity {

    MKLoader loader;
    ListView listView;
    TextView textView;
    List<FeedbackDataModel> dataModelList;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseReference reference;

    String userID,date,time,msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_get_feedback);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        dataModelList = new ArrayList<>();
        listView = findViewById(R.id.admin_get_feedback_list_view);
        textView = findViewById(R.id.admin_text_unable_feedback);
        loader = findViewById(R.id.loader);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        loader.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("Feedback").child(userID);

        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                child = listView;
                return child.canScrollVertically(-1);
            }
        });

        onSwipeRefreshed();
        getFeedbackData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                msg = dataModelList.get(position).getUserFeedback();
                date = dataModelList.get(position).getDate();
                time = dataModelList.get(position).getTime();
                Intent intent = new Intent(AdminGetFeedbackActivity.this,AdminFeedbackActivity.class);
                intent.putExtra("date",date);
                intent.putExtra("time",time);
                intent.putExtra("msg",msg);
                startActivity(intent);
            }
        });
    }

    private void getFeedbackData() {
        dataModelList.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FeedbackDataModel dataModel = snapshot.getValue(FeedbackDataModel.class);
                        dataModelList.add(dataModel);
                    }
                    AdminGetFeedbackListAdapter adapter = new AdminGetFeedbackListAdapter(AdminGetFeedbackActivity.this,dataModelList);
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
                Toast.makeText(AdminGetFeedbackActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                textView.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
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
                    getFeedbackData();
                }
            });
        }
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
