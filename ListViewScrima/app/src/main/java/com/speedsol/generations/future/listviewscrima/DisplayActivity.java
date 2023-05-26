package com.speedsol.generations.future.listviewscrima;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class DisplayActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        imageView = findViewById(R.id.imgview);
        Intent intent = getIntent();

        String i = intent.getStringExtra("fruit");

        if (i.equals("Apple")) { imageView.setImageResource(R.drawable.apple); }

        else if (i.equals("Guava")) { imageView.setImageResource(R.drawable.guava); }

        else if (i.equals("Coconut")) { imageView.setImageResource(R.drawable.coconut); }

        else if (i.equals("Banana")) { imageView.setImageResource(R.drawable.banana); }

        else if (i.equals("Mango")) { imageView.setImageResource(R.drawable.mango); }

        else { Toast.makeText(this, "Invalid Choice", Toast.LENGTH_SHORT).show(); }
    }
}
