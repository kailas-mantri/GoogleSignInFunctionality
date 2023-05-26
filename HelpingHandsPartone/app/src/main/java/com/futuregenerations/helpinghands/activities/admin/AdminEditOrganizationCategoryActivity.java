package com.futuregenerations.helpinghands.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.models.GetOrganizationCategoryModel;
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
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.IOException;
import java.util.UUID;

public class AdminEditOrganizationCategoryActivity extends AppCompatActivity {

    ImageView imageViewCategory;
    TextInputEditText editTextID, editTextTitle,editTextDescription;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    DatabaseReference reference;
    Uri fileUri;
    String categoryID, categoryTitle,categoryDescription,categoryImageFileURL,categoryImageFileName;
    public static final int IMAGE_REQUEST_CODE = 22;

    MKLoader loader;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_organization_category);

        Intent intent = getIntent();
        categoryID = intent.getStringExtra("categoryID");
        imageViewCategory = findViewById(R.id.admin_organization_edit_category_image);
        editTextID = findViewById(R.id.admin_edit_organization_category_id);
        editTextTitle = findViewById(R.id.admin_edit_organization_category_title);
        editTextDescription = findViewById(R.id.admin_edit_organization_category_description);
        editTextID.setText(categoryID);
        editTextID.setEnabled(false);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        reference = FirebaseDatabase.getInstance().getReference("OrganizationCategory");
        loader = findViewById(R.id.loader);
        layout = findViewById(R.id.relative_layout_edit_category_organization_admin);

        getDataToView();

        imageViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromDevice();
            }
        });
    }

    private void selectImageFromDevice() {
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
            &&data.getData()!=null) {
            fileUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),fileUri);
                imageViewCategory.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getDataToView() {
        reference.child(categoryID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    GetOrganizationCategoryModel categoryModel = dataSnapshot.getValue(GetOrganizationCategoryModel.class);
                    editTextTitle.setText(categoryModel.getCategoryTitle());
                    editTextDescription.setText(categoryModel.getCategoryDescription());
                    Glide.with(AdminEditOrganizationCategoryActivity.this)
                            .load(categoryModel.getCategoryImageURL())
                            .into(imageViewCategory);
                    categoryImageFileName = categoryModel.getCategoryImageName();
                    categoryImageFileURL = categoryModel.getCategoryImageURL();
                }
                else {
                    Toast.makeText(AdminEditOrganizationCategoryActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminEditOrganizationCategoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    public void saveChanges(View view) {
        loader.setVisibility(View.VISIBLE);
        layout.setClickable(false);
        layout.setEnabled(false);
        checkImage();
    }

    private void checkImage() {
        if (fileUri!=null) {
            updateDataWithImage();
        }
        else {
            updateDataToFirebaseDatabase();
        }
    }

    private void updateDataWithImage() {
        StorageReference imageDeleteRef = FirebaseStorage.getInstance().getReferenceFromUrl(categoryImageFileURL);
        imageDeleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                addUpdatedImage();
            }
        });
    }

    private void addUpdatedImage() {
        final StorageReference updateRef = storageReference.child("OrganizationCategoryImages/"+UUID.randomUUID().toString());
        updateRef.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                updateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        categoryImageFileURL = uri.toString();
                        updateRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {
                                categoryImageFileName = storageMetadata.getName();
                                updateDataToFirebaseDatabase();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminEditOrganizationCategoryActivity.this, "Failed to update image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDataToFirebaseDatabase() {
        categoryTitle = editTextTitle.getText().toString();
        categoryDescription = editTextDescription.getText().toString();
        GetOrganizationCategoryModel model = new GetOrganizationCategoryModel(categoryID,categoryTitle,categoryDescription,categoryImageFileURL,categoryImageFileName);
        reference.child(categoryID).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AdminEditOrganizationCategoryActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
                layout.setEnabled(true);
                layout.setClickable(true);
                clearLayout();
            }
        });
    }

    private void clearLayout() {
        editTextID.setText("");
        editTextTitle.setText("");
        editTextDescription.setText("");
        imageViewCategory.setImageResource(R.drawable.no_poster);
    }
}
