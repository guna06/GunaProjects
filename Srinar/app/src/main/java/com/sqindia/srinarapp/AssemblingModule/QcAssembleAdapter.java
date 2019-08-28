package com.sqindia.srinarapp.AssemblingModule;

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
import com.sqindia.srinarapp.Model.AssemblingJobList;
import com.sqindia.srinarapp.Model.QcAssemblingPojo;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.SuperAdmin.DialogShowQcList;

import java.util.List;


/**
 * Created by Guna on 29-11-2017.
 */

public class QcAssembleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<QcAssemblingPojo> assemblingJobLists;
    SharedPreferences.Editor editor;
    String token,get_batchid,get_quantity,session_token,str_dept,str_name,str_role,set_approved;
    Typeface lato;

    public QcAssembleAdapter(QcAssemblingJob qcAssemblingJob, List<QcAssemblingPojo> assemblingJobLists) {

        this.context=qcAssemblingJob;
        this.assemblingJobLists=assemblingJobLists;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.qc_assem_job_row, parent, false);
        MyHolder holder = new MyHolder(view);
        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        str_dept=sharedPreferences.getString("userpermission","");
        str_name=sharedPreferences.getString("emp_name","");
        str_role=sharedPreferences.getString("str_role","");
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final QcAssemblingPojo current=assemblingJobLists.get(position);

        myHolder.part_tv.setText(current.QMP_mainpart);
        myHolder.subpart_tv.setText(current.QMP_submainpart);
        myHolder.drescription_tv.setText(current.QMP_desc);
        myHolder.partmonth_tv.setText(current.QMP_month);
        myHolder.initialqty_tv.setText(current.QMP_initial);
        myHolder.completed_tv.setText(current.QMP_completed);
        myHolder.approved_tv.setText(current.QMP_approved);


        int int_complete=Integer.parseInt(current.QMP_completed);
        int int_approve=Integer.parseInt(current.QMP_approved);
        int int_pending=int_complete-int_approve;

        myHolder.pending_tv.setText(String.valueOf(int_pending));


       if(myHolder.approved_tv.getText().toString().equals("0"))
        {
            myHolder. status_img.setImageResource(R.drawable.red_check);
        }
        else if(myHolder.approved_tv.getText().toString().equals(current.QMP_completed))
        {
            myHolder. status_img.setImageResource(R.drawable.green_check);
        }
        else
        {

            myHolder. status_img.setImageResource(R.drawable.yellow_check);
        }


       /* myHolder.qc_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssemblingJobList current=assemblingJobLists.get(position);
                SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor edit = sharedPrefces.edit();
                edit.putString("qc_batchid", current._id);
                edit.putString("qc_tot_qty", current._id);
                edit.commit();

                DialogShowQcList cdd = new DialogShowQcList((Activity) context);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
        });*/


        myHolder.status_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QcAssemblingPojo current=assemblingJobLists.get(position);
               /* get_batchid = current._id;
                get_quantity =current.ass_quantityaompleted;
                set_approved=current.ass_approved;
                Log.e("tag","Vendor"+set_approved);*/

                if(!myHolder.approved_tv.getText().toString().equals(current.QMP_completed))
                {

                        if(str_dept.equals("MCECAC"))
                        {
                            SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor edit = sharedPrefces.edit();
                            edit.putString("dialog_part_id_ass", current.QMP_part_id);
                            edit.putString("dialog_complete_qty_ass",current.QMP_completed);
                            edit.putString("dialog_approved_qty_ass",current.QMP_approved);
                            edit.commit();


                            DialogSignatureAssembling cdd = new DialogSignatureAssembling((Activity) context);
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            cdd.show();
                        }

                        if(str_dept.equals("AEC"))
                        {
                            SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor edit = sharedPrefces.edit();
                            edit.putString("dialog_part_id_ass", current.QMP_part_id);
                            edit.putString("dialog_complete_qty_ass",current.QMP_completed);
                            edit.putString("dialog_approved_qty_ass",current.QMP_approved);
                            edit.commit();


                            DialogSignatureAssembling cdd = new DialogSignatureAssembling((Activity) context);
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            cdd.show();
                        }


                        else if(str_dept.equals("ALL"))
                        {
                            SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor edit = sharedPrefces.edit();
                            edit.putString("dialog_part_id_ass", current.QMP_part_id);
                            edit.putString("dialog_complete_qty_ass",current.QMP_completed);
                            edit.putString("dialog_approved_qty_ass",current.QMP_approved);
                            edit.commit();


                            DialogSignatureAssembling cdd = new DialogSignatureAssembling((Activity) context);
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            cdd.show();
                        }
                        else
                        {
                            Toast.makeText(context,"Not accessed for Assembling.....",Toast.LENGTH_LONG).show();
                        }


                }
                else
                {
                    Toast.makeText(context,"This part completed and moved to next level...",Toast.LENGTH_LONG).show();
                }

            }
        });


        lato = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");
    }


    @Override
    public int getItemCount() {
        return assemblingJobLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView part_tv,subpart_tv,drescription_tv,partmonth_tv,completed_tv,approved_tv,pending_tv,status_tv,initialqty_tv;
        public ImageView status_img,qcstate_img;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");
            part_tv = (TextView) itemView.findViewById(R.id.part_tv);
            subpart_tv = (TextView) itemView.findViewById(R.id.subpart_tv);
            drescription_tv=itemView.findViewById(R.id.drescription_tv);
            partmonth_tv=itemView.findViewById(R.id.partmonth_tv);
            completed_tv=itemView.findViewById(R.id.completed_tv);
            approved_tv=itemView.findViewById(R.id.approved_tv);
            pending_tv=itemView.findViewById(R.id.pending_tv);
            status_tv=itemView.findViewById(R.id.status_tv);
            initialqty_tv=itemView.findViewById(R.id.initialqty_tv);
            status_img=itemView.findViewById(R.id.status_img);
            qcstate_img=itemView.findViewById(R.id.qcstate_img);


            part_tv.setTypeface(tf);
            subpart_tv.setTypeface(tf);
            drescription_tv.setTypeface(tf);
            partmonth_tv.setTypeface(tf);
            completed_tv.setTypeface(tf);
            approved_tv.setTypeface(tf);
            pending_tv.setTypeface(tf);
            status_tv.setTypeface(tf);
            initialqty_tv.setTypeface(tf);


        }
    }
}
