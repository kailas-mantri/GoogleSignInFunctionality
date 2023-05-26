package com.onlyforfuture.helpinghands.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.onlyforfuture.helpinghands.R;

import java.util.List;

public class OrganizationCategoryGridAdapter extends BaseAdapter {

    private Context context;
    private List<GetOrganizationCategoryDataModel> dataModelList;

    public OrganizationCategoryGridAdapter(Context context, List<GetOrganizationCategoryDataModel> dataModelList) {
        this.context = context;
        this.dataModelList = dataModelList;
    }

    @Override
    public int getCount() {
        return dataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelList.get(position).getCategoryTitle();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_organizations_grid_category,parent,false);
        TextView textView = convertView.findViewById(R.id.display_organization_category_title);
        ImageView imageView = convertView.findViewById(R.id.display_organization_category_image);
        Glide.with(context)
                .load(dataModelList.get(position).getCategoryImageURL())
                .into(imageView);
        textView.setText(dataModelList.get(position).getCategoryTitle());
        return convertView;
    }
}
