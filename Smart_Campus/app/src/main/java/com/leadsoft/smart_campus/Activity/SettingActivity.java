package com.leadsoft.smart_campus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.leadsoft.smart_campus.R;

public class SettingActivity extends AppCompatActivity {

    ListView listViewSettings;

    String title[] = {"Theme","Notifications", "Feedback", "About Us", "Sign Out"};
    int drawable[] = {R.drawable.ic_theme,R.drawable.ic_notifications,R.drawable.ic_send,R.drawable.ic_about,R.drawable.ic_signout};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        listViewSettings = findViewById(R.id.listSettings);

        CustomListSettingAdaptor adaptor = new CustomListSettingAdaptor(SettingActivity.this,title,drawable);
        listViewSettings.setAdapter(adaptor);
    }
}
class CustomListSettingAdaptor extends BaseAdapter {

    private Context context;
    private final String title[];
    private final int drawable[];

    public CustomListSettingAdaptor(Context context, String[] title, int[] drawable) {
        this.context = context;
        this.title = title;
        this.drawable = drawable;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return title[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.setting_list_item,parent,false);
        TextView settingItem = convertView.findViewById(R.id.text_setting_item);
        settingItem.setText(title[position]);
        settingItem.setCompoundDrawablesWithIntrinsicBounds(drawable[position],0,0,0);
        return convertView;
    }
}