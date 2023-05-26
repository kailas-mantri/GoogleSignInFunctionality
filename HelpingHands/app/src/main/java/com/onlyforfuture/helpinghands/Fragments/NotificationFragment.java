package com.onlyforfuture.helpinghands.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onlyforfuture.helpinghands.R;
import com.tuyenmonkey.mkloader.MKLoader;

public class NotificationFragment extends Fragment {

    RecyclerView recyclerViewNotifications;
    MKLoader loader;

    public NotificationFragment(){ }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_notifications,container,false);

        recyclerViewNotifications = rootView.findViewById(R.id.recyclerViewNotifications);
        recyclerViewNotifications.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewNotifications.setLayoutManager(layoutManager);

        loader = rootView.findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);

        return rootView;
    }

}
