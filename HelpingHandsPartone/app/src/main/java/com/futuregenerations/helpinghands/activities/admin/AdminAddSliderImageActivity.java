package com.futuregenerations.helpinghands.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.futuregenerations.helpinghands.models.GetSliderDataModel;
import com.futuregenerations.helpinghands.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AdminAddSliderImageActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST_CODE = 22;

    Uri filePath;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    ImageView imageViewSlider;
    TextInputEditText editTextID, editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_slider_image);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        imageViewSlider = findViewById(R.id.admin_add_slider_image);
        editTextID = findViewById(R.id.admin_slider_image_id);
        editTextDescription = findViewById(R.id.admin_slider_image_description);

        imageViewSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image From Here..."),PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE
            && resultCode == RESULT_OK
            && data!=null
            && data.getData()!=null) {

            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageViewSlider.setImageBitmap(bitmap);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    public void addImageToSlider(View view) {
        String imageID = editTextID.getText().toString();
        String imageDescription = editTextDescription.getText().toString();
        if (imageViewSlider.getDrawable() == getResources().getDrawable(R.drawable.no_poster)) {
            Toast.makeText(this, "Please Select an Image for Slider", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(imageID)) {
            editTextID.setError("Please provide a valid ID");
            editTextID.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(imageDescription)) {
            editTextDescription.setError("Please provide image description");
            editTextDescription.requestFocus();
            return;
        }
        else {
            uploadSliderImageToStorage();
        }
    }

    private void uploadSliderImageToStorage() {
        if (filePath!=null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference imageRef = storageReference.child("SliderImages/"+ UUID.randomUUID().toString());

            imageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            uploadSliderDataToFirebase(imageUrl);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminAddSliderImageActivity.this, "Failed To Upload the Image", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }
    }

    private void uploadSliderDataToFirebase(String imageUrl) {
        String imageURL = imageUrl;
        String imageID = editTextID.getText().toString();
        String imageDescription = editTextDescription.getText().toString();
        GetSliderDataModel sliderDataModel = new GetSliderDataModel(imageDescription,imageURL,imageID);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("SliderImage");
        reference.child(imageID).setValue(sliderDataModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AdminAddSliderImageActivity.this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                clearPage();
            }
        });
    }

    private void clearPage() {
        editTextID.setText("");
        editTextDescription.setText("");
        imageViewSlider.setImageResource(R.drawable.no_poster);
    }
}
