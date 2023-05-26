package com.onlyforfuture.helpinghands.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.onlyforfuture.helpinghands.R;

public class HistoryFragment extends Fragment {

    public HistoryFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle SavedInstance){
        super.onCreate(SavedInstance);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle SavedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_history,container,false);

        return rootView;
    }
}
