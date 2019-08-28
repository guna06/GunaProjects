package com.sqindia.srinarapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sqindia.srinarapp.Model.MachineList;
import com.sqindia.srinarapp.Model.PartList;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.SuperAdmin.AddSingleParts;
import com.sqindia.srinarapp.SuperAdmin.ViewMAchineEntry;
import com.sqindia.srinarapp.SuperAdmin.ViewParts;

import java.util.List;


/**
 * Created by Guna on 29-11-2017.
 */

public class PartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<PartList> partLists;
    SharedPreferences.Editor editor;
    Typeface lato;


    public PartListAdapter(ViewParts viewParts, List<PartList> partLists) {
        this.context=viewParts;
        this.partLists=partLists;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_part_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyHolder myHolder= (MyHolder) holder;
        final PartList current=partLists.get(position);
        int p=position+1;
        myHolder.sno_tv.setText(String.valueOf(p));
        String month_split=current.PL_month.substring(0,3);
        myHolder.partid_tv.setText(month_split);
        myHolder.mainpart_tv.setText(current.PL_mainpart);
        myHolder.subpart_tv.setText(current.PL_subpart);
        myHolder.initial_tv.setText(current.PL_initialqty);
        myHolder.dec_tv.setText(current.PL_Description);
        myHolder.multifactor_tv.setText(current.Pl_qty_multifactor);

        lato = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");
        myHolder.edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                final PartList current=partLists.get(position);
                Intent editpage=new Intent(context, AddSingleParts.class);
                editpage.putExtra("mainpart",current.PL_mainpart);
                editpage.putExtra("subpart",current.PL_subpart);
                editpage.putExtra("initialqty",current.PL_initialqty);
                editpage.putExtra("description",current.PL_Description);
                editpage.putExtra("partid",current.PL_partid);
                editpage.putExtra("multifactor",current.Pl_qty_multifactor);
                context.startActivity(editpage);
            }
        });
    }


    @Override
    public int getItemCount() {
        return partLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView sno_tv,partid_tv,mainpart_tv,subpart_tv,initial_tv,dec_tv,edit_icon,multifactor_tv;
        // create constructor to get widget reference

        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");
            sno_tv = itemView.findViewById(R.id.sno_tv);
            partid_tv = itemView.findViewById(R.id.partid_tv);
            mainpart_tv=itemView.findViewById(R.id.mainpart_tv);
            subpart_tv=itemView.findViewById(R.id.subpart_tv);
            initial_tv=itemView.findViewById(R.id.initial_tv);
            dec_tv=itemView.findViewById(R.id.dec_tv);
            edit_icon=itemView.findViewById(R.id.edit_icon);
            multifactor_tv=itemView.findViewById(R.id.multifactor_tv);

            sno_tv.setTypeface(tf);
            partid_tv.setTypeface(tf);
            mainpart_tv.setTypeface(tf);
            subpart_tv.setTypeface(tf);
            initial_tv.setTypeface(tf);
            dec_tv.setTypeface(tf);
            multifactor_tv.setTypeface(tf);
        }
    }
}
