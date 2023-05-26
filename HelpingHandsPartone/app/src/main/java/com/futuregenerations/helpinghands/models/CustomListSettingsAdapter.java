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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.futuregenerations.helpinghands.R;

public class CustomListSettingsAdapter extends ArrayAdapter<String> {

    private Context context;
    private final String title[];
    private final int drawables[];
    boolean dark=false;
    public CustomListSettingsAdapter(@NonNull Context context, String title[], int drawables[]) {
        super(context, R.layout.custom_settings_list_item_layout,title);
        this.context = context;
        this.title = title;
        this.drawables = drawables;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_settings_list_item_layout,null,true);
        TextView textView = convertView.findViewById(R.id.settings_item_text_title);
        textView.setText(title[position]);
        boolean correct = isDark();
        if (correct) {
            Drawable drawable = context.getResources().getDrawable(drawables[position]);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable,context.getResources().getColor(android.R.color.white));
            textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(drawables[position]),null,null,null);
        }
        else {
            textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(drawables[position]),null,null,null);
        }
        return convertView;
    }

    private boolean isDark() {
        String themeMode = UserSharedPrefManager.getInstance(context).getData().getThemeName();

        if (TextUtils.equals(themeMode,context.getResources().getString(R.string.theme_dark))) {
            dark = true;
        }
        else if (TextUtils.equals(themeMode,context.getResources().getString(R.string.theme_system)))
        {
            switch (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
            {
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
