package com.futuregenerations.helpinghands.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminFeedbackUserListAdapter extends BaseAdapter {

    private Context context;
    private final List<FeedbackUserDataModel> dataModelList;

    public AdminFeedbackUserListAdapter(Context context, List<FeedbackUserDataModel> dataModelList) {
        this.context = context;
        this.dataModelList = dataModelList;
    }
    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return dataModelList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return dataModelList.get(position).getUserID();
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_admin_feedback_user_list_layout,parent,false);

        CircleImageView imageView;
        TextView textView;
        imageView = convertView.findViewById(R.id.admin_feedback_user_list_item_user_image);
        textView = convertView.findViewById(R.id.admin_feedback_user_list_item_user_name);

        textView.setText(dataModelList.get(position).getUserName());
        String imageURL = dataModelList.get(position).getUserImage();
        String oldResolution = "s96-c";
        String newResolution = "s100-c";
        String userImage = imageURL.replace(oldResolution,newResolution);

        Glide.with(context)
                .load(userImage)
                .into(imageView);

        return convertView;
    }
}
