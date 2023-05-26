package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class AdminPaymentsUserListAdapter extends BaseAdapter {

    private Context context;
    private List<UserPaymentModel> paymentModelList;
    List<String> count;

    public AdminPaymentsUserListAdapter(Context context, List<UserPaymentModel> paymentModelList, List<String> count) {
        this.context = context;
        this.paymentModelList = paymentModelList;
        this.count = count;
    }

    @Override
    public int getCount() {
        return paymentModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return paymentModelList.get(position).getUserDetails().getUserName();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.admin_payment_user_list_layout,parent,false);
        RoundedImageView imageView = convertView.findViewById(R.id.user_image);
        TextView textViewName = convertView.findViewById(R.id.user_name);
        TextView textViewCount = convertView.findViewById(R.id.num_payments);
        textViewName.setText(getItem(position).toString());
        textViewCount.setText(count.get(position)+" Payments");
        Glide.with(context)
                .load(paymentModelList.get(position).getUserDetails().getUserImage())
                .into(imageView);
        return convertView;
    }
}
