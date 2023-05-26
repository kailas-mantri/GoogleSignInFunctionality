package com.speedsol.generations.future.expandablelistscrima;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ExpandableListView listView;
    ExpandableListAdapter adapter;
    List<String> parent;
    HashMap<String, List<String>> child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.expandedList);
        child = ExpandableListData.getData();
        parent = new ArrayList<>(child.keySet());
//        initData();
        adapter = new CustomExpandableListAdapter(this,parent,child);
        listView.setAdapter(adapter);
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(MainActivity.this, parent.get(groupPosition)+" List Expanded", Toast.LENGTH_SHORT).show();
                listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                    @Override
                    public void onGroupCollapse(int groupPosition) {
                        Toast.makeText(MainActivity.this,parent.get(groupPosition)+" List Collapsed", Toast.LENGTH_SHORT).show();
                    }
                });
                listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView list, View v, int groupPosition, int childPosition, long id) {
                        Toast.makeText(MainActivity.this, parent.get(groupPosition)+" -> "+child.get(parent.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }
        });
    }
}
