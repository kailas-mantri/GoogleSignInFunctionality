package com.speedsol.generations.future.listviewscrima;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {

    ListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.list);
        final String arr[] = new String[]{"Apple","Guava","Coconut","Banana","Mango"};

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_expandable_list_item_1,arr);

        view.setAdapter(adapter);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "You Have Selected "+arr[position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,DisplayActivity.class);
                intent.putExtra("fruit",arr[position]);
                startActivity(intent);
            }
        });
    }
}