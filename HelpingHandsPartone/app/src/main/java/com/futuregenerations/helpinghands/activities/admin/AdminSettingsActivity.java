package com.futuregenerations.helpinghands.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.activities.LoginActivity;
import com.futuregenerations.helpinghands.models.AdminSettingsListAdapter;
import com.futuregenerations.helpinghands.models.AdminSharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;

public class AdminSettingsActivity extends AppCompatActivity {

    ListView listView;
    String[] settingsItemTitle = {"Theme","Share","About App","Sign Out"};
    int[] settingsItemDrawable = {R.drawable.ic_theme,R.drawable.ic_share,R.drawable.ic_about,R.drawable.ic_signout};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        listView = findViewById(R.id.admin_settings_list_view);
        AdminSettingsListAdapter adapter = new AdminSettingsListAdapter(this,settingsItemTitle,settingsItemDrawable);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickPerform(position);
            }
        });
    }

    private void onClickPerform(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(AdminSettingsActivity.this,AdminThemeActivity.class));
                break;

            case 1:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"https://github.com/Abhi26shah/HelpingHands");
                Intent chooserIntent = Intent.createChooser(shareIntent,"Share Using");
                startActivity(chooserIntent);
                break;

            case 2:
                startActivity(new Intent(AdminSettingsActivity.this,AdminAboutActivity.class));
                break;

            case 3:
                AdminSharedPrefManager.getInstance(getApplicationContext()).clearTheme();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(AdminSettingsActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
