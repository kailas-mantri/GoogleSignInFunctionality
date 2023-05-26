package com.futuregenerations.helpinghands.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.futuregenerations.helpinghands.models.AdminUserListAdapter;
import com.futuregenerations.helpinghands.models.UserDataModel;
import com.futuregenerations.helpinghands.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class AdminUsersActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    TextView textView;
    List<UserDataModel> modelList;
    MKLoader loader;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);

        listView = findViewById(R.id.admin_user_list);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        textView = findViewById(R.id.admin_users_text_no_user);
        loader = findViewById(R.id.loader);

        modelList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");
        loader.setVisibility(View.VISIBLE);
        getUsersList();

        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                child = listView;
                return child.canScrollVertically(-1);
            }
        });

        onSwipeRefreshed();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userID = modelList.get(position).getUserId();
                Intent userIntent = new Intent(AdminUsersActivity.this,AdminUserDetailsActivity.class);
                userIntent.putExtra("userID",userID);
                startActivity(userIntent);
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
                    getUsersList();
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    private void getUsersList() {
        modelList.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserDataModel dataModel = snapshot.getValue(UserDataModel.class);
                        modelList.add(dataModel);
                    }
                    AdminUserListAdapter adapter = new AdminUserListAdapter(AdminUsersActivity.this,modelList);
                    listView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                    textView.setVisibility(View.GONE);
                    loader.setVisibility(View.GONE);
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminUsersActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
