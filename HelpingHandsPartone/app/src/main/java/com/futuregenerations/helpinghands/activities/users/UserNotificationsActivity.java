package com.futuregenerations.helpinghands.activities.users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.models.UserSharedPrefData;
import com.futuregenerations.helpinghands.models.UserSharedPrefManager;

public class UserNotificationsActivity extends AppCompatActivity {

    Switch aSwitch;
    String themeName, notificationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notifications);

        aSwitch = findViewById(R.id.notification_switch);
        themeName = UserSharedPrefManager.getInstance(this).getData().getThemeName();
        notificationStatus = UserSharedPrefManager.getInstance(this).getData().getNotificationStatus();

        if (TextUtils.equals(notificationStatus,getResources().getString(R.string.allow_notification))) {
            aSwitch.setChecked(false);
        }
        else if (TextUtils.equals(notificationStatus,getResources().getString(R.string.block_app_notifications))) {
            aSwitch.setChecked(true);
        }
        else {
            aSwitch.setChecked(false);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notificationStatus = getResources().getString(R.string.block_app_notifications);
                    UserSharedPrefData data = new UserSharedPrefData(themeName,notificationStatus);
                    UserSharedPrefManager.getInstance(UserNotificationsActivity.this).userData(data);
                }
                else {
                    notificationStatus = getResources().getString(R.string.allow_notification);
                    UserSharedPrefData data = new UserSharedPrefData(themeName,notificationStatus);
                    UserSharedPrefManager.getInstance(UserNotificationsActivity.this).userData(data);
                }
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
