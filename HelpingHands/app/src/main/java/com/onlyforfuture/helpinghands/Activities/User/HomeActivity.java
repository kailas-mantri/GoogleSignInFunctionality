package com.onlyforfuture.helpinghands.Activities.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onlyforfuture.helpinghands.Fragments.HistoryFragment;
import com.onlyforfuture.helpinghands.Fragments.HomeFragment;
import com.onlyforfuture.helpinghands.Fragments.NotificationFragment;
import com.onlyforfuture.helpinghands.Fragments.ProfileFragment;
import com.onlyforfuture.helpinghands.Fragments.SettingsFragment;
import com.onlyforfuture.helpinghands.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    BottomNavigationView navigationView;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationDrawer;
    FirebaseUser user;
    FirebaseAuth auth;

    Fragment fragment = null;

    private GoogleApiClient googleApiClient;

    boolean isSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        GoogleSignInOptions gso =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        getSupportActionBar().setTitle("Home");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationDrawer = findViewById(R.id.navigationView);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.menu_home);
        fragment = new HomeFragment();
        loadFragment(fragment);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.white));
        drawerToggle.syncState();

        navigationDrawer.setCheckedItem(R.id.menu_home);

        navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int menuId = menuItem.getItemId();

                switch (menuId)
                {
                    case R.id.menu_home :
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        getSupportActionBar().setTitle("Home");
                        navigationView.setSelectedItemId(menuId);
                        break;

                    case R.id.menu_profile :
                        fragment = new ProfileFragment();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        navigationView.setSelectedItemId(menuId);
                        loadFragment(fragment);
                        break;

                    case R.id.menu_notifications :
                        fragment = new NotificationFragment();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        navigationView.setSelectedItemId(menuId);
                        loadFragment(fragment);
                        break;

                    case R.id.menu_settings :
                        fragment = new SettingsFragment();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        navigationView.setSelectedItemId(menuId);
                        loadFragment(fragment);
                        break;

                    case R.id.menu_history :
                        fragment = new HistoryFragment();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        navigationView.setSelectedItemId(menuId);
                        loadFragment(fragment);
                        break;

                    case R.id.menu_organization :
                        startActivity(new Intent(HomeActivity.this, OrganizationsTypeActivity.class));
                        break;

                    case R.id.menu_fav :
                        startActivity(new Intent(HomeActivity.this,FavouritesActivity.class));
                        break;

                    case R.id.menu_feedback :
                        startActivity(new Intent(HomeActivity.this,FeedbackActivity.class));
                        break;

                    case R.id.menu_share_app :
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT,"https://github.com/KailasMantri/DonationAppication");
                        Intent chooserIntent = Intent.createChooser(shareIntent,"Share Using");
                        startActivity(chooserIntent);
                        break;

                    case R.id.menu_about :
                        startActivity(new Intent(HomeActivity.this,AboutActivity.class));
                        break;

                    case R.id.menu_Sign_out:
                        FirebaseAuth.getInstance().signOut();
                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        if (status.isSuccess()){
                                            startActivity(new Intent(HomeActivity.this,MainActivity.class));
                                            finish();
                                        }else{
                                            Toast.makeText(getApplicationContext(),"Session not close",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.menu_home :
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        navigationDrawer.setCheckedItem(menuItem.getItemId());
                        getSupportActionBar().setTitle("Home");
                        break;

                    case R.id.menu_profile :
                        fragment = new ProfileFragment();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        loadFragment(fragment);
                        navigationDrawer.setCheckedItem(menuItem.getItemId());
                        break;

                    case R.id.menu_notifications :
                        fragment = new NotificationFragment();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        loadFragment(fragment);
                        navigationDrawer.setCheckedItem(menuItem.getItemId());
                        break;

                    case R.id.menu_settings :
                        fragment = new SettingsFragment();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        loadFragment(fragment);
                        navigationDrawer.setCheckedItem(menuItem.getItemId());
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }
        else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()) {

            GoogleSignInAccount account=result.getSignInAccount();
            if (account != null) {
                View headerView = navigationDrawer.getHeaderView(0);
                TextView userName = headerView.findViewById(R.id.name);
                userName.setText(account.getDisplayName());
                TextView userEmail = headerView.findViewById(R.id.textemail);
                userEmail.setText(account.getEmail());
            }
            try {
                if (account != null) {
                    View headerView = navigationDrawer.getHeaderView(0);
                    CircleImageView profileImage = headerView.findViewById(R.id.image_view);
                    Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
                }
            } catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
            }

        } else{
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
