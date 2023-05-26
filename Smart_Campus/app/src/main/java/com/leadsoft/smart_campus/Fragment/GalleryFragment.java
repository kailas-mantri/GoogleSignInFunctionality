package com.leadsoft.smart_campus.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.leadsoft.smart_campus.R;

public class GalleryFragment extends Fragment {

    GridView gridView;
    public int[] images = {
            R.drawable.campus_img1,R.drawable.campus_img2,R.drawable.campus_img3,
            R.drawable.campus_img4,R.drawable.campus_img5,R.drawable.campus_img6
    };

    public GalleryFragment(){ }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery,container,false);

        gridView = rootView.findViewById(R.id.grid_gallery);
        MainAdaptor adaptor = new MainAdaptor(getContext(), images);
        gridView.setAdapter(adaptor);
        return rootView;
    }
}

class MainAdaptor extends BaseAdapter{

    private final Context context;
    private final int[] images;

    public MainAdaptor(Context context, int[] images) {
        this.context = context;
        this.images = images;
    }


    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custon_gallery, parent, false);
        ImageView imageView = view.findViewById(R.id.image_galley_item);
        imageView.setImageResource(images[position]);
        return view;
    }
}