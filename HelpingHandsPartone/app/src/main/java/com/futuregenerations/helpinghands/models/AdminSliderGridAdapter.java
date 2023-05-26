package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.futuregenerations.helpinghands.activities.admin.AdminEditSliderImageActivity;
import com.futuregenerations.helpinghands.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdminSliderGridAdapter extends BaseAdapter {

    private Context context;
    private final List<GetSliderDataModel> getSliderDataModel;
    public AdminSliderGridAdapter(Context context, List<GetSliderDataModel> getSliderDataModel) {
        this.context = context;
        this.getSliderDataModel = getSliderDataModel;
    }

    @Override
    public int getCount() {
        return getSliderDataModel.size();
    }

    @Override
    public Object getItem(int position) {
        return getSliderDataModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_admin_grid_slider_layout,parent,false);
        TextView textViewTitle = convertView.findViewById(R.id.grid_item_text_slider_admin);
        ImageView imageViewSlider = convertView.findViewById(R.id.grid_item_image_slider_admin);
        SwipeLayout swipeLayout = convertView.findViewById(R.id.swipeLayout);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left,swipeLayout.findViewById(R.id.linearlayoutswipeleft));
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right,swipeLayout.findViewById(R.id.linearlayoutswiperight));

        textViewTitle.setText(getSliderDataModel.get(position).getDescription());
        Glide.with(convertView)
                .load(getSliderDataModel.get(position).getImageURL())
                .into(imageViewSlider);

        swipeLayout.findViewById(R.id.delete_slider_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sliderID = getSliderDataModel.get(position).getImageId();
                deleteSlider(sliderID,position);
                Toast.makeText(context, "You Clicked Delete "+getSliderDataModel.get(position).getImageId(), Toast.LENGTH_SHORT).show();
            }
        });

        swipeLayout.findViewById(R.id.update_slider_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminEditSliderImageActivity.class);
                String imageId = getSliderDataModel.get(position).getImageId();
                intent.putExtra("imageId",imageId);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private void deleteSlider(String sliderID, final int position) {
        DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference().child("SliderImage");
        deleteRef.child(sliderID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                StorageReference imageDeleteRef = FirebaseStorage.getInstance().getReferenceFromUrl(getSliderDataModel.get(position).getImageURL());
                imageDeleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getSliderDataModel.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Slider Item Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
