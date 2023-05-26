package com.futuregenerations.helpinghands.activities.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.activities.LoginActivity;
import com.futuregenerations.helpinghands.fragments.HomeFragment;
import com.futuregenerations.helpinghands.fragments.NotificationsFragment;
import com.futuregenerations.helpinghands.fragments.ProfileFragment;
import com.futuregenerations.helpinghands.fragments.SettingsFragment;
import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.models.UserSharedPrefManager;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    Fragment fragment = null;

    int getId;

    private GoogleApiClient googleApiClient;
    FirebaseUser user;
    FirebaseAuth auth;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        String userTheme = UserSharedPrefManager.getInstance(this).getData().getThemeName();
        setUserTheme(userTheme);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        setSupportActionBar(toolbar);
        navigationView.setCheckedItem(R.id.menu_home);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        fragment = new HomeFragment();
        loadFragment(fragment);
        getId = navigationView.getCheckedItem().getItemId();
        getSupportActionBar().setTitle(navigationView.getCheckedItem().getTitle());

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.white));
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        onNavigationViewItemIsClicked();
        onBottomNavigationClicked();
    }

    private void onBottomNavigationClicked() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                String title = item.getTitle().toString();

                switch(itemId) {

                    case R.id.menu_home :
                        if (getId!=itemId) {
                            getId = itemId;
                            navigationView.setCheckedItem(itemId);
                            getSupportActionBar().setTitle(title);
                            fragment = new HomeFragment();
                            loadFragment(fragment);
                        }
                        break;

                    case R.id.menu_profile :
                        if (getId!=itemId) {
                            getId = itemId;
                            navigationView.setCheckedItem(itemId);
                            getSupportActionBar().setTitle(title);
                            fragment = new ProfileFragment();
                            loadFragment(fragment);
                        }
                        break;

                    case R.id.menu_notifications :
                        if (getId!=itemId) {
                            getId = itemId;
                            navigationView.setCheckedItem(itemId);
                            getSupportActionBar().setTitle(title);
                            fragment = new NotificationsFragment();
                            loadFragment(fragment);
                        }
                        break;

                    case R.id.menu_settings :
                        if (getId!=itemId) {
                            getId = itemId;
                            navigationView.setCheckedItem(itemId);
                            getSupportActionBar().setTitle(title);
                            fragment = new SettingsFragment();
                            loadFragment(fragment);
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void onNavigationViewItemIsClicked() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                String title = item.getTitle().toString();

                switch (itemId) {

                    case R.id.menu_home :
                        if (getId!=itemId) {
                            getId = itemId;
                            bottomNavigationView.setSelectedItemId(itemId);
                            getSupportActionBar().setTitle(title);
                            fragment = new HomeFragment();
                            loadFragment(fragment);
                        }
                        break;

                    case R.id.menu_profile :
                        if (getId!=itemId) {
                            getId = itemId;
                            bottomNavigationView.setSelectedItemId(itemId);
                            getSupportActionBar().setTitle(title);
                            fragment = new ProfileFragment();
                            loadFragment(fragment);
                        }
                        break;

                    case  R.id.menu_notifications :
                        if (getId!=itemId) {
                            getId = itemId;
                            bottomNavigationView.setSelectedItemId(itemId);
                            getSupportActionBar().setTitle(title);
                            fragment = new NotificationsFragment();
                            loadFragment(fragment);
                        }
                        break;

                    case R.id.menu_settings :
                        if (getId!=itemId) {
                            getId = itemId;
                            bottomNavigationView.setSelectedItemId(itemId);
                            getSupportActionBar().setTitle(title);
                            fragment = new SettingsFragment();
                            loadFragment(fragment);
                        }
                        break;

                    case R.id.menu_history :
                        startActivity(new Intent(HomeActivity.this,HistoryActivity.class));
                        break;

                    case R.id.menu_organizations :
                        startActivity(new Intent(HomeActivity.this, OrganizationTypesActivity.class));
                        break;

                    case R.id.menu_favorites :
                        startActivity(new Intent(HomeActivity.this,FavouritesActivity.class));
                        break;

                    case R.id.menu_feedback :
                        startActivity(new Intent(HomeActivity.this,FeedbackActivity.class));
                        break;

                    case R.id.menu_share :
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT,"https://github.com/Abhi26shah/HelpingHands");
                        Intent chooserIntent = Intent.createChooser(shareIntent,"Share Using");
                        startActivity(chooserIntent);
                        break;

                    case R.id.menu_about :
                        startActivity(new Intent(HomeActivity.this,AboutActivity.class));
                        break;

                    case R.id.menu_signOut :
                        FirebaseAuth.getInstance().signOut();
                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        if (status.isSuccess()){
                                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
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
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
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
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            if (account != null) {
                View headerView = navigationView.getHeaderView(0);
                TextView userName = headerView.findViewById(R.id.name_navigation);
                userName.setText(account.getDisplayName());
                TextView userEmail = headerView.findViewById(R.id.email_navigation);
                userEmail.setText(account.getEmail());
            }
            try{
                if (account != null) {
                    View headerView = navigationView.getHeaderView(0);
                    CircleImageView profileImage = headerView.findViewById(R.id.image_navigation);
                    Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
                }
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
            }

        }else{
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (bottomNavigationView.getSelectedItemId()!=R.id.menu_home) {
            bottomNavigationView.setSelectedItemId(R.id.menu_home);
            navigationView.setCheckedItem(R.id.menu_home);
            fragment = new HomeFragment();
            loadFragment(fragment);
            getSupportActionBar().setTitle(navigationView.getCheckedItem().getTitle());
        }
        else {
            super.onBackPressed();
        }
    }

    private void setUserTheme(String userThemeMode) {
        if (TextUtils.equals(userThemeMode,getResources().getString(R.string.theme_dark))) {
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.black));
        }
        else if (TextUtils.equals(userThemeMode,getResources().getString(R.string.theme_system))) {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES :
                    bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.black));
                    break;

                case Configuration.UI_MODE_NIGHT_NO :
                    bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.white));
                    break;
            }
        }

        else {
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }
}
