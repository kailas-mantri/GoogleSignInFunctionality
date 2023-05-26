package com.onlyforfuture.helpinghands.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onlyforfuture.helpinghands.R;

import java.util.List;

public class organisationListAdapter extends BaseAdapter {

    private Context context;
    private List<GetOrganizationListDataModel> listDataModelList;
    TextView textType;

    public organisationListAdapter(Context context, List<GetOrganizationListDataModel> listDataModelList) {
        this.context = context;
        this.listDataModelList = listDataModelList;
    }

    @Override
    public int getCount() { return listDataModelList.size(); }

    @Override
    public Object getItem(int position) {
        return listDataModelList.get(position).getOrganizationTitle();
    }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_organizations_list_category,parent,false);
        TextView textView = convertView.findViewById(R.id.organization_list_category_Title_name);
        textType = convertView.findViewById(R.id.organisation_list_category_type_title_name);
        ImageView imageView = convertView.findViewById(R.id.display_organization_list_category_image);
        TextView textDescription = convertView.findViewById(R.id.organisation_list_category_type_description);

        getCategoryName(position);

        Glide.with(context)
                .load(listDataModelList.get(position).getOrganizationImageURL())
                .into(imageView);
        textView.setText(listDataModelList.get(position).getOrganizationTitle());
        textDescription.setText(listDataModelList.get(position).getOrganizationDescription());

        return convertView;
    }

    public void getCategoryName(int position) {
        String categoryID = listDataModelList.get(position).getOrganizationType();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OrganizationCategory");
        reference.child(categoryID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    GetOrganizationCategoryDataModel model = dataSnapshot.getValue(GetOrganizationCategoryDataModel.class);
                    textType.setText(model.getCategoryTitle());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
