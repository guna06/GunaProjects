package com.sqindia.srinarapp.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.sqindia.srinarapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 07-10-2016.
 */
public class CustomAdapterArrayList extends ArrayAdapter<String> {

    private Context context;
    private int layout;
    List<String> source;

    public CustomAdapterArrayList(Context applicationContext, int simple_dropdown_item_1line, ArrayList<String> machine_name) {
        super(applicationContext, R.layout.spinner_item_list,machine_name);
        this.context = applicationContext;
        this.layout = simple_dropdown_item_1line;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = super.getView(position, convertView, parent);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/opensans.ttf");

        TextView suggestion = (TextView) view.findViewById(R.id.text);

        return view;
    }


}
