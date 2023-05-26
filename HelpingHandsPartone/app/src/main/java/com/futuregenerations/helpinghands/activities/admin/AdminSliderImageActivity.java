package com.futuregenerations.helpinghands.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.futuregenerations.helpinghands.models.AdminSliderGridAdapter;
import com.futuregenerations.helpinghands.models.GetSliderDataModel;
import com.futuregenerations.helpinghands.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class AdminSliderImageActivity extends AppCompatActivity {

    GridView gridView;
    TextView textView;
    DatabaseReference reference;
    List<GetSliderDataModel> sliderDataModels;
    MKLoader loader;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_slider_image);

        sliderDataModels = new ArrayList<>();

        loader = findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);

        textView = findViewById(R.id.text_no_image_slider_admin);
        gridView = findViewById(R.id.admin_grid_slider_layout);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        reference = FirebaseDatabase.getInstance().getReference("SliderImage");

        getSliderData();

        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                return findViewById(R.id.admin_grid_slider_layout).canScrollVertically(-1);
            }
        });

        onSwipeRefreshed();
    }

    private void onSwipeRefreshed() {
        if (swipeRefreshLayout.canChildScrollUp()) {
            swipeRefreshLayout.setEnabled(false);
        }
        else {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setEnabled(true);
                    getSliderData();
                }
            });
        }
    }

    private void getSliderData() {
        sliderDataModels.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GetSliderDataModel sliderDataModel = snapshot.getValue(GetSliderDataModel.class);
                        sliderDataModels.add(sliderDataModel);
                    }
                    if (sliderDataModels.size()!=0) {
                        AdminSliderGridAdapter adapter = new AdminSliderGridAdapter(AdminSliderImageActivity.this, sliderDataModels);
                        gridView.setAdapter(adapter);
                        loader.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String sliderId = sliderDataModels.get(position).getImageId();
                                Intent intent = new Intent(AdminSliderImageActivity.this,AdminEditSliderImageActivity.class);
                                intent.putExtra("imageId",sliderId);
                                startActivity(intent);
                            }
                        });
                    }
                    else {
                        loader.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        textView.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loader.setVisibility(View.GONE);
                Toast.makeText(AdminSliderImageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    public void addSlider(View view) {
        startActivity(new Intent(AdminSliderImageActivity.this,AdminAddSliderImageActivity.class));
    }
}
