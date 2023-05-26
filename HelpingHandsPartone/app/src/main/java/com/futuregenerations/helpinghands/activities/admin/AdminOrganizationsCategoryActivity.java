package com.futuregenerations.helpinghands.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.futuregenerations.helpinghands.models.GetOrganizationCategoryModel;
import com.futuregenerations.helpinghands.models.GetSliderDataModel;
import com.futuregenerations.helpinghands.models.ImageSliderAdapter;
import com.futuregenerations.helpinghands.models.OrganizationsCategoryGridAdapter;
import com.futuregenerations.helpinghands.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class AdminOrganizationsCategoryActivity extends AppCompatActivity {

    GridView gridView;
    DatabaseReference reference;
    List<GetOrganizationCategoryModel> modelList;
    TextView textViewNoData;
    MKLoader loader;
    SwipeRefreshLayout swipeRefreshLayout;

    SliderView sliderView;
    ImageSliderAdapter adapter;
    List<GetSliderDataModel> listSliderImages;
    DatabaseReference sliderImageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_organizations_category);

        sliderView = findViewById(R.id.imageSlider);
        sliderImageReference = FirebaseDatabase.getInstance().getReference("SliderImage");
        listSliderImages = new ArrayList<>();

        modelList = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        gridView = findViewById(R.id.admin_grid_organizations_category_layout);
        textViewNoData = findViewById(R.id.organizations_category_no_data_available_text);
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
        textViewNoData.setVisibility(View.GONE);
        reference = FirebaseDatabase.getInstance().getReference("OrganizationCategory");
        saveDataToGrid();

        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                return findViewById(R.id.admin_grid_organizations_category_layout).canScrollVertically(-1);
            }
        });

        initializeSlider();

        onDataRefreshed();
    }

    private void initializeSlider() {
        sliderView.setIndicatorAnimation(IndicatorAnimations.SWAP);
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINDEPTHTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(2);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        addSliderItems();
    }

    private void addSliderItems() {
        listSliderImages.clear();
        sliderImageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GetSliderDataModel sliderDataModel = snapshot.getValue(GetSliderDataModel.class);
                        listSliderImages.add(sliderDataModel);
                    }
                    adapter = new ImageSliderAdapter(AdminOrganizationsCategoryActivity.this,listSliderImages);
                    sliderView.setSliderAdapter(adapter);
                }
                else {
                    sliderView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onDataRefreshed() {
        if (swipeRefreshLayout.canChildScrollUp()) {
            swipeRefreshLayout.setEnabled(false);
        }
        else {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(true);
                    saveDataToGrid();
                }
            });
        }
    }

    private void saveDataToGrid() {
        modelList.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GetOrganizationCategoryModel organizationCategoryModel = snapshot.getValue(GetOrganizationCategoryModel.class);
                        modelList.add(organizationCategoryModel);
                    }
                    OrganizationsCategoryGridAdapter adapter = new OrganizationsCategoryGridAdapter(AdminOrganizationsCategoryActivity.this,modelList);
                    gridView.setAdapter(adapter);
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    textViewNoData.setVisibility(View.GONE);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(AdminOrganizationsCategoryActivity.this,AdminOrganizationsActivity.class);
                            intent.putExtra("categoryID",modelList.get(position).getCategoryID());
                            startActivity(intent);
                        }
                    });
                }
                else {
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    textViewNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loader.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                textViewNoData.setVisibility(View.VISIBLE);
                Toast.makeText(AdminOrganizationsCategoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    public void addCategory(View view) {
        startActivity(new Intent(AdminOrganizationsCategoryActivity.this,AdminAddCategoryActivity.class));
    }
}
