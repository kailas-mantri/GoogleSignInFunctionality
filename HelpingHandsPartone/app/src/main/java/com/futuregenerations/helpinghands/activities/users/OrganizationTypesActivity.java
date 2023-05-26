package com.futuregenerations.helpinghands.activities.users;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class OrganizationTypesActivity extends AppCompatActivity {

    SliderView sliderView;
    ImageSliderAdapter adapter;

    DatabaseReference sliderImageReference, organizationTypeReference;
    List<GetSliderDataModel> listSliderImages;
    List<GetOrganizationCategoryModel> organizationCategoryModelList;

    GridView gridView;
    MKLoader loader;
    RelativeLayout layout;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_types);

        sliderView = findViewById(R.id.imageSlider);
        sliderImageReference = FirebaseDatabase.getInstance().getReference("SliderImage");
        organizationTypeReference = FirebaseDatabase.getInstance().getReference("OrganizationCategory");
        loader = findViewById(R.id.loader);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        layout = findViewById(R.id.organization_type_layout);
        layout.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        listSliderImages = new ArrayList<>();
        organizationCategoryModelList = new ArrayList<>();

        gridView = findViewById(R.id.grid_organizations_types);

        initializeSlider();

        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                child = gridView;
                return child.canScrollVertically(-1);
            }
        });

        onSwipeRefreshed();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryID = organizationCategoryModelList.get(position).getCategoryID();
                String categoryName = organizationCategoryModelList.get(position).getCategoryTitle();
                Intent intent = new Intent(OrganizationTypesActivity.this,OrganizationsActivity.class);
                intent.putExtra("categoryID",categoryID);
                intent.putExtra("categoryName",categoryName);
                startActivity(intent);
            }
        });
    }

    private void onSwipeRefreshed() {
        if (swipeRefreshLayout.canChildScrollUp()) {
            swipeRefreshLayout.setEnabled(false);
        }
        else {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(true);
                    addSliderItems();
                }
            });
        }
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
                    adapter = new ImageSliderAdapter(OrganizationTypesActivity.this,listSliderImages);
                    sliderView.setSliderAdapter(adapter);
                    getOrganizationsTypes();
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

    private void getOrganizationsTypes() {
        organizationCategoryModelList.clear();
        organizationTypeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GetOrganizationCategoryModel organizationCategoryModel = snapshot.getValue(GetOrganizationCategoryModel.class);
                        organizationCategoryModelList.add(organizationCategoryModel);
                    }
                    OrganizationsCategoryGridAdapter adapter = new OrganizationsCategoryGridAdapter(OrganizationTypesActivity.this,organizationCategoryModelList);
                    gridView.setAdapter(adapter);
                    loader.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }
                else {
                    Toast.makeText(OrganizationTypesActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loader.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(OrganizationTypesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }
}
