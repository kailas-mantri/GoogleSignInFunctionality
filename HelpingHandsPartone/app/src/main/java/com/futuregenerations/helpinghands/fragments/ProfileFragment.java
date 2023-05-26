package com.futuregenerations.helpinghands.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.models.UserDataModel;
import com.futuregenerations.helpinghands.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tuyenmonkey.mkloader.MKLoader;

public class ProfileFragment extends Fragment {
    
    TextView textViewName, textViewEmail;
    TextInputEditText editTextMobile, editTextDate, editTextAddress, editTextCity, editTextState;
    Button buttonSaveChanges;
    FloatingActionButton fabEdit;
    RoundedImageView imageViewUserImage;
    RelativeLayout layoutUserProfileMain;
    MKLoader loader;
    
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference reference;
    
    String uID, userID, userName, userEmail, userMobile, userDate, userAddress, userCity, userState, userImage;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile,container,false);
        
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uID = user.getUid();
        
        reference = FirebaseDatabase.getInstance().getReference("Users");
        
        loader = rootView.findViewById(R.id.loader);
        layoutUserProfileMain = rootView.findViewById(R.id.user_profile_main_layout);
        
        textViewName = rootView.findViewById(R.id.user_profile_user_name);
        textViewEmail = rootView.findViewById(R.id.user_profile_user_email);
        
        editTextMobile = rootView.findViewById(R.id.user_profile_user_mobile);
        editTextDate = rootView.findViewById(R.id.user_profile_user_date);
        editTextAddress = rootView.findViewById(R.id.user_profile_user_address);
        editTextCity = rootView.findViewById(R.id.user_profile_user_city);
        editTextState = rootView.findViewById(R.id.user_profile_user_state);
        
        buttonSaveChanges = rootView.findViewById(R.id.user_profile_btn_save_details);
        fabEdit = rootView.findViewById(R.id.user_profile_fab_edit);
        
        imageViewUserImage = rootView.findViewById(R.id.user_profile_user_image);
        
        loader.setVisibility(View.VISIBLE);
        layoutUserProfileMain.setVisibility(View.GONE);
        
        getDataToVariables();
        
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllEnabled();
            }
        });

        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                layoutUserProfileMain.setEnabled(false);
                saveDataChanges();
            }
        });
        
        return rootView;
    }

    private void saveDataChanges() {
        userMobile = editTextMobile.getText().toString();
        userDate = editTextDate.getText().toString();
        userAddress = editTextAddress.getText().toString();
        userCity = editTextCity.getText().toString();
        userState = editTextState.getText().toString();

        UserDataModel model = new UserDataModel(userID,userName,userImage,userEmail,userMobile,userDate,userCity,userState,userAddress);
        reference.child(userID).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
                layoutUserProfileMain.setEnabled(true);
                setAllDisabled();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed To Update Profile. Please Try Again Later...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAllDisabled() {
        editTextMobile.setEnabled(false);
        editTextDate.setEnabled(false);
        editTextAddress.setEnabled(false);
        editTextCity.setEnabled(false);
        editTextState.setEnabled(false);
        fabEdit.setVisibility(View.VISIBLE);
        buttonSaveChanges.setVisibility(View.GONE);
    }

    private void setAllEnabled() {
        editTextMobile.setEnabled(true);
        editTextDate.setEnabled(true);
        editTextAddress.setEnabled(true);
        editTextCity.setEnabled(true);
        editTextState.setEnabled(true);
        fabEdit.setVisibility(View.INVISIBLE);
        buttonSaveChanges.setVisibility(View.VISIBLE);
    }

    private void getDataToVariables() {
        reference.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserDataModel dataModel = dataSnapshot.getValue(UserDataModel.class);
                    userID = dataModel.getUserId();
                    userName = dataModel.getUserName();
                    userEmail = dataModel.getUserEmail();
                    userMobile = dataModel.getUserMobile();
                    userDate = dataModel.getUserDate();
                    userAddress = dataModel.getUserAddress();
                    userCity = dataModel.getUserCity();
                    userState = dataModel.getUserState();
                    userImage = dataModel.getUserImage();
                    
                    getDataToView();
                }
                else {
                    Toast.makeText(getContext(), "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }
        });
    }

    private void getDataToView() {
        
        String oldResolution = "=s96-c";
        String originalResolution = "";
        String profileImage = userImage.replace(oldResolution,originalResolution);
        
        textViewName.setText(userName);
        textViewEmail.setText(userEmail);

        Glide.with(getContext())
                .load(profileImage)
                .into(imageViewUserImage);
        
        editTextMobile.setText(userMobile);
        editTextDate.setText(userDate);
        editTextAddress.setText(userAddress);
        editTextCity.setText(userCity);
        editTextState.setText(userState);
        
        loader.setVisibility(View.GONE);
        layoutUserProfileMain.setVisibility(View.VISIBLE);
    }
}
