package com.sqindia.srinarapp.AssemblingModule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.sqindia.srinarapp.Model.AssemblingJobList;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import org.json.JSONObject;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Guna on 29-11-2017.
 */

public class ViewAssembleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<AssemblingJobList> assemblingJobLists;
    SharedPreferences.Editor editor;
    String get_batchid,get_quantity,session_token,str_dept,str_name,str_role,employeeid,get_partid;
    Typeface lato;
    Dialog loader_dialog;

    public ViewAssembleAdapter(ViewAssemblingJob viewAssemblingJob, List<AssemblingJobList> assemblingJobLists) {

        this.context=viewAssemblingJob;
        this.assemblingJobLists=assemblingJobLists;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_machine_job_row, parent, false);
        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        employeeid = sharedPreferences.getString("emp_id", "");
        str_dept=sharedPreferences.getString("userpermission","");
        str_name=sharedPreferences.getString("emp_name","");
        str_role=sharedPreferences.getString("str_role","");


        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final AssemblingJobList current=assemblingJobLists.get(position);
        myHolder.sno_tv.setText(current._id);
        myHolder.shift_tv.setText(current.ass_shift);
        myHolder.machine_tv.setText(current.ass_machine);
        myHolder.operator_tv.setText(current.ass_operator);
        myHolder.part_tv.setText(current.ass_part);
        myHolder.subpart_tv.setText(current.ass_subpart);

        myHolder.date_tv.setText(current.ass_date);

        String str_status=current.ass_approved;
        if(employeeid.equals(current.ass_wcentryby))
        {
            myHolder.edit_img.setImageResource(R.drawable.red_del);
        }


       /* if(str_status.equals("0"))
        {
            myHolder. status_img.setImageResource(R.drawable.red_check);




        }
        else if(str_status.equals(current.ass_quantityaompleted))
        {
            myHolder. status_img.setImageResource(R.drawable.green_check);
        }
        else
        {
            myHolder. status_img.setImageResource(R.drawable.yellow_check);
        }*/



        myHolder.edit_img.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                AssemblingJobList current=assemblingJobLists.get(position);
                get_batchid = current._id;
                get_quantity = current.ass_quantityaompleted;
                get_partid=current.ass_partid;
                Log.e("tag","jjjjjjjjjjjjjjjjjj--------->"+get_batchid);
                //delete Entry
                loader_dialog.show();
                new deleteMachineEntryAsync().execute();

            }
        });


        myHolder.qty_tv.setText(current.ass_quantityaompleted);
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

        public TextView sno_tv,shift_tv,machine_tv,operator_tv,part_tv,subpart_tv,status_tv,qty_tv,approved_tv,rejected_tv,date_tv;
        public ImageView status_img,edit_img;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");
            sno_tv = itemView.findViewById(R.id.sno_tv);
            shift_tv =  itemView.findViewById(R.id.shift_tv);
            machine_tv=itemView.findViewById(R.id.machine_tv);

            operator_tv = itemView.findViewById(R.id.operator_tv);
            part_tv = itemView.findViewById(R.id.part_tv);
            subpart_tv=itemView.findViewById(R.id.subpart_tv);

            qty_tv=itemView.findViewById(R.id.qty_tv);
            status_img=itemView.findViewById(R.id.status_img);
            edit_img=itemView.findViewById(R.id.edit_img);
            date_tv=itemView.findViewById(R.id.date_tv);


            sno_tv.setTypeface(tf);
            shift_tv.setTypeface(tf);
            machine_tv.setTypeface(tf);
            operator_tv.setTypeface(tf);
            part_tv.setTypeface(tf);
            subpart_tv.setTypeface(tf);
            date_tv.setTypeface(tf);
            qty_tv.setTypeface(tf);


            //set default Loader:
            loader_dialog = new Dialog(context);
            loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loader_dialog.setCancelable(false);
            loader_dialog.setContentView(R.layout.test_loader);

        }
    }




    //@@@@@@@@@@@@@@@@@@@@@@@@ ALLOCATE MACHINE WORK API
    public class deleteMachineEntryAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("qtycompleted",get_quantity);
                jsonObject.accumulate("batchid",get_batchid);
                jsonObject.accumulate("partid",get_partid);


                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_DELETE_ASSEMBLE_WORK,json, session_token);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg =jo.getString("message");

                if (status.equals("true")) {
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("SUCCESS MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    Intent job_mac=new Intent(context,ViewAssemblingJob.class);
                                    context.startActivity(job_mac);
                                }
                            })
                            .show();


                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                }
                            })
                            .show();
                }
            } catch (Exception e) {

            }
        }

    }
}
