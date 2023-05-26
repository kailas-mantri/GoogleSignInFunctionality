package com.onlyforfuture.helpinghands.Activities.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.onlyforfuture.helpinghands.Models.CustomGridAdminHomeAdapter;
import com.onlyforfuture.helpinghands.R;

public class AdminHomeActivity extends AppCompatActivity {

    GridView gridView;
    Toolbar  toolbar;

    int[] image={R.drawable.dashboard_logo_admin,R.drawable.orgaisation_logo_admin,R.drawable.slider_image_logo_admin,
            R.drawable.user_logo_admin,R.drawable.payment_logo_admin,R.drawable.feedback_logo_admin};


    String[] name={"Dashboard","Organisation","Slider Images", "User","Payment","Feedback"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        toolbar = findViewById(R.id.toolbar);

        gridView = findViewById(R.id.admin_dashboard_panel);

        CustomGridAdminHomeAdapter adapter = new CustomGridAdminHomeAdapter(getBaseContext(),image,name);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                performOnClick(position);
            }
        });
    }

    private void performOnClick(int position) {
        switch (position){
            case 0:
                startActivity(new Intent(AdminHomeActivity.this,AdminDashboardActivity.class));
                break;

            case 1:
                startActivity(new Intent(AdminHomeActivity.this, AdminOrganizationActivity.class));
                break;

            case 2:
                startActivity(new Intent(AdminHomeActivity.this,AdminSliderImageActivity.class));
                break;

            case 3:
                startActivity(new Intent(AdminHomeActivity.this,AdminUserActivity.class));
                break;

            case 4:
                startActivity(new Intent(AdminHomeActivity.this,AdminPaymentActivity.class));
                break;

            case 5:
                startActivity(new Intent(AdminHomeActivity.this,AdminFeedbackActivity.class));
                break;
        }
    }

}
