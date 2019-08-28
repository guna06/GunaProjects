package com.sqindia.srinarapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.SuperAdmin.ViewMAchineEntry;
import com.sqindia.srinarapp.Model.MachineList;


import java.util.List;


/**
 * Created by Guna on 29-11-2017.
 */

public class MachineListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<MachineList> machineLists;

    public MachineListAdapter(ViewMAchineEntry viewMAchineEntry, List<MachineList> machineListListModel) {
        this.context=viewMAchineEntry;
        this.machineLists=machineListListModel;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.machine_adapter, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final MachineList current=machineLists.get(position);
        myHolder.sno_tv.setText(current._id);
        myHolder.macname_tv.setText(current.machine_name);
        myHolder.dept_tv.setText(current.machine_dept);


    }


    @Override
    public int getItemCount() {
        return machineLists.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView sno_tv,macname_tv,dept_tv,del_tv;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");
            sno_tv = itemView.findViewById(R.id.sno_tv);
            macname_tv = itemView.findViewById(R.id.macname_tv);
            dept_tv=itemView.findViewById(R.id.dept_tv);

            sno_tv.setTypeface(tf);
            macname_tv.setTypeface(tf);
            dept_tv.setTypeface(tf);

        }
    }
}
