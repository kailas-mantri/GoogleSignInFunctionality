package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.futuregenerations.helpinghands.activities.admin.AdminEditOrganizationsActivity;
import com.futuregenerations.helpinghands.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class OrganizationsListAdapter extends BaseAdapter {

    private Context context;
    private final List<GetOrganizationsDataModel> dataModelList;

    public OrganizationsListAdapter(Context context, List<GetOrganizationsDataModel> dataModelList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_organizations_list,parent,false);
        TextView textViewTitle = convertView.findViewById(R.id.title_organizations);
        final TextView textViewType = convertView.findViewById(R.id.type_organizations);
        TextView textViewDescription = convertView.findViewById(R.id.description_organization);
        ImageView imageView = convertView.findViewById(R.id.image_organizations);

        SwipeLayout swipeLayout = convertView.findViewById(R.id.swipeLayout);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left,swipeLayout.findViewById(R.id.linearlayoutswipeleft));
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right,swipeLayout.findViewById(R.id.linearlayoutswiperight));

        textViewType.setText(dataModelList.get(position).getOrganizationCategory());
        textViewTitle.setText(dataModelList.get(position).getOrganizationTitle());
        textViewDescription.setText(dataModelList.get(position).getOrganizationDescription());
        Glide.with(context)
                .load(dataModelList.get(position).getOrganizationImageURL())
                .into(imageView);

        swipeLayout.findViewById(R.id.delete_slider_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String organizationID = dataModelList.get(position).getOrganizationID();
                deleteSlider(organizationID,position);
                Toast.makeText(context, "You Clicked Delete "+dataModelList.get(position).getOrganizationID(), Toast.LENGTH_SHORT).show();
            }
        });

        swipeLayout.findViewById(R.id.update_slider_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(context, AdminEditOrganizationsActivity.class);
                String orgID = dataModelList.get(position).getOrganizationID();
                String orgType = dataModelList.get(position).getOrganizationType();
                editIntent.putExtra("orgID",orgID);
                editIntent.putExtra("orgType",orgType);
                context.startActivity(editIntent);
            }
        });

        return convertView;
    }
    private void deleteSlider(String organizationID, final int position) {
        DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference().child("Organizations");

        List<String> list = dataModelList.get(position).getOrganizationImages();
        int i;
        for (i=0;i<list.size();i++) {
            String url = dataModelList.get(position).getOrganizationImages().get(i);
            StorageReference myRef = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            myRef.delete();
        }

        deleteRef.child(dataModelList.get(position).getOrganizationType()).child(organizationID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
        StorageReference imageDeleteRef = FirebaseStorage.getInstance().getReferenceFromUrl(dataModelList.get(position).getOrganizationImageURL());
        imageDeleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Slider Item Deleted", Toast.LENGTH_SHORT).show();
                dataModelList.remove(position);
                notifyDataSetChanged();
            }
        });
    }
}
