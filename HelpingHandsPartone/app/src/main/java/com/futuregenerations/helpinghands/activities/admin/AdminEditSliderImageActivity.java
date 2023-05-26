package com.futuregenerations.helpinghands.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.models.GetSliderDataModel;
import com.futuregenerations.helpinghands.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

public class AdminEditSliderImageActivity extends AppCompatActivity {

    ImageView imageViewSlider;
    TextInputEditText editTextID, editTextDescription;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    String imageURL, imageID, imageDescription;
    String sliderId;
    RelativeLayout layout;
    MKLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_slider_image);

        imageViewSlider = findViewById(R.id.admin_view_slider_image);
        editTextID = findViewById(R.id.admin_edit_slider_image_id);
        editTextDescription = findViewById(R.id.admin_edit_slider_image_description);
        layout = findViewById(R.id.slider_edit_layout);
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("SliderImage");
        Intent intent = getIntent();
        sliderId = intent.getStringExtra("imageId");
        getDataToView(sliderId);
    }

    private void getDataToView(String sliderId) {
        reference.child(sliderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GetSliderDataModel dataModel = dataSnapshot.getValue(GetSliderDataModel.class);
                imageID = dataModel.getImageId();
                imageURL = dataModel.getImageURL();
                imageDescription = dataModel.getDescription();
                Glide.with(AdminEditSliderImageActivity.this)
                        .load(imageURL)
                        .into(imageViewSlider);

                editTextID.setText(imageID);
                editTextDescription.setText(imageDescription);
                layout.setClickable(true);
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminEditSliderImageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    public void updateChanges(View view) {
        String sliderImageDescription = editTextDescription.getText().toString();
        if (sliderImageDescription.equals(imageDescription)) {
            Toast.makeText(this, "No changes made for update", Toast.LENGTH_SHORT).show();
        }
        else {
            loader.setVisibility(View.VISIBLE);
            layout.setClickable(false);
            GetSliderDataModel sliderDataModel = new GetSliderDataModel(sliderImageDescription,imageURL,imageID);
            reference.child(imageID).setValue(sliderDataModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AdminEditSliderImageActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                    loader.setVisibility(View.GONE);
                    onBackPressed();
                    finish();
                }
            });
        }
    }
}
