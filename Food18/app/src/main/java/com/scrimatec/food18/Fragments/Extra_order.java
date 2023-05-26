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

public class Extra_order extends Fragment {

    GridView gridView;
    int[] images = {R.drawable.fruit_salad,R.drawable.irish_coffee,R.drawable.soft_drink,R.drawable.faluda_shooter,R.drawable.ginger_and_iced_honey_tea,R.drawable.pizza,R.drawable.burger_extra};
    String[] titles = {"Fruit Salad","Irish Coffee","Soft Drink","Faluda","Ginger Tea","Pizza","Burger"};

    public Extra_order() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_extra_orders,container,false);

        gridView = rootView.findViewById(R.id.grid_extra_order);

        CustomGridExtraOrderAdapter adapter = new CustomGridExtraOrderAdapter(getContext(),images,titles);
        gridView.setAdapter(adapter);
        return rootView;
    }
}

class CustomGridExtraOrderAdapter extends ArrayAdapter<String> {

    private Context context;
    private final int[] images;
    private final String[] titles;

    public CustomGridExtraOrderAdapter(Context context, int[] images, String[] titles) {
        super(context, R.layout.custom_extra_orders,titles);
        this.context = context;
        this.images = images;
        this.titles = titles;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_extra_orders,null,true);

        ImageView imageVegies = view.findViewById(R.id.image_extra_orders);
        TextView textVegies = view.findViewById(R.id.title_extra_orders);
        final ImageView imageView = view.findViewById(R.id.image_checked);
        imageVegies.setImageResource(images[position]);
        textVegies.setText(titles[position]);

        final MaterialRadioButton radioSelection = view.findViewById(R.id.radio_selection);

        imageVegies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioSelection.isChecked())
                {
                    radioSelection.setChecked(false);
                    imageView.setVisibility(View.VISIBLE);
                    radioSelection.setButtonDrawable(context.getResources().getDrawable(R.drawable.radio));
                }
                else
                {
                    radioSelection.setChecked(true);
                    imageView.setVisibility(View.GONE);
                    radioSelection.setButtonDrawable(context.getResources().getDrawable(R.drawable.radio));
                }
            }
        });
        return view;
    }
}