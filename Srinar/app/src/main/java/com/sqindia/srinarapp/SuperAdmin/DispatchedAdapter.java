package com.sqindia.srinarapp.SuperAdmin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.srinarapp.ElectroplatingModule.DialogSignatureElectroplate;
import com.sqindia.srinarapp.ElectroplatingModule.QcElectroplateJob;
import com.sqindia.srinarapp.Model.DispatchedList;
import com.sqindia.srinarapp.Model.ElectroplateJobList;
import com.sqindia.srinarapp.R;

import java.util.List;


/**
 * Created by Guna on 29-11-2017.
 */

public class DispatchedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<DispatchedList> dispatchedLists;
    SharedPreferences.Editor editor;
    String token,get_batchid,get_quantity;
    Typeface lato;
    String session_token,str_dept,str_name,str_role;



   public DispatchedAdapter(DispatchProduct dispatchProduct, List<DispatchedList> dispatchedLists) {

        this.context=dispatchProduct;
        this.dispatchedLists=dispatchedLists;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dispatch_machine_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final DispatchedList current=dispatchedLists.get(position);
        int sno=position+1;
        myHolder.sno_tv.setText(String.valueOf(sno));
        myHolder.subpart_tv.setText(current.dis_subpart);
        myHolder.desc_tv.setText(current.dis_desc);
        myHolder.initial_tv.setText(current.dis_initial);
        myHolder.ass_approved_tv.setText(current.dis_assemble_approved);
        myHolder.dispatched_tv.setText(current.dis_dispatched);
        myHolder.dispending_tv.setText(current.dis_pending);

        lato = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");
    }


    @Override
    public int getItemCount() {
        return dispatchedLists.size();

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView sno_tv,dispending_tv,dispatched_tv,ass_approved_tv,initial_tv,desc_tv,subpart_tv;
        public ImageView status_img;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");
            sno_tv = itemView.findViewById(R.id.sno_tv);
            dispending_tv = itemView.findViewById(R.id.dispending_tv);
            dispatched_tv=itemView.findViewById(R.id.dispatched_tv);
            ass_approved_tv = itemView.findViewById(R.id.ass_approved_tv);
            initial_tv =  itemView.findViewById(R.id.initial_tv);
            desc_tv=itemView.findViewById(R.id.desc_tv);
            subpart_tv=itemView.findViewById(R.id.subpart_tv);
            status_img=itemView.findViewById(R.id.status_img);


            sno_tv.setTypeface(tf);
            dispending_tv.setTypeface(tf);
            dispatched_tv.setTypeface(tf);
            ass_approved_tv.setTypeface(tf);
            initial_tv.setTypeface(tf);
            desc_tv.setTypeface(tf);
            subpart_tv.setTypeface(tf);

        }
    }
}
