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

public class UserOrganizationListAdapter extends BaseAdapter {

    private Context context;
    private final List<GetOrganizationsDataModel> dataModelList;

    public UserOrganizationListAdapter(Context context, List<GetOrganizationsDataModel> dataModelList) {
        this.context = context;
        this.dataModelList = dataModelList;
    }

    @Override
    public int getCount() {
        return dataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelList.get(position).getOrganizationTitle();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_user_organization_list,parent,false);
        TextView textViewTitle = convertView.findViewById(R.id.title_organizations);
        final TextView textViewType = convertView.findViewById(R.id.type_organizations);
        TextView textViewDescription = convertView.findViewById(R.id.description_organization);
        ImageView imageView = convertView.findViewById(R.id.image_organizations);
        textViewType.setText(dataModelList.get(position).getOrganizationCategory());

        textViewTitle.setText(dataModelList.get(position).getOrganizationTitle());
        textViewDescription.setText(dataModelList.get(position).getOrganizationDescription());
        Glide.with(context)
                .load(dataModelList.get(position).getOrganizationImageURL())
                .into(imageView);

        return convertView;
    }
}
