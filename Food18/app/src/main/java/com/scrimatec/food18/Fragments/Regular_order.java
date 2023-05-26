package com.scrimatec.food18.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.scrimatec.food18.R;

public class Regular_order extends Fragment {

    GridView gridView;
    int[] image={R.drawable.paneer_butter_curry,R.drawable.veg_kadai_curry,R.drawable.missi_roti_dish,R.drawable.salad_dish
                    ,R.drawable.daal_dish,R.drawable.rice_dish};

    String[] name={"Paneer","Veg-kadai","Roti","Salad","Daal","Rice"};

    public Regular_order() {    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_regular_order,container,false);
        gridView=rootview.findViewById(R.id.grid_regular_order);

        CustomGridRegularOrderAdaptor adaptor = new CustomGridRegularOrderAdaptor(getContext(),image,name);
        gridView.setAdapter(adaptor);
        return rootview;
    }
}

class CustomGridRegularOrderAdaptor extends ArrayAdapter<String> {

    private Context context;
    private final String[] name;
    private int[] image;

    public CustomGridRegularOrderAdaptor(@NonNull Context context, int[] image, String[] name) {
        super(context,R.layout.custom_regular_order,name);
        this.context=context;
        this.image=image;
        this.name=name;
        }

    public View getView(int position,View view, ViewGroup parent){

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_regular_order,null,true);

        ImageView imageVegies = view.findViewById(R.id.image_regular_orders);
        TextView textVegies =(TextView) view.findViewById(R.id.text_regular_orders);
        final ImageView imageView = view.findViewById(R.id.image_checked);
        imageVegies.setImageResource(image[position]);
        textVegies.setText(name[position]);
        final MaterialRadioButton radioSelection = view.findViewById(R.id.radio_selection);
        imageVegies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioSelection.isChecked()) {
                    radioSelection.setChecked(false);
                    imageView.setVisibility(View.GONE);
                    radioSelection.setButtonDrawable(context.getResources().getDrawable(R.drawable.radio));
                }
                else {
                    radioSelection.setChecked(true);
                    imageView.setVisibility(View.VISIBLE);
                    radioSelection.setButtonDrawable(context.getResources().getDrawable(R.drawable.radio));
                }
            }
        });
        return view;
    }
}