package com.leadsoft.smart_campus.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.leadsoft.smart_campus.Fragment.Applied_Company_Fragment;
import com.leadsoft.smart_campus.Fragment.CompanyFragment;
import com.leadsoft.smart_campus.Fragment.GalleryFragment;
import com.leadsoft.smart_campus.Fragment.HomeFragment;
import com.leadsoft.smart_campus.Fragment.ProfileFragment;
import com.leadsoft.smart_campus.R;
import com.leadsoft.smart_campus.Fragment.UpcomingFragment;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    NavigationView navigationView;
    Fragment fragment= null;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findview();

        ActionBarDrawerToggle DrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.app_name,R.string.app_name);
        drawer.addDrawerListener(DrawerToggle);
        DrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void findview(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.menu_home);
        fragment = new HomeFragment();
        loadFragment(fragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.menu_home) {
            fragment = new HomeFragment();
            toolbar.setTitle("Home");
            loadFragment(fragment);
        }

        else if(id == R.id.menu_gallery) {
            fragment = new GalleryFragment();
            toolbar.setTitle("Gallery");
            loadFragment(fragment);
        }
        else if(id == R.id.menu_profile) {
            fragment = new ProfileFragment();
            toolbar.setTitle("Profile");
            loadFragment(fragment);
        }
        else if (id == R.id.menu_company) {
            fragment = new CompanyFragment();
            toolbar.setTitle("Company");
            loadFragment(fragment);
        }
        else if(id == R.id.menu_applied_company) {
            fragment = new Applied_Company_Fragment();
            toolbar.setTitle("Applied Company");
            loadFragment(fragment);
        }
        else if(id == R.id.menu_upcoming_Events) {
            fragment = new UpcomingFragment();
            toolbar.setTitle("Events");
            loadFragment(fragment);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}