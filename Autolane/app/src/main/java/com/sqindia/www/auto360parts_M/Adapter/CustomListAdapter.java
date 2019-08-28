package com.sqindia.www.auto360parts_M.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sqindia.www.auto360parts_M.R;

import java.util.ArrayList;


/**
 * Created by Admin on 07-10-2016.
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private Context context;
    private int layout;
    ArrayList<String> source;

    public CustomListAdapter(Context context, int layout , ArrayList<String> source) {
        super(context, R.layout.spinner_item_list,source);
        this.context = context;
        this.layout = layout;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = super.getView(position, convertView, parent);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/helevetical.ttf");

        TextView suggestion = (TextView) view.findViewById(R.id.text);

        return view;
    }


}
