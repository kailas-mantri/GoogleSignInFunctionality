package com.speedsol.generations.future.fragmentscrima;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button1,button2;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button)findViewById(R.id.btn1);
        button2 = (Button)findViewById(R.id.btn2);
    }

    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btn1:
                Toast.makeText(this, "This is the trial method", Toast.LENGTH_SHORT).show();
                fragment = new Fragment1();
                loadFragment(fragment);
                break;

            case R.id.btn2:
                fragment = new Fragment2();
                loadFragment(fragment);
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag1,fragment);
        fragmentTransaction.commit();
    }
}