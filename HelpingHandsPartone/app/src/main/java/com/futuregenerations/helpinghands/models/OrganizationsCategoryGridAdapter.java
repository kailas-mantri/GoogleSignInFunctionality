package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.R;

import java.util.List;

public class OrganizationsCategoryGridAdapter extends BaseAdapter {

    private Context context;
    private final List<GetOrganizationCategoryModel> categoryModelList;

    public OrganizationsCategoryGridAdapter(Context context,List<GetOrganizationCategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    @Override
    public int getCount() {
        return categoryModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryModelList.get(position).getCategoryTitle();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_organizations_category_grid_layout,parent,false);
        TextView textViewTitle = convertView.findViewById(R.id.display_organization_category_title);
        ImageView imageView = convertView.findViewById(R.id.display_organization_category_image);

        textViewTitle.setText(categoryModelList.get(position).getCategoryTitle());

        Glide.with(context)
                .load(categoryModelList.get(position).getCategoryImageURL())
                .into(imageView);

        return convertView;
    }
}
