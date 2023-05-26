package com.onlyforfuture.helpinghands.Activities.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyforfuture.helpinghands.Models.UserOrganizationListAdapter;
import com.onlyforfuture.helpinghands.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onlyforfuture.helpinghands.Models.GetOrganizationListDataModel;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listOrganizations;
    List<GetOrganizationListDataModel> dataModelList;
    TextView textView;
    MKLoader loader;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean isFavorites;

    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser user;

    String categoryID,organizationID,userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        dataModelList = new ArrayList<>();

        loader = findViewById(R.id.loader);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user!=null) {
            userID = user.getUid();
        }
        reference = FirebaseDatabase.getInstance().getReference("UserFavorites");
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        textView= findViewById(R.id.user_text_no_data);

        listOrganizations = findViewById(R.id.list_favorites_organizations);

        loader.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                child = listOrganizations;
                return child.canScrollVertically(-1);
            }
        });

        onSwipeRefreshed();
        getOrganizationsData();


        listOrganizations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                organizationID = dataModelList.get(position).getOrganizationID();
                categoryID = dataModelList.get(position).getOrganizationType();
                checkIfFavorites();
            }
        });
    }

    private void getOrganizationsData() {
        dataModelList.clear();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GetOrganizationListDataModel dataModel = snapshot.getValue(GetOrganizationListDataModel.class);
                        dataModelList.add(dataModel);
                    }
                    UserOrganizationListAdapter adapter = new UserOrganizationListAdapter(FavouritesActivity.this,dataModelList);
                    listOrganizations.setAdapter(adapter);
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FavouritesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                loader.setVisibility(View.GONE);
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
                    getOrganizationsData();
                }
            });
        }
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    private void checkIfFavorites() {

        loader.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild(userID)) {
                        if (dataSnapshot.child(userID).hasChild(organizationID)) {
                            loader.setVisibility(View.GONE);
                            isFavorites = true;
                            gotoNextActivity();
                        }
                        else {
                            loader.setVisibility(View.GONE);
                            isFavorites = false;
                            gotoNextActivity();
                        }
                    }
                    else {
                        loader.setVisibility(View.GONE);
                        isFavorites = false;
                        gotoNextActivity();
                    }
                }
                else {
                    loader.setVisibility(View.GONE);
                    isFavorites = false;
                    gotoNextActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gotoNextActivity() {
        Intent organizationIntent = new Intent(FavouritesActivity.this,OrganisationDetailActivity.class);
        organizationIntent.putExtra("organizationID",organizationID);
        organizationIntent.putExtra("categoryID",categoryID);
        organizationIntent.putExtra("isFavorites",isFavorites);
        startActivity(organizationIntent);
    }
}
