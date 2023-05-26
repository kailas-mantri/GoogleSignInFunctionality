package com.speedsol.generations.future.fragmentscrima;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


@SuppressLint("ValidFragment")
public class Fragment1 extends android.support.v4.app.Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag1,container,false);
        TextView textView = (TextView)view.findViewById(R.id.tvtext1);
        textView.setText("This is Fragment-1");
        return view;
    }
}
