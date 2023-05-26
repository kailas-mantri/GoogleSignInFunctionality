package com.scrimatec.food18.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.scrimatec.food18.Fragments.ContactFragment;
import com.scrimatec.food18.Fragments.CouponsAndVouchersFragment;
import com.scrimatec.food18.Fragments.HomeFragment;
import com.scrimatec.food18.Fragments.MyOrdersFragment;
import com.scrimatec.food18.Fragments.NotificationsFragment;
import com.scrimatec.food18.Fragments.ProfileFragment;
import com.scrimatec.food18.Fragments.SettingFragment;
import com.scrimatec.food18.Fragments.SupportFragment;
import com.scrimatec.food18.R;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawer;
    public NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    ImageView imageView;
    TextView textViewPage;
    Fragment fragment = null;
    public static int itemId = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        textViewPage = findViewById(R.id.text_page_name);
        imageView = findViewById(R.id.edit_image);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        Intent intent = getIntent();
        int profileFlag = intent.getIntExtra("Profile",0);

            if (profileFlag == 1){
                fragment=new ProfileFragment();
                loadFragment(fragment);
                bottomNavigationView.setSelectedItemId(R.id.menu_profile);
                navigationView.setCheckedItem(R.id.menu_profile);
                textViewPage.setText("Profile");
                imageView.setVisibility(View.VISIBLE);
            }
            else {
                fragment = new HomeFragment();
                loadFragment(fragment);
                textViewPage.setText("Home");
                itemId = R.id.menu_home;
                bottomNavigationView.setSelectedItemId(R.id.menu_home);
                navigationView.setCheckedItem(R.id.menu_home);
                imageView.setVisibility(View.GONE);
            }

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                switch (id) {

                    case R.id.menu_home :
                        if (itemId != id) {
                            itemId = id;
                            navigationView.setCheckedItem(id);
                            bottomNavigationView.setSelectedItemId(id);
                            fragment = new HomeFragment();
                            loadFragment(fragment);
                            textViewPage.setText("Home");
                            imageView.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.menu_profile :
                        if (itemId != id) {
                            itemId = id;
                            navigationView.setCheckedItem(id);
                            bottomNavigationView.setSelectedItemId(id);
                            imageView.setVisibility(View.VISIBLE);
                            fragment = new ProfileFragment();
                            textViewPage.setText("Profile");
                            loadFragment(fragment);
                        }
                        break;

                    case R.id.menu_notifications :
                        if (itemId != id) {
                            imageView.setVisibility(View.GONE);
                            itemId = id;
                            navigationView.setCheckedItem(id);
                            bottomNavigationView.setSelectedItemId(id);
                            fragment = new NotificationsFragment();
                            textViewPage.setText("Notifications");
                            loadFragment(fragment);
                        }
                        break;

                    case R.id.menu_settings :
                        if (itemId != id) {
                            itemId = id;
                            bottomNavigationView.setSelectedItemId(id);
                            fragment = new SettingFragment();
                            navigationView.setCheckedItem(id);
                            textViewPage.setText("Settings");
                            imageView.setVisibility(View.GONE);
                            loadFragment(fragment);
                        }
                        break;

                    case R.id.menu_my_order :
                        if (itemId!=id) {
                            itemId = id;
                            navigationView.setCheckedItem(id);
                            fragment = new MyOrdersFragment();
                            loadFragment(fragment);
                            textViewPage.setText("My Orders");
                            imageView.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.menu_contact :
                        if (itemId!=id) {
                            itemId = id;
                            navigationView.setCheckedItem(id);
                            fragment = new ContactFragment();
                            loadFragment(fragment);
                            textViewPage.setText("Contact");
                            imageView.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.menu_coupons :
                        if (itemId!=id) {
                            itemId = id;
                            navigationView.setCheckedItem(id);
                            fragment = new CouponsAndVouchersFragment();
                            loadFragment(fragment);
                            textViewPage.setText("Coupons And Vouchers");
                            imageView.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.menu_support :
                        if (itemId!=id) {
                            itemId = id;
                            navigationView.setCheckedItem(id);
                            fragment = new SupportFragment();
                            loadFragment(fragment);
                            textViewPage.setText("Support");
                            imageView.setVisibility(View.GONE);
                        }
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.menu_home :
                        if (itemId != menuItem.getItemId()) {
                            itemId = menuItem.getItemId();
                            fragment = new HomeFragment();
                            navigationView.setCheckedItem(menuItem.getItemId());
                            loadFragment(fragment);
                            textViewPage.setText("Home");
                            imageView.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.menu_profile :
                        if (itemId != menuItem.getItemId()) {
                            itemId = menuItem.getItemId();
                            navigationView.setCheckedItem(menuItem.getItemId());
                            imageView.setVisibility(View.VISIBLE);
                            fragment = new ProfileFragment();
                            textViewPage.setText("Profile");
                            loadFragment(fragment);
                        }
                        break;

                    case R.id.menu_notifications :
                        if (itemId != menuItem.getItemId()) {
                            imageView.setVisibility(View.GONE);
                            itemId = menuItem.getItemId();
                            fragment = new NotificationsFragment();
                            navigationView.setCheckedItem(menuItem.getItemId());
                            textViewPage.setText("Notifications");
                            loadFragment(fragment);
                        }
                        break;

                    case R.id.menu_settings :
                        if (itemId != menuItem.getItemId()) {
                            itemId = menuItem.getItemId();
                            navigationView.setCheckedItem(menuItem.getItemId());
                            fragment = new SettingFragment();
                            textViewPage.setText("Settings");
                            imageView.setVisibility(View.GONE);
                            loadFragment(fragment);
                        }
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
}