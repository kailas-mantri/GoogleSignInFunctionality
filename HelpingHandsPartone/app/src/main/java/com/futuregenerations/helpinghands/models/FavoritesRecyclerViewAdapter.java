package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.R;

import java.util.List;

public class FavoritesRecyclerViewAdapter extends RecyclerView.Adapter<FavoritesRecyclerViewAdapter.FavoritesViewHolder> {

    Context context;
    List<GetOrganizationsDataModel> organizationsDataModelList;

    public FavoritesRecyclerViewAdapter(Context context, List<GetOrganizationsDataModel> organizationsDataModelList) {
        this.context = context;
        this.organizationsDataModelList = organizationsDataModelList;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_card_view_item,parent,false);
        FavoritesViewHolder holder = new FavoritesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Glide.with(context)
                .load(organizationsDataModelList.get(position).getOrganizationImageURL())
                .into(holder.imageView);

        holder.textViewOrganization.setText(organizationsDataModelList.get(position).getOrganizationTitle());
        holder.textViewCategory.setText(organizationsDataModelList.get(position).getOrganizationCategory());
    }

    @Override
    public int getItemCount() {
        return organizationsDataModelList.size();
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewCategory, textViewOrganization;
        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.organization_image);
            textViewCategory = itemView.findViewById(R.id.category);
            textViewOrganization = itemView.findViewById(R.id.organization_title);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView view) {
        super.onAttachedToRecyclerView(view);
    }
}
