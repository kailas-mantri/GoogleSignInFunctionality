package com.futuregenerations.helpinghands.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.futuregenerations.helpinghands.R;

public class AdminFeedbackActivity extends AppCompatActivity {

    TextView textViewFeedback, textViewDate, textViewTime;
    String msg, date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_feedback);

        Intent intent = getIntent();
        msg = intent.getStringExtra("msg");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");

        textViewFeedback = findViewById(R.id.admin_feedback_text_message);
        textViewDate = findViewById(R.id.admin_feedback_text_date);
        textViewTime = findViewById(R.id.admin_feedback_text_time);

        textViewFeedback.setText(msg);
        textViewDate.setText(date);
        textViewTime.setText(time);
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
