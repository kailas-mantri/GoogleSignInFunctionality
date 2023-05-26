package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.futuregenerations.helpinghands.R;

import java.util.List;

public class NotificationListAdapter extends BaseAdapter {

    private Context context;
    private List<NotificationDataModel> dataModelList;

    public NotificationListAdapter(Context context, List<NotificationDataModel> dataModelList) {
        this.context = context;
        this.dataModelList = dataModelList;
    }

    @Override
    public int getCount() {
        return dataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelList.get(position).getNotification();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_notifications_list_layout,parent,false);
        TextView textViewNotification = convertView.findViewById(R.id.text_notification);
        TextView textViewDate = convertView.findViewById(R.id.text_notification_date);
        TextView textViewTime = convertView.findViewById(R.id.text_notification_time);

        textViewNotification.setText(dataModelList.get(position).getNotification());
        textViewDate.setText(dataModelList.get(position).getDate());
        textViewTime.setText(dataModelList.get(position).getTime());

        return convertView;
    }
}
