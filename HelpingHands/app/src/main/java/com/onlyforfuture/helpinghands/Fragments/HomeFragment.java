package com.onlyforfuture.helpinghands.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onlyforfuture.helpinghands.Models.StoreFirebaseUser;
import com.onlyforfuture.helpinghands.R;
import com.tuyenmonkey.mkloader.MKLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    TextView textUserName, textUserEmail;
    Button donation_button;

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    MKLoader loader;
    String userID;

    public HomeFragment() { }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);

        textUserName = rootView.findViewById(R.id.textViewName);
        textUserEmail = rootView.findViewById(R.id.textViewEmail);

        donation_button = rootView.findViewById(R.id.donation_button);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        loader = rootView.findViewById(R.id.loader);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        userID = firebaseUser.getUid();
        loader.setVisibility(View.VISIBLE);
        getUserDataToView();
        return rootView;
    }

    private void getUserDataToView() {
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    StoreFirebaseUser user = dataSnapshot.getValue(StoreFirebaseUser.class);
                    textUserName.setText(user.getUserName());
                    textUserEmail.setText(user.getUserEmail());
                }
                Toast.makeText(getContext(), "Data Retrieval is Successful", Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }
        });
    }
}