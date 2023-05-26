package com.futuregenerations.helpinghands.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.futuregenerations.helpinghands.activities.LoginActivity;
import com.futuregenerations.helpinghands.models.AdminHomeGridAdapter;
import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.models.AdminSharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity {

    Toolbar toolbar;

    GridView gridView;
    String[] title= {"Dashboard", "Organizations", "Slider Images", "Users", "Payments", "Feedback"};
    int[] images = {R.drawable.dashboard,R.drawable.organizations,R.drawable.slider,R.drawable.users,R.drawable.payments,R.drawable.feedback};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gridView = findViewById(R.id.admin_grid_home_layout);

        AdminHomeGridAdapter adapter = new AdminHomeGridAdapter(this,title,images);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickedItem(position);
            }
        });

        checkTheme();
    }

    private void checkTheme() {
        String themeMode = AdminSharedPrefManager.getInstance(this).getTheme();

        if (TextUtils.equals(themeMode,getResources().getString(R.string.theme_dark))) {
            toolbar.setPopupTheme(android.R.style.ThemeOverlay_Material_Dark);
        }
        else if (TextUtils.equals(themeMode,getResources().getString(R.string.theme_system))) {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES :
                    toolbar.setPopupTheme(android.R.style.ThemeOverlay_Material_Dark);
                    break;

                case Configuration.UI_MODE_NIGHT_NO :
                    toolbar.setPopupTheme(android.R.style.ThemeOverlay_Material_Light);
                    break;
            }
        }

        else {
            toolbar.setPopupTheme(android.R.style.ThemeOverlay_Material_Light);
        }
    }

    private void onClickedItem(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(AdminHomeActivity.this,AdminDashboardActivity.class));
                break;

            case 1:
                startActivity(new Intent(AdminHomeActivity.this,AdminOrganizationsCategoryActivity.class));
                break;

            case 2:
                startActivity(new Intent(AdminHomeActivity.this,AdminSliderImageActivity.class));
                break;

            case 3:
                startActivity(new Intent(AdminHomeActivity.this,AdminUsersActivity.class));
                break;

            case 4:
                startActivity(new Intent(AdminHomeActivity.this,AdminPaymentsActivity.class));
                break;

            case 5:
                startActivity(new Intent(AdminHomeActivity.this, AdminFeedbackUserActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.admin_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.menu_admin_about:
                startActivity(new Intent(AdminHomeActivity.this,AdminAboutActivity.class));
                break;

            case R.id.menu_admin_settings:
                startActivity(new Intent(AdminHomeActivity.this,AdminSettingsActivity.class));
                break;

            case R.id.menu_admin_logout:
                AdminSharedPrefManager.getInstance(this).clearTheme();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(new Intent(AdminHomeActivity.this,LoginActivity.class));
                finish();
                break;
        }

        return true;
    }
}
