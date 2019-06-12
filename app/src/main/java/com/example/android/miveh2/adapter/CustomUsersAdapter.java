package com.example.android.miveh2.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.android.miveh2.R;
import com.example.android.miveh2.model.User;

import java.util.ArrayList;

public class CustomUsersAdapter extends ArrayAdapter<User> {
    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    public CustomUsersAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        // Get the data item for this position
        User user = getItem(position);

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        // Populate the data into the template view using the data object
        tvName.setText(user.getName());
        tvName.setTag(user.getId());
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        checkBox.setText(user.getId());

        // Return the completed view to render on screen
        return convertView;
    }
}