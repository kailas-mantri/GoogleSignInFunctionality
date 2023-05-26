package com.futuregenerations.helpinghands.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.futuregenerations.helpinghands.models.GetOrganizationCategoryModel;
import com.futuregenerations.helpinghands.models.GetOrganizationsDataModel;
import com.futuregenerations.helpinghands.models.OrganizationsListAdapter;
import com.futuregenerations.helpinghands.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class AdminOrganizationsActivity extends AppCompatActivity {

    String categoryID;
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    List<GetOrganizationsDataModel> dataModelList;
    ListView listView;
    TextView textView;
    MKLoader loader;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_organizations);

        listView = findViewById(R.id.admin_show_organizations_list);
        textView = findViewById(R.id.text_no_data_organizations);
        loader = findViewById(R.id.loader);

        dataModelList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        reference = FirebaseDatabase.getInstance().getReference("Organizations");

        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                return findViewById(R.id.admin_show_organizations_list).canScrollVertically(-1);
            }
        });
        onSwipeRefreshed();
        checkAvailableOrganizations();

        Intent intent = getIntent();
        categoryID = intent.getStringExtra("categoryID");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String orgID = dataModelList.get(position).getOrganizationID();
                String orgType = dataModelList.get(position).getOrganizationType();
                Intent editIntent = new Intent(AdminOrganizationsActivity.this,AdminEditOrganizationsActivity.class);
                editIntent.putExtra("orgID",orgID);
                editIntent.putExtra("orgType",orgType);
                startActivity(editIntent);
            }
        });
    }

    private void checkAvailableOrganizations() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    getOrganizationsDataToList();
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminOrganizationsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOrganizationsDataToList() {
        dataModelList.clear();
        reference.child(categoryID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GetOrganizationsDataModel dataModel = snapshot.getValue(GetOrganizationsDataModel.class);
                        dataModelList.add(dataModel);
                    }
                    OrganizationsListAdapter adapter = new OrganizationsListAdapter(AdminOrganizationsActivity.this,dataModelList);
                    listView.setAdapter(adapter);
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    textView.setVisibility(View.GONE);
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminOrganizationsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
                    checkAvailableOrganizations();
                }
            });
        }
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.admin_menu_organizations_category,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        switch (itemID) {

            case R.id.admin_menu_update:
                Intent updateIntent = new Intent(AdminOrganizationsActivity.this,AdminEditOrganizationCategoryActivity.class);
                updateIntent.putExtra("categoryID",categoryID);
                startActivity(updateIntent);
                break;

            case R.id.admin_menu_delete:
                deleteOrganizationCategory();
                break;

            case R.id.admin_menu_add:
                Intent intent = new Intent(AdminOrganizationsActivity.this,AdminAddOrganizationsActivity.class);
                intent.putExtra("categoryID",categoryID);
                startActivity(intent);
                break;
        }

        return true;
    }

    private void deleteOrganizationCategory() {
        if (textView.getVisibility()==View.VISIBLE) {
            if (dataModelList.isEmpty()) {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Delete Category")
                        .setMessage("Are you sure you want to delete this organization category?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                deleteCategory();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setCancelable(false)
                        .setIcon(R.drawable.logo)
                        .create();
                alertDialog.show();
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Delete Category")
                        .setMessage("Unable to delete category as organization are available in this category.")
                        .setCancelable(false)
                        .setIcon(R.drawable.logo)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();

                alertDialog.show();
            }
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Delete Category")
                    .setMessage("Unable to delete category as organization are available in this category.")
                    .setCancelable(false)
                    .setIcon(R.drawable.logo)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();

            alertDialog.show();
        }
    }

    private void deleteCategory() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("OrganizationCategory");
        databaseReference.child(categoryID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GetOrganizationCategoryModel categoryModel = dataSnapshot.getValue(GetOrganizationCategoryModel.class);
                String imageUrl = categoryModel.getCategoryImageURL();
                delete(imageUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void delete(String imageUrl) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DatabaseReference deleteReference = FirebaseDatabase.getInstance().getReference("OrganizationCategory");
                deleteReference.child(categoryID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AdminOrganizationsActivity.this, "Category Deleted Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                    }
                });
            }
        });
    }
}
