package com.futuregenerations.helpinghands.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.futuregenerations.helpinghands.models.GetOrganizationCategoryModel;
import com.futuregenerations.helpinghands.models.GetOrganizationsDataModel;
import com.futuregenerations.helpinghands.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminAddOrganizationsActivity extends AppCompatActivity {

    TextInputEditText editTextName, editTextType, editTextID, editTextLocation, editTextDescription;
    ImageView imageViewLogo, imageViewAddOne, imageViewAddtwo, imageViewAddThree, imageViewAddFour, imageViewForList;
    String organizationName, organizationType, organizationID, organizationLocation, organizationDescription,logoImageURL,orgImageURL, organizationCategory;
    Uri fileLogoPathUri,fileImageUri;
    List<String> imagesURL;
    List<Uri> uriList;
    Toolbar toolbar;
    MKLoader loader;
    RelativeLayout layout;
    public static final int IMAGE_REQUEST_CODE = 22;
    int totalImages;

    FirebaseStorage storage;
    StorageReference storageReference;

    DatabaseReference reference, categoryReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_organizations);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        uriList = new ArrayList<>();
        imagesURL = new ArrayList<>();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        reference = FirebaseDatabase.getInstance().getReference("Organizations");
        categoryReference = FirebaseDatabase.getInstance().getReference("OrganizationCategory");
        getCategory();

        loader = findViewById(R.id.loader);
        layout = findViewById(R.id.admin_add_organization_layout);
        editTextName = findViewById(R.id.admin_add_organization_title);
        editTextType = findViewById(R.id.admin_add_organization_type);
        editTextID = findViewById(R.id.admin_add_organization_id);
        editTextLocation = findViewById(R.id.admin_add_organization_address);
        editTextDescription = findViewById(R.id.admin_add_organization_description);

        imageViewLogo = findViewById(R.id.admin_image_view_add_organization);
        imageViewAddOne = findViewById(R.id.admin_extra_image_one);
        imageViewAddtwo = findViewById(R.id.admin_extra_image_two);
        imageViewAddThree = findViewById(R.id.admin_extra_image_three);
        imageViewAddFour = findViewById(R.id.admin_extra_image_four);

        Intent intent = getIntent();
        organizationType = intent.getStringExtra("categoryID");
        editTextType.setText(organizationType);

        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImagefromDeviceToView(R.id.admin_image_view_add_organization);
            }
        });

        imageViewAddOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImagefromDeviceToView(R.id.admin_extra_image_one);
            }
        });

        imageViewAddtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageViewAddtwo.getVisibility()==View.VISIBLE) {
                    getImagefromDeviceToView(R.id.admin_extra_image_two);
                }
            }
        });

        imageViewAddThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageViewAddThree.getVisibility()==View.VISIBLE) {
                    getImagefromDeviceToView(R.id.admin_extra_image_three);
                }
            }
        });

        imageViewAddFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageViewAddFour.getVisibility()==View.VISIBLE) {
                    getImagefromDeviceToView(R.id.admin_extra_image_four);
                }
            }
        });
    }

    private void getCategory() {
        categoryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    categoryReference.child(organizationType).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            GetOrganizationCategoryModel model = dataSnapshot.getValue(GetOrganizationCategoryModel.class);
                            organizationCategory = model.getCategoryTitle();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getImagefromDeviceToView(int imageViewID) {
        imageViewForList = findViewById(imageViewID);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE
            && resultCode == RESULT_OK
            && data!=null
            && data.getData()!=null) {

            Uri fileUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),fileUri);
                imageViewForList.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            setImageDrawable(imageViewForList.getId(),fileUri);
        }
    }

    private void setImageDrawable(int id,Uri fileUri) {
        switch (id) {

            case R.id.admin_image_view_add_organization:
                fileLogoPathUri = fileUri;
                break;
            case R.id.admin_extra_image_one:
                if (uriList.size()!=0) {
                    uriList.remove(0);
                }
                uriList.add(0, fileUri);
                imageViewAddtwo.setVisibility(View.VISIBLE);
                imageViewAddtwo.setImageResource(R.drawable.ic_add_image);
                break;

            case R.id.admin_extra_image_two:
                if (uriList.size()>1) {
                    uriList.remove(1);
                }
                uriList.add(1,fileUri);
                imageViewAddThree.setVisibility(View.VISIBLE);
                imageViewAddThree.setImageResource(R.drawable.ic_add_image);
                break;

            case R.id.admin_extra_image_three:
                if (uriList.size()>2) {
                    uriList.remove(3);
                }
                uriList.add(2,fileUri);
                imageViewAddFour.setVisibility(View.VISIBLE);
                imageViewAddFour.setImageResource(R.drawable.ic_add_image);
                break;

            case R.id.admin_extra_image_four:
                if (uriList.size()>3) {
                    uriList.remove(4);
                }
                uriList.add(3,fileUri);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.admin_add_organization_toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.admin_menu_add_organizations_clear:
                clearLayout();
                break;

            case R.id.admin_menu_add_organizations_save:
                checkData();
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
            storeAllImagesToFirebaseStorage();
        }
    }

    private void storeAllImagesToFirebaseStorage() {
        if (fileLogoPathUri!=null) {
            final StorageReference addImageRef = storageReference.child("OrganizationImages/"+ UUID.randomUUID().toString());
            addImageRef.putFile(fileLogoPathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    addImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            logoImageURL = uri.toString();
                            addOtherImages();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminAddOrganizationsActivity.this, "Failed to upload images... Please try again Later", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void addOtherImages() {
        totalImages = uriList.size();
        int i;
        if (totalImages!=0) {
            for (i=0;i<totalImages;i++) {
                fileImageUri = uriList.get(i);
                final StorageReference addImagesStorageRef = storageReference.child("OrganizationImages/"+UUID.randomUUID().toString());

                addImagesStorageRef.putFile(fileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        addImagesStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                orgImageURL = uri.toString();
                                imagesURL.add(orgImageURL);
                                if (uriList.size()==imagesURL.size()) {
                                    saveDataToFirebaseDatabase();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminAddOrganizationsActivity.this, "Failed To add Images", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else {
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
                    Toast.makeText(AdminAddOrganizationsActivity.this, "Data Stored Successfully", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AdminAddOrganizationsActivity.this, "Data Stored Successfully", Toast.LENGTH_SHORT).show();
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
