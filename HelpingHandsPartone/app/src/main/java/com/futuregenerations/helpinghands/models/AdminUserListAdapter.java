package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminUserListAdapter extends BaseAdapter {

    private Context context;
    private final List<UserDataModel> dataModelList;

    public AdminUserListAdapter(Context context, List<UserDataModel> dataModelList) {
        this.context = context;
        this.dataModelList = dataModelList;
    }

    @Override
    public int getCount() {
        return dataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelList.get(position).getUserName();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_admin_user_list_layout,parent,false);

        TextView textViewName = convertView.findViewById(R.id.admin_user_list_user_name);
        TextView textViewEmail = convertView.findViewById(R.id.admin_user_list_user_email);
        View dividerView = convertView.findViewById(R.id.admin_user_list_divider_view);
        CircleImageView imageView = convertView.findViewById(R.id.admin_user_list_user_image);

        int lastPosition = getCount();

        textViewName.setText(dataModelList.get(position).getUserName());
        textViewEmail.setText(dataModelList.get(position).getUserEmail());

        if (position == lastPosition-1) {
            dividerView.setVisibility(View.INVISIBLE);
        }

        String imageURL = dataModelList.get(position).getUserImage();
        String oldQuality = "=s96-c";
        String originalQuality = "=s100-c";
        String userImage = imageURL.replace(oldQuality,originalQuality);

        Glide.with(context)
                .load(userImage)
                .into(imageView);

        return convertView;
    }
}
