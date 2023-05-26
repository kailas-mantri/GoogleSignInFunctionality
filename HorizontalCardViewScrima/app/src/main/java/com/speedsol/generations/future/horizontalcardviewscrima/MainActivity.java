package com.speedsol.generations.future.horizontalcardviewscrima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    ArrayList<String> numbers;
    ArrayList<Integer> listImages;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RecyclerViewAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout;
    View ChildView;
    int RecyclerViewItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        AddItemsToRecyclerViewArrayList();
        RecyclerViewHorizontalAdapter = new RecyclerViewAdapter(numbers,listImages);
        HorizontalLayout = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(RecyclerViewHorizontalAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector detector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                ChildView = rv.findChildViewUnder(e.getX(),e.getY());
                if ((ChildView!=null) && detector.onTouchEvent(e))
                {
                    RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                    Toast.makeText(MainActivity.this, numbers.get(RecyclerViewItemPosition), Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private void AddItemsToRecyclerViewArrayList() {
        numbers = new ArrayList<>();
        numbers.add("ONE");
        numbers.add("TWO");
        numbers.add("THREE");
        numbers.add("FOUR");
        numbers.add("FIVE");
        numbers.add("SIX");
        numbers.add("SEVEN");
        numbers.add("EIGHT");
        numbers.add("NINE");
        numbers.add("TEN");

        listImages = new ArrayList<>();
        listImages.add(R.drawable.one);
        listImages.add(R.drawable.two);
        listImages.add(R.drawable.three);
        listImages.add(R.drawable.four);
        listImages.add(R.drawable.five);
        listImages.add(R.drawable.six);
        listImages.add(R.drawable.seven);
        listImages.add(R.drawable.eight);
        listImages.add(R.drawable.nine);
        listImages.add(R.drawable.ten);
    }
}
