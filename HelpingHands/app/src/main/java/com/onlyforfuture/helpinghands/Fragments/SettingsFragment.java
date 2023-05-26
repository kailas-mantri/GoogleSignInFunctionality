package com.onlyforfuture.helpinghands.Fragments;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onlyforfuture.helpinghands.Activities.User.AboutActivity;
import com.onlyforfuture.helpinghands.Activities.User.FeedbackActivity;
import com.onlyforfuture.helpinghands.Activities.User.HomeActivity;
import com.onlyforfuture.helpinghands.Activities.User.MainActivity;
import com.onlyforfuture.helpinghands.Activities.User.NotificationActivity;
import com.onlyforfuture.helpinghands.Activities.User.ThemeActivity;
import com.onlyforfuture.helpinghands.Models.CustomListSettingAdapter;
import com.onlyforfuture.helpinghands.Models.StoreFirebaseUser;
import com.onlyforfuture.helpinghands.R;
import com.tuyenmonkey.mkloader.MKLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {

    MKLoader loader;
    ListView listViewSettings;
    CircleImageView imageProfile;
    TextView textViewName;
    RelativeLayout relativeLayout;

    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private GoogleApiClient googleApiClient;

    String title[] = {"Change Theme", "Notification Settings", "Share App", "Feedback", "About", "Sign Out"};
    int drawables[] = {R.drawable.ic_theme_change, R.drawable.ic_menu_notifications,
            R.drawable.ic_menu_share, R.drawable.ic_menu_feedback, R.drawable.ic_menu_about,R.drawable.ic_menu_sign_out};

    String uID="";

    public SettingsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){ super.onCreate(savedInstanceState); }

    @Override
    @Nullable
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_settings,container,false);

        listViewSettings = rootView.findViewById(R.id.listSettings);
        loader = rootView.findViewById(R.id.loader);
        relativeLayout = rootView.findViewById(R.id.layout_profile_image);
        loader.setVisibility(View.VISIBLE);

        imageProfile = rootView.findViewById(R.id.profile_image);
        textViewName = rootView.findViewById(R.id.profile_name);

        imageProfile.setVisibility(View.GONE);


        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        uID = firebaseUser.getUid();

        getUserData(rootView);

        CustomListSettingAdapter adapter = new CustomListSettingAdapter(getContext(),title,drawables);
        listViewSettings.setAdapter(adapter);

        HomeActivity homeActivity = (HomeActivity)getActivity();
        googleApiClient = homeActivity.getGoogleApiClient();

        listViewSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                performOnClick(position);
            }
        });
        return rootView;
    }

    private void performOnClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(getContext(), ThemeActivity.class));
                break;

            case 1:
                startActivity(new Intent(getContext(), NotificationActivity.class));
                break;

            case 2:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"https://github.com/KarunaJoshi1998/MoviezPoint");
                Intent chooserIntent = Intent.createChooser(shareIntent,"Share Using");
                startActivity(chooserIntent);
                break;

            case 3:
                startActivity(new Intent(getContext(), FeedbackActivity.class));
                break;

            case 4:
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;

            case 5:
                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (status.isSuccess()){
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                    getActivity().finish();
                                }else{
                                    Toast.makeText(getContext(),"Session not close",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                break;
        }
    }

    private void getUserData(final View v) {
        reference.child("Users").child(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StoreFirebaseUser getData = dataSnapshot.getValue(StoreFirebaseUser.class);
                Glide.with(v)
                        .load(getData.getUserImage())
                        .into(imageProfile);

                textViewName.setText(getData.getUserName());

                loader.setVisibility(View.GONE);
                imageProfile.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data... Please try again", Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }
        });
    }
}
