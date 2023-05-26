package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.futuregenerations.helpinghands.R;

public class AdminHomeGridAdapter extends ArrayAdapter<String> {

    private Context context;
    private final String[] title;
    private final int[] images;

    public AdminHomeGridAdapter(Context context, String[] title, int[] images) {
        super(context, R.layout.custom_admin_home_grid_layout,title);
        this.context = context;
        this.title = title;
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_admin_home_grid_layout,null,true);
        TextView textTitle = convertView.findViewById(R.id.grid_item_title_home_admin);
        ImageView imageLogo = convertView.findViewById(R.id.grid_item_image_home_admin);
        textTitle.setText(title[position]);
        imageLogo.setImageResource(images[position]);

        return convertView;
    }
}
