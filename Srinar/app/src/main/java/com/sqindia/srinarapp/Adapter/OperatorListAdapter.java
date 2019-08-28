package com.sqindia.srinarapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.SuperAdmin.ActivityNew;
import com.sqindia.srinarapp.Model.OperatorList;

import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Guna on 29-11-2017.
 */

public class OperatorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<OperatorList> operatorLists;
    SharedPreferences.Editor editor;
    String session_token,id;
    Typeface lato;
    ArrayList<String> names;
    Dialog loader_dialog;

    public OperatorListAdapter(ActivityNew viewOperatorEntry, List<OperatorList> operatorListListModel) {

        this.context=viewOperatorEntry;
        this.operatorLists=operatorListListModel;
        inflater = LayoutInflater.from(context);
    }

    public void filterList(List<OperatorList> operatorListListModel) {

        this.operatorLists = operatorListListModel;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_operator_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final OperatorList current=operatorLists.get(position);
        int sno=position+1;
        myHolder.sno_tv.setText(String.valueOf(sno));
        myHolder.machine_tv.setText(current.operator_name);
        myHolder.operatorid_tv.setText(current.operator_id);
        myHolder.dept_tv.setText(current.operator_dept);

        //set default Loader:
        loader_dialog = new Dialog(context);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);


        lato = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");

        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");


        myHolder.del_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.Operations.isOnline(context)) {
                    id=current.operator_id;

                    new DeleteStaff_Task().execute();
                }
                else
                {
                    Toast.makeText(context,"No Internet Connectivity",Toast.LENGTH_LONG).show();
                }

            }
        });

    }




    @Override
    public int getItemCount() {
        return operatorLists.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView sno_tv,machine_tv,del_tv,operatorid_tv,dept_tv;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");
            sno_tv = (TextView) itemView.findViewById(R.id.sno_tv);
            machine_tv = (TextView) itemView.findViewById(R.id.machine_tv);
            del_tv=itemView.findViewById(R.id.del_tv);
            operatorid_tv=itemView.findViewById(R.id.operatorid_tv);
            dept_tv=itemView.findViewById(R.id.dept_tv);

            sno_tv.setTypeface(tf);
            machine_tv.setTypeface(tf);
            del_tv.setTypeface(tf);
            operatorid_tv.setTypeface(tf);
            dept_tv.setTypeface(tf);
        }
    }




    public class DeleteStaff_Task extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("operatorid", id);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_DELETE_OPERATOR, json);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.hide();

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");


                if (status.equals("true")) {

                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(context, ActivityNew.class);
                                    context.startActivity(i);
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
