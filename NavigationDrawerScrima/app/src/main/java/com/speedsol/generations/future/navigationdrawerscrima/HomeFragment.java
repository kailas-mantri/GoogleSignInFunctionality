package com.speedsol.generations.future.navigationdrawerscrima;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    String[] singers_name = {"Arijit Singh", "Armaan Malik", "Ayushmann Khurana", "Kumar Sanu", "Sonu Nigam", "Udit Narayan"};
    int[] singers_images = {R.drawable.arijit,R.drawable.armaan,R.drawable.ayushmann,R.drawable.kumar,R.drawable.sonu,R.drawable.udit};

    ListView listView;

    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);

        listView = rootView.findViewById(R.id.listView);

        SingersListAdapter adapter = new SingersListAdapter(getActivity(),singers_name,singers_images);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "You Have Selected "+singers_name[position], Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "You Have Long Clicked "+singers_name[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return rootView;
    }
}

class SingersListAdapter extends ArrayAdapter<String> {

    private Context context;
    private final String[] singers_name;
    private final int[] singers_images;

    public SingersListAdapter(Context context, String[] singers_name, int[] singers_images) {

        super(context, R.layout.home_list_layout, singers_name);

        this.context = context;
        this.singers_name = singers_name;
        this.singers_images = singers_images;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.home_list_layout,null,true);

        CircleImageView imgUser = view.findViewById(R.id.imgSinger);
        TextView txtname = view.findViewById(R.id.nameSinger);

        imgUser.setImageResource(singers_images[position]);
        txtname.setText(singers_name[position]);

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Clicked the Image of "+singers_name[position], Toast.LENGTH_SHORT).show();
                CustomDialogClass aClass = new CustomDialogClass(getContext(),singers_images[position]);
                aClass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                aClass.getWindow().setGravity(Gravity.CENTER);
                aClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                aClass.show();
            }
        });

        return view;
    }
}