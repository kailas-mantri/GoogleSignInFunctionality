package com.futuregenerations.helpinghands.activities.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.models.UserFavoritesModel;
import com.futuregenerations.helpinghands.models.GetOrganizationsDataModel;
import com.futuregenerations.helpinghands.models.UserOrganizationImagesAdapter;
import com.futuregenerations.helpinghands.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class OrganizationDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    String organizationID,categoryID;
    GridView gridView;
    ImageView imageView;
    TextView textViewType, textViewLocation, textViewDescription, textViewTitle;
    String organizationType, organizationTitle, organizationLocation, organizationDescription,
            organizationLogo,organizationCategory;
    DatabaseReference reference, favoritesReference;
    MKLoader loader;
    List<String> extraImages;
    RelativeLayout layout;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userID;
    boolean isFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layout = findViewById(R.id.user_organization_details_main_layout);
        layout.setVisibility(View.GONE);
        loader = findViewById(R.id.loader);
        extraImages = new ArrayList<>();
        textViewTitle = findViewById(R.id.toolbarTextTitle);
        textViewType = findViewById(R.id.organization_type);
        textViewDescription = findViewById(R.id.organization_description);
        textViewLocation = findViewById(R.id.organization_location);
        imageView = findViewById(R.id.image_organizations_logo);
        gridView = findViewById(R.id.user_organization_images_grid_view);

        Intent organizationIntent = getIntent();
        organizationID = organizationIntent.getStringExtra("organizationID");
        categoryID = organizationIntent.getStringExtra("categoryID");
        isFavorites = organizationIntent.getBooleanExtra("isFavorites",false);

        setToolbarTheme();

        reference = FirebaseDatabase.getInstance().getReference("Organizations").child(categoryID);
        favoritesReference = FirebaseDatabase.getInstance().getReference("UserFavorites");
        getOrganizationDetails();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user!=null) {
            userID = user.getUid();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrganizationDetailsActivity.this,OrganizationViewImagesActivity.class);
                intent.putStringArrayListExtra("extraImages", (ArrayList<String>) extraImages);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }

    private void setToolbarTheme() {
        if (isFavorites) {
            toolbar.getContext().setTheme(R.style.AppTheme_ToolbarOrganizationSelected);
            toolbar.setPopupTheme(R.style.AppTheme_ToolbarOrganizationSelected);
        }
        else {
            toolbar.setPopupTheme(R.style.AppTheme_Toolbar);
            toolbar.getContext().setTheme(R.style.AppTheme_Toolbar);
        }
    }

    private void getOrganizationDetails() {

        loader.setVisibility(View.VISIBLE);
        reference.child(organizationID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    GetOrganizationsDataModel dataModel = dataSnapshot.getValue(GetOrganizationsDataModel.class);
                    if (dataModel!=null) {
                        organizationDescription = dataModel.getOrganizationDescription();
                        organizationLocation = dataModel.getOrganizationLocation();
                        organizationTitle = dataModel.getOrganizationTitle();
                        organizationType = dataModel.getOrganizationType();
                        organizationCategory = dataModel.getOrganizationCategory();
                        organizationLogo = dataModel.getOrganizationImageURL();
                        extraImages = dataModel.getOrganizationImages();
                    }
                    textViewTitle.setText(organizationTitle);
                    textViewDescription.setText(organizationDescription);
                    textViewLocation.setText(organizationLocation);
                    textViewType.setText(organizationCategory);
                    Glide.with(getApplicationContext())
                            .load(organizationLogo)
                            .into(imageView);
                    UserOrganizationImagesAdapter adapter = new UserOrganizationImagesAdapter(OrganizationDetailsActivity.this,extraImages);
                    gridView.setAdapter(adapter);
                    loader.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(OrganizationDetailsActivity.this, "Something Went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrganizationDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_organization_details_menu,menu);
        MenuItem item = menu.findItem(R.id.menu_organization_favorites);
        if (toolbar.getPopupTheme() == R.style.AppTheme_ToolbarOrganizationSelected) {
            item.setTitle("Added To Favorites");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_organization_favorites) {
            loader.setVisibility(View.VISIBLE);
            checkFavorites();
        }
        return true;
    }

    private void checkFavorites() {
        favoritesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    favoritesReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                favoritesReference.child(userID).child(organizationID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            openDialogforDeleteFavorite();
                                        }
                                        else {
                                            openDialogforAddFavorite();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(OrganizationDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                openDialogforAddFavorite();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(OrganizationDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    openDialogforAddFavorite();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrganizationDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialogforAddFavorite() {
        AlertDialog dialog = new AlertDialog.Builder(OrganizationDetailsActivity.this)
                .setTitle("Add To Favorites")
                .setMessage("Click Yes to add "+organizationTitle+" to your favorites section to easily access it again.")
                .setCancelable(false)
                .setIcon(R.drawable.logo)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loader.setVisibility(View.GONE);
                        dialog.cancel();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addOrganizationToFavorites();
                        dialog.cancel();
                    }
                }).create();
        dialog.show();
    }

    private void addOrganizationToFavorites() {
        UserFavoritesModel model = new UserFavoritesModel(organizationID,organizationType,organizationTitle,organizationLogo,organizationLocation,organizationDescription,organizationCategory);
        favoritesReference.child(userID).child(organizationID).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(OrganizationDetailsActivity.this, "Organization Added To Your Favorites", Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
                recreate();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loader.setVisibility(View.GONE);
                Toast.makeText(OrganizationDetailsActivity.this, "Failed To Add Organization. Please Try Again Later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialogforDeleteFavorite() {
        AlertDialog dialog = new AlertDialog.Builder(OrganizationDetailsActivity.this)
                .setTitle("Add To Favorites")
                .setMessage("Are you sure you want to delete the organization "+organizationTitle+" from your Favorites section.")
                .setCancelable(false)
                .setIcon(R.drawable.logo)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loader.setVisibility(View.GONE);
                        dialog.cancel();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteOrganizationFromFavorites();
                        dialog.cancel();
                    }
                }).create();
        dialog.show();
    }

    private void deleteOrganizationFromFavorites() {
        favoritesReference.child(userID).child(organizationID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(OrganizationDetailsActivity.this, "Organization Removed Successfully from Favorites Section.", Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
                recreate();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loader.setVisibility(View.GONE);
                Toast.makeText(OrganizationDetailsActivity.this, "Failed To Remove Organization From Favorites. Please Try Again Later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void DonateMoney(View view) {
        Intent intent = new Intent(OrganizationDetailsActivity.this,MakePaymentActivity.class);
        intent.putExtra("organizationID",organizationID);
        intent.putExtra("categoryID",organizationType);
        startActivity(intent);
    }
}
