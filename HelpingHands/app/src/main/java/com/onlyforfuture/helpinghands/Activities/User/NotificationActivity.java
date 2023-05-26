package com.onlyforfuture.helpinghands.Activities.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.os.Bundle;
import android.widget.TextView;

import com.onlyforfuture.helpinghands.R;

public class NotificationActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView textView_Notice;
    SwitchCompat do_not_disturb_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        textView_Notice = findViewById(R.id.user_notification_notice);
        do_not_disturb_switch = findViewById(R.id.switch_do_not_disturb);

        if (do_not_disturb_switch.isChecked()){
            do_not_disturb_switch.setChecked(true);

            int Notification_ACTION_ID = 0;

        }
        else {
            do_not_disturb_switch.setChecked(false);

            int Notification_ACTION_ID = 1;

        }
    }
}
