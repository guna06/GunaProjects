package autohubb.vinture.com.autohubb.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.R;

public class CustomAdapterArrayList extends ArrayAdapter<String> {

    private Context context;
    private int layout;
    List<String> source;

    public CustomAdapterArrayList(Context applicationContext, int simple_dropdown_item_1line, ArrayList<String> caryear_arraylist) {
        super(applicationContext, R.layout.spinner_item_list,caryear_arraylist);
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
