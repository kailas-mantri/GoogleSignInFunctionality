package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.R;

import java.util.List;

public class UserOrganizationImagesAdapter extends BaseAdapter {

    Context context;
    List<String> extraImages;

    public UserOrganizationImagesAdapter(Context context, List<String> extraImages) {
        this.context = context;
        this.extraImages = extraImages;
    }

    @Override
    public int getCount() {
        return extraImages.size();
    }

    @Override
    public Object getItem(int position) {
        return extraImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_user_organization_images_grid_layout,parent,false);
        ImageView imageView = convertView.findViewById(R.id.organization_extra_image);
        Glide.with(context)
                .load(extraImages.get(position))
                .into(imageView);
        return convertView;
    }
}
