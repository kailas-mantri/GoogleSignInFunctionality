package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.futuregenerations.helpinghands.R;

import java.util.List;

public class PaymentsRecyclerViewAdapter extends RecyclerView.Adapter<PaymentsRecyclerViewAdapter.PaymentsViewHolder> {

    Context context;
    List<UserPaymentModel> paymentModelList;

    public PaymentsRecyclerViewAdapter(Context context, List<UserPaymentModel> paymentModelList) {
        this.context = context;
        this.paymentModelList = paymentModelList;
    }

    @NonNull
    @Override
    public PaymentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payments_card_view_item,parent,false);
        PaymentsViewHolder holder = new PaymentsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentsViewHolder holder, int position) {
        holder.textViewTransactionID.setText(paymentModelList.get(position).getPaymentDetails().getTransactionID());
        holder.textViewDate.setText(paymentModelList.get(position).getPaymentDetails().getDonationDate());
        holder.textViewAmount.setText(paymentModelList.get(position).getPaymentDetails().getDonationAmount());
    }

    @Override
    public int getItemCount() {
        return paymentModelList.size();
    }

    public class PaymentsViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTransactionID, textViewDate, textViewAmount;

        public PaymentsViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTransactionID = itemView.findViewById(R.id.transaction_id);
            textViewDate = itemView.findViewById(R.id.date);
            textViewAmount = itemView.findViewById(R.id.amount);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView view) {
        super.onAttachedToRecyclerView(view);
    }
}
