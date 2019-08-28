package com.sqindia.srinarapp.SuperAdmin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sqindia.srinarapp.MachiningModule.QcMachineJob;
import com.sqindia.srinarapp.Model.MachineJobList;
import com.sqindia.srinarapp.Model.QcApproverList;
import com.sqindia.srinarapp.R;

import java.util.List;


/**
 * Created by Guna on 29-11-2017.
 */

public class ShowQcAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private DialogShowQcList context;
    public LayoutInflater inflater;
    private List<QcApproverList> qcApproverLists;
    SharedPreferences.Editor editor;
    String session_token;


    public ShowQcAdapter(DialogShowQcList dialogShowQcList, List<QcApproverList> qcApproverLists) {

        this.context=dialogShowQcList;
        this.qcApproverLists=qcApproverLists;
        inflater = LayoutInflater.from(context.activity);

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.show_qc_row, parent, false);

        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.activity);
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        final QcApproverList current = qcApproverLists.get(position);
        myHolder.qty_tv.setText(current.qc_by);
        myHolder.name_tv.setText(current.qty_approved);
        myHolder.date_tv.setText(current.qc_date);

    }


    @Override
    public int getItemCount() {
        return qcApproverLists.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView qty_tv,name_tv,date_tv;
        public ImageView status_img;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.activity.getAssets(), "fonts/segoeuil.ttf");
            qty_tv = (TextView) itemView.findViewById(R.id.qty_tv);
            name_tv = (TextView) itemView.findViewById(R.id.name_tv);
            date_tv=itemView.findViewById(R.id.date_tv);

            qty_tv.setTypeface(tf);
            name_tv.setTypeface(tf);
            date_tv.setTypeface(tf);
        }
    }
}
