package com.speedsol.generations.future.navigationdrawerscrima;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class CustomDialogClass extends AlertDialog implements android.view.View.OnClickListener {

    public Context context;
    public AlertDialog dialog;
    public int image;

    public CustomDialogClass(Context context, int image) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        this.image = image;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog_layout);
        ImageView imageView = findViewById(R.id.imgDialog);
        imageView.setImageResource(image);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
