package com.leadsoft.smart_campus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.leadsoft.smart_campus.R;

public class MainActivity extends AppCompatActivity {

    public static final int SPLASH_TIME_OUT = 2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable(){
           @Override
           public void run(){
               startActivity(new Intent(MainActivity.this,LoginActivity.class));
               finish();
           }
        },SPLASH_TIME_OUT);
    }
}