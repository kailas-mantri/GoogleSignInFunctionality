package com.futuregenerations.helpinghands.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.futuregenerations.helpinghands.models.GetOrganizationCategoryModel;
import com.futuregenerations.helpinghands.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.UUID;

public class AdminAddCategoryActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_admin_add_category);

        imageViewCategory = findViewById(R.id.admin_organization_category_image);
        editTextID = findViewById(R.id.admin_add_organization_category_id);
        editTextTitle = findViewById(R.id.admin_add_organization_category_title);
        editTextDescription = findViewById(R.id.admin_add_organization_category_description);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        reference = FirebaseDatabase.getInstance().getReference("OrganizationCategory");
        loader = findViewById(R.id.loader);
        layout = findViewById(R.id.relative_layout_add_category_organization_admin);

        imageViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromStorage();
            }
        });
    }

    private void selectImageFromStorage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Images From Here..."),IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_REQUEST_CODE
            && resultCode == RESULT_OK
            && data!=null
            && data.getData()!=null) {

            fileUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),fileUri);
                imageViewCategory.setImageBitmap(bitmap);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {
            Toast.makeText(this, "Please Select a valid Category Logo", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    public void saveOrganizationCategory(View view) {
        categoryID = editTextID.getText().toString();
        categoryTitle = editTextTitle.getText().toString();
        categoryDescription = editTextDescription.getText().toString();
        if (TextUtils.isEmpty(categoryID)) {
            editTextID.setError("Please Enter a valid ID");
            editTextID.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(categoryTitle)) {
            editTextTitle.setError("Please Enter a valid Category Title");
            editTextTitle.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(categoryDescription)) {
            editTextDescription.setError("Please Enter The Category Description");
            editTextDescription.requestFocus();
            return;
        }
        else if (imageViewCategory.getDrawable()==getResources().getDrawable(R.drawable.no_poster)) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("No Image Selected")
                    .setMessage("Please Select an Image for Organization Category and then try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();
            alertDialog.show();
            return;
        }
        else {
            storeImageToFirebaseStorage();
        }
    }

    private void storeImageToFirebaseStorage() {
        if (fileUri!=null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference imageRef = storageReference.child("OrganizationCategoryImages/"+ UUID.randomUUID().toString());

            imageRef.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            categoryImageFileURL = uri.toString();
                            imageRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                @Override
                                public void onSuccess(StorageMetadata storageMetadata) {
                                    categoryImageFileName = storageMetadata.getName();
                                    StoreDataToFirebaseDatabase();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminAddCategoryActivity.this, "Falied To Upload Image", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = 100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded "+(int)progress);
                }
            });
        }
    }

    private void StoreDataToFirebaseDatabase() {
        layout.setClickable(false);
        layout.setEnabled(false);
        loader.setVisibility(View.VISIBLE);
        GetOrganizationCategoryModel getOrganizationCategoryModel = new GetOrganizationCategoryModel(categoryID,categoryTitle,categoryDescription,categoryImageFileURL,categoryImageFileName);
        reference.child(categoryID).setValue(getOrganizationCategoryModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loader.setVisibility(View.GONE);
                layout.setEnabled(true);
                layout.setClickable(true);
                clearLayout();
                Toast.makeText(AdminAddCategoryActivity.this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
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
