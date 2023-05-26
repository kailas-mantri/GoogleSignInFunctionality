package com.onlyforfuture.helpinghands.Activities.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onlyforfuture.helpinghands.Models.GetOrganizationCategoryDataModel;
import com.onlyforfuture.helpinghands.Models.GetSliderImageDataModel;
import com.onlyforfuture.helpinghands.Models.OrganizationCategoryGridAdapter;
import com.onlyforfuture.helpinghands.Models.SliderIMageDataAdapter;
import com.onlyforfuture.helpinghands.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class OrganizationsTypeActivity extends AppCompatActivity {

    GridView gridView;
    String CategoryId;
    Toolbar toolbar;

    DatabaseReference reference;
    List<GetOrganizationCategoryDataModel> dataModelList;

    SliderView sliderView;
    List<GetSliderImageDataModel> imageDataModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisations_type);

        sliderView = findViewById(R.id.slider_image_view);
        imageDataModelList = new ArrayList<>();

        // Slider Image indicator,Transform Animation,Auto cycle direction, Indicator Selected & UnSelected Color,
        // Time in Seconds, Autocycle "Set & Start"

        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(2);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        addnewItem();

        dataModelList = new ArrayList<>();
        gridView = findViewById(R.id.grid_organisation);

        reference = FirebaseDatabase.getInstance().getReference("OrganizationCategory");

        // back press to home page without taking the backimagefile
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        getFirebaseDataToAdapter();
    }

    private void addnewItem() {
        try {
                reference = FirebaseDatabase.getInstance().getReference("SliderImage");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                GetSliderImageDataModel imagedatamodel = dataSnapshot1.getValue(GetSliderImageDataModel.class);
                                imageDataModelList.add(imagedatamodel);
                            }
                            SliderIMageDataAdapter adapter = new SliderIMageDataAdapter(OrganizationsTypeActivity.this,imageDataModelList);
                            sliderView.setSliderAdapter(adapter);
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    CategoryId = dataModelList.get(position).getCategoryID();
                                    Intent intent =new Intent(OrganizationsTypeActivity.this, OrganizationsListActivity.class);
                                    intent.putExtra("CategoryId",CategoryId);
                                    startActivity(intent);
                                }
                            });
                        }
                        else {
                            Toast.makeText(OrganizationsTypeActivity.this, "Data Not Available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(OrganizationsTypeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        }
        catch (Exception e){ e.printStackTrace(); }
    }

    private void getFirebaseDataToAdapter() {
        dataModelList.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GetOrganizationCategoryDataModel dataModel = snapshot.getValue(GetOrganizationCategoryDataModel.class);
                        dataModelList.add(dataModel);
                    }
                    OrganizationCategoryGridAdapter adapter = new OrganizationCategoryGridAdapter(OrganizationsTypeActivity.this,dataModelList);
                    gridView.setAdapter(adapter);
                }
                else {
                    Toast.makeText(OrganizationsTypeActivity.this, "No Category Available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrganizationsTypeActivity.this, "Category Not Available", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
