package com.example.android.miveh2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.android.miveh2.R;

public class SongArrayAdapter extends BaseAdapter implements SpinnerAdapter {

    String[] company;
    Context context;

    public SongArrayAdapter(Context context, String[] company) {
        this.company = company;
        this.context = context;
    }

    @Override
    public int getCount() {
        return company.length;
    }

    @Override
    public Object getItem(int position) {
        return company[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  View.inflate(context, R.layout.music_spinner, null);
        TextView textView = (TextView) view.findViewById(R.id.text1);
        textView.setText(company[position]);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view;
        view =  View.inflate(context, R.layout.simple_spinner_dropdown_item, null);
        final CheckedTextView textView = (CheckedTextView) view.findViewById(R.id.chkTextView);
        textView.setText(company[position]);

        return view;
    }
}
