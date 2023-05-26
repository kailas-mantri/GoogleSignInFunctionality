package com.scrimatec.scrimasearchview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    SearchView view;
    ListView listView;

    String[] search={"Roy","Smith","Jhon","Ronaldo","Whisley","Tango","Oliever"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view=findViewById(R.id.search);
        listView=findViewById(R.id.viewlist);

        ArrayAdapter<String> search1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                android.R.id.text1,search);

        listView.setAdapter(search1);
        search.setOnQueryTextListener(this);


    }
}
