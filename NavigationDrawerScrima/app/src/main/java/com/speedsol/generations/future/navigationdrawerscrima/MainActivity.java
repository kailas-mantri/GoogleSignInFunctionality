package com.speedsol.generations.future.navigationdrawerscrima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    TextView textView;
    Fragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        FloatingActionButton fab = findViewById(R.id.fab);
        navigationView.setCheckedItem(R.id.menu_home);

        textView = findViewById(R.id.txtText);
        textView.setText(navigationView.getCheckedItem().toString());

        fragment = new HomeFragment();
        loadFragment(fragment);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v,
                        "You have clicked the floating button.",Snackbar.LENGTH_LONG)
                        .setAction("View", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Hello World", Toast.LENGTH_SHORT).show();
                    }
                });
                snackbar.show();

            }
        });

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.app_name,R.string.app_name);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second,menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.menu_home) {
            Toast.makeText(this, "You Have Selected Home", Toast.LENGTH_SHORT).show();
            textView.setText("Home");
            fragment = new HomeFragment();
            loadFragment(fragment);
        }

        else if (id == R.id.menu_profile) {
            Toast.makeText(this, "You Have Selected Profile", Toast.LENGTH_SHORT).show();
            textView.setText("Profile");
            fragment = new ProfileFragment();
            loadFragment(fragment);
        }

        else if (id == R.id.menu_about) {
            Toast.makeText(this, "You Have Selected About", Toast.LENGTH_SHORT).show();
            textView.setText("About");
            fragment = new AboutFragment();
            loadFragment(fragment);
        }

        else if (id == R.id.menu_tools) {
            Toast.makeText(this, "You Have Selected Tools", Toast.LENGTH_SHORT).show();
            textView.setText("Tools");
            fragment = new ToolsFragment();
            loadFragment(fragment);
        }
        else if (id == R.id.menu_share) {
            Toast.makeText(this, "You Have Selected Share", Toast.LENGTH_SHORT).show();
            textView.setText("Share");
            fragment = null;
        }
        else if (id == R.id.menu_send) {
            Toast.makeText(this, "You Have Selected Send", Toast.LENGTH_SHORT).show();
            textView.setText("Send");
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
