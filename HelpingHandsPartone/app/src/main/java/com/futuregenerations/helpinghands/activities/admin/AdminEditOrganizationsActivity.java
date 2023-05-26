package com.futuregenerations.helpinghands.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.models.GetOrganizationsDataModel;
import com.futuregenerations.helpinghands.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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

public class AdminEditOrganizationsActivity extends AppCompatActivity {

    TextInputEditText editTextName, editTextType, editTextID, editTextLocation, editTextDescription;
    ImageView imageViewLogo, imageViewAddOne, imageViewAddtwo, imageViewAddThree, imageViewAddFour;
    String organizationName, organizationType, organizationID, organizationLocation, organizationDescription,logoImageURL,orgID,orgType,organizationCategory;
    List<String> imagesURL;
    Toolbar toolbar;
    MKLoader loader;
    RelativeLayout layout;

    FirebaseStorage storage;
    StorageReference storageReference;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_organizations);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imagesURL = new ArrayList<>();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        reference = FirebaseDatabase.getInstance().getReference("Organizations");

        loader = findViewById(R.id.loader);
        layout = findViewById(R.id.admin_edit_organization_layout);
        editTextName = findViewById(R.id.admin_edit_organization_title);
        editTextType = findViewById(R.id.admin_edit_organization_type);
        editTextID = findViewById(R.id.admin_edit_organization_id);
        editTextLocation = findViewById(R.id.admin_edit_organization_address);
        editTextDescription = findViewById(R.id.admin_edit_organization_description);

        imageViewLogo = findViewById(R.id.admin_image_view_edit_organization);
        imageViewAddOne = findViewById(R.id.admin_extra_image_one);
        imageViewAddtwo = findViewById(R.id.admin_extra_image_two);
        imageViewAddThree = findViewById(R.id.admin_extra_image_three);
        imageViewAddFour = findViewById(R.id.admin_extra_image_four);

        getIntentData();
        loader.setVisibility(View.VISIBLE);
        layout.setEnabled(false);

    }

    private void getIntentData() {
        Intent editIntent = getIntent();
        orgID = editIntent.getStringExtra("orgID");
        orgType = editIntent.getStringExtra("orgType");
        getDataFromDatabase();
    }

    private void getDataFromDatabase() {
        reference.child(orgType).child(orgID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    GetOrganizationsDataModel dataModel = dataSnapshot.getValue(GetOrganizationsDataModel.class);
                    editTextName.setText(dataModel.getOrganizationTitle());
                    editTextType.setText(dataModel.getOrganizationType());
                    editTextID.setText(dataModel.getOrganizationID());
                    editTextLocation.setText(dataModel.getOrganizationLocation());
                    editTextDescription.setText(dataModel.getOrganizationDescription());
                    organizationCategory = dataModel.getOrganizationCategory();
                    imagesURL = dataModel.getOrganizationImages();
                    Glide.with(AdminEditOrganizationsActivity.this)
                            .load(dataModel.getOrganizationImageURL())
                            .into(imageViewLogo);
                    loadAllImages(imagesURL);
                    loader.setVisibility(View.GONE);
                    layout.setEnabled(true);
                }
                else {
                    loader.setVisibility(View.GONE);
                    layout.setEnabled(true);
                    Toast.makeText(AdminEditOrganizationsActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loader.setVisibility(View.GONE);
                layout.setEnabled(true);
                Toast.makeText(AdminEditOrganizationsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllImages(List<String> imageURL) {
        int size = imageURL.size();
        int i;
        for (i=0;i<size;i++) {
            String imageUrl = imageURL.get(i);
            loadIntoImageView(imageUrl,i);
        }
    }

    private void loadIntoImageView(String imageURL, int i) {
        if (i==0) {
            Glide.with(AdminEditOrganizationsActivity.this)
                    .load(imageURL)
                    .into(imageViewAddOne);
            imageViewAddtwo.setVisibility(View.VISIBLE);
            return;
        }
        else if (i==1) {
            Glide.with(AdminEditOrganizationsActivity.this)
                    .load(imageURL)
                    .into(imageViewAddtwo);
            imageViewAddThree.setVisibility(View.VISIBLE);
            return;
        }
        else if (i==2) {
            Glide.with(AdminEditOrganizationsActivity.this)
                    .load(imageURL)
                    .into(imageViewAddThree);
            imageViewAddFour.setVisibility(View.VISIBLE);
            return;
        }
        else if (i==3) {
            Glide.with(AdminEditOrganizationsActivity.this)
                    .load(imageURL)
                    .into(imageViewAddFour);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.admin_menu_edit_organization,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.admin_menu_edit_organization_save_data:
                checkData();
                break;
            default:
                break;
        }
        return true;
    }

    private void checkData() {
        organizationName = editTextName.getText().toString();
        organizationType = editTextType.getText().toString();
        organizationID = editTextID.getText().toString();
        organizationLocation = editTextLocation.getText().toString();
        organizationDescription = editTextDescription.getText().toString();

        if (imageViewLogo.getDrawable()==getResources().getDrawable(R.drawable.no_poster)) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("No Logo Selected")
                    .setMessage("Please Select an image for Organization's Logo.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();

            dialog.show();
            return;
        }

        else if (TextUtils.isEmpty(organizationName)) {
            editTextName.setError("Enter Organization Name");
            editTextName.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(organizationType)) {
            editTextType.setError("Type not Available");
            editTextType.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(organizationID)) {
            editTextID.setError("Enter Organization ID");
            editTextID.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(organizationLocation)) {
            editTextLocation.setError("Enter Organization Location");
            editTextLocation.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(organizationDescription)) {
            editTextDescription.setError("Enter Organization Description");
            editTextDescription.requestFocus();
            return;
        }

        else {
            loader.setVisibility(View.VISIBLE);
            layout.setEnabled(false);
            saveDataToFirebaseDatabase();
        }
    }

    private void saveDataToFirebaseDatabase() {
        int urls = imagesURL.size();
        if (urls == 0) {
            GetOrganizationsDataModel dataModel = new GetOrganizationsDataModel(organizationID,organizationType,organizationName,logoImageURL,organizationLocation,organizationDescription,organizationCategory,null);
            reference.child(organizationType).child(organizationID).setValue(dataModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AdminEditOrganizationsActivity.this, "Data Stored Successfully", Toast.LENGTH_SHORT).show();
                    layout.setEnabled(true);
                    loader.setVisibility(View.GONE);
                    clearLayout();
                }
            });
        }
        else {
            GetOrganizationsDataModel dataModel = new GetOrganizationsDataModel(organizationID,organizationType,organizationName,logoImageURL,organizationLocation,organizationDescription,organizationCategory,imagesURL);
            reference.child(organizationType).child(organizationID).setValue(dataModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AdminEditOrganizationsActivity.this, "Data Stored Successfully", Toast.LENGTH_SHORT).show();
                    layout.setEnabled(true);
                    loader.setVisibility(View.GONE);
                    clearLayout();
                }
            });
        }
    }

    private void clearLayout() {
        editTextName.setText("");
        editTextID.setText("");
        editTextLocation.setText("");
        editTextDescription.setText("");
        imageViewLogo.setImageResource(R.drawable.no_poster);
        imageViewAddOne.setImageResource(R.drawable.ic_add_image);
        imageViewAddtwo.setImageResource(R.drawable.ic_add_image);
        imageViewAddThree.setImageResource(R.drawable.ic_add_image);
        imageViewAddFour.setImageResource(R.drawable.ic_add_image);
        imageViewAddtwo.setVisibility(View.INVISIBLE);
        imageViewAddThree.setVisibility(View.INVISIBLE);
        imageViewAddFour.setVisibility(View.INVISIBLE);
        editTextName.clearFocus();
        editTextID.clearFocus();
        editTextLocation.clearFocus();
        editTextDescription.clearFocus();
    }
}
