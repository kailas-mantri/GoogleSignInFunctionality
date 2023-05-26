package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.futuregenerations.helpinghands.R;

import java.util.List;

public class PaymentHistoryAdapter extends BaseAdapter {

    private Context context;
    private final List<UserPaymentModel> paymentModelList;

    public PaymentHistoryAdapter(Context context, List<UserPaymentModel> paymentModelList) {
        this.context = context;
        this.paymentModelList = paymentModelList;
    }

    @Override
    public int getCount() {
        return paymentModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return paymentModelList.get(position).getOrganizationDetails().getOrganizationTitle();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_history_list_layout,parent,false);
        TextView textViewTransaction = convertView.findViewById(R.id.text_transaction_id);
        TextView textViewOrganization = convertView.findViewById(R.id.text_org_name);
        TextView textViewAmount = convertView.findViewById(R.id.text_amount);

        textViewAmount.setText("RS. "+paymentModelList.get(position).getPaymentDetails().getDonationAmount()+"/-");
        textViewOrganization.setText(paymentModelList.get(position).getOrganizationDetails().getOrganizationTitle());
        textViewTransaction.setText(paymentModelList.get(position).getPaymentDetails().getTransactionID());
        return convertView;
    }
}
