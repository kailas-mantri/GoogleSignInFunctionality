package com.onlyforfuture.helpinghands.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.onlyforfuture.helpinghands.R;

public class CustomGridAdminHomeAdapter extends ArrayAdapter<String> {

    private Context context;
    private final String[] name;
    private int[] image;

    public CustomGridAdminHomeAdapter(@NonNull Context context, int[] image, String[] name) {
        super(context,R.layout.custom_admin_dashboard,name);
        this.context=context;
        this.image=image;
        this.name=name;
    }

    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_admin_dashboard,null,true);

        ImageView imagedashboard = view.findViewById(R.id.image_regular_orders);
        TextView textdashoboard =view.findViewById(R.id.text_regular_orders);
        imagedashboard.setImageResource(image[position]);
        textdashoboard.setText(name[position]);
        return view;
    }
}
