package com.onlyforfuture.helpinghands.Activities.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onlyforfuture.helpinghands.Models.GetOrganizationListDataModel;
import com.onlyforfuture.helpinghands.Models.organisationListAdapter;
import com.onlyforfuture.helpinghands.R;

import java.util.ArrayList;
import java.util.List;

public class OrganizationsListActivity extends AppCompatActivity {

    ListView organisationList;
    Toolbar toolbar;

    DatabaseReference reference;
    List<GetOrganizationListDataModel> organizationListDataModels;
    String CategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation_list);

        toolbar = findViewById(R.id.toolbar);
        organizationListDataModels = new ArrayList<>();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        organisationList = findViewById(R.id.list_view_for_organisation);
        reference = FirebaseDatabase.getInstance().getReference("Organizations");
        Intent intent = getIntent();
        CategoryId = intent.getStringExtra("CategoryId");

        organisationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ID = organizationListDataModels.get(position).getOrganizationID();
                Intent intent = new Intent(OrganizationsListActivity.this, OrganizationDetailsActivity.class);
                intent.putExtra("ID",ID);
                startActivity(intent);
            }
        });
        getOrganizationsToList();
    }

    private void getOrganizationsToList() {
        organizationListDataModels.clear();
        reference.child(CategoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GetOrganizationListDataModel dataModel = snapshot.getValue(GetOrganizationListDataModel.class);
                        organizationListDataModels.add(dataModel);
                    }
                    organisationListAdapter adapter = new organisationListAdapter(OrganizationsListActivity.this,organizationListDataModels);
                    organisationList.setAdapter(adapter);
                }
                else {
                    Toast.makeText(OrganizationsListActivity.this, "No List Available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrganizationsListActivity.this, "List Not Found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
