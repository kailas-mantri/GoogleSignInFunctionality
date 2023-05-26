package com.speedsol.generations.future.fragmentscrima;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


@SuppressLint("ValidFragment")
public class Fragment2 extends android.support.v4.app.Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2,container,false);
        TextView textView = (TextView)view.findViewById(R.id.tvtext2);
        textView.setText("This is Fragment-2");
        return view;
    }
}