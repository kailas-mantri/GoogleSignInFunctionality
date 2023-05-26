package com.leadsoft.smart_campus.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.leadsoft.smart_campus.R;

public class Saved_Company extends Fragment {

    RecyclerView recyclerView;

    public Saved_Company() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved_company,container,false);

        recyclerView = rootView.findViewById(R.id.recycler_Saved_Company);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
