package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;

import com.futuregenerations.helpinghands.R;

public class AdminSettingsListAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] settingsItemTitle;
    private int[] settingsItemDrawable;
    boolean dark=false;

    public AdminSettingsListAdapter(Context context,String[] settingsItemTitle, int[] settingsItemDrawable) {
        super(context, R.layout.custom_settings_list_item_layout,settingsItemTitle);

        this.context = context;
        this.settingsItemDrawable = settingsItemDrawable;
        this.settingsItemTitle = settingsItemTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_settings_list_item_layout,parent,false);
        TextView textViewTitle = convertView.findViewById(R.id.settings_item_text_title);
        textViewTitle.setText(settingsItemTitle[position]);
        boolean correct = isDark();
        if (correct) {
            Drawable drawable = context.getResources().getDrawable(settingsItemDrawable[position]);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable,context.getResources().getColor(android.R.color.white));
            textViewTitle.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(settingsItemDrawable[position]),null,null,null);
        }
        else {
            textViewTitle.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(settingsItemDrawable[position]),null,null,null);
        }
        return convertView;
    }

    private boolean isDark() {
        String themeMode = AdminSharedPrefManager.getInstance(context).getTheme();

        if (TextUtils.equals(themeMode,context.getResources().getString(R.string.theme_dark))) {
            dark = true;
        }
        else if (TextUtils.equals(themeMode,context.getResources().getString(R.string.theme_system))) {
            switch (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES :
                    dark = true;
                    break;

                case Configuration.UI_MODE_NIGHT_NO :
                    dark = false;
                    break;
            }
        }

        else {
            dark = false;
        }
        return dark;
    }
}
