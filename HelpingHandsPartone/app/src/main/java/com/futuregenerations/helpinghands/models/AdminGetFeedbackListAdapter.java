package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.futuregenerations.helpinghands.R;

import java.util.List;

public class AdminGetFeedbackListAdapter extends BaseAdapter {

    private Context context;
    private final List<FeedbackDataModel> dataModelList;

    public AdminGetFeedbackListAdapter(Context context, List<FeedbackDataModel> dataModelList) {
        this.context = context;
        this.dataModelList = dataModelList;
    }
    @Override
    public int getCount() {
        return dataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelList.get(position).getUserFeedback();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_get_feedback_list_item_layout,parent,false);
        TextView textViewFeedback, textViewDate, textViewTime;
        textViewFeedback = convertView.findViewById(R.id.admin_get_feedback_list_item_user_feedback);
        textViewDate = convertView.findViewById(R.id.admin_get_feedback_list_item_date);
        textViewTime = convertView.findViewById(R.id.admin_get_feedback_list_item_time);
        textViewFeedback.setText(dataModelList.get(position).getUserFeedback());
        textViewDate.setText(dataModelList.get(position).getDate());
        textViewTime.setText(dataModelList.get(position).getTime());
        return convertView;
    }
}
