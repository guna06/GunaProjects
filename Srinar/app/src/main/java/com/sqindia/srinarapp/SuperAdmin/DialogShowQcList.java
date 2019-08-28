package com.sqindia.srinarapp.SuperAdmin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.srinarapp.AssemblingModule.QcAssemblingJob;
import com.sqindia.srinarapp.ElectroplatingModule.ElectroplateEntryActivity;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.MachiningModule.MachineQcAdapter;
import com.sqindia.srinarapp.MachiningModule.MachiningEntryActivity;
import com.sqindia.srinarapp.MachiningModule.QcMachineJob;
import com.sqindia.srinarapp.Model.MachineJobList;
import com.sqindia.srinarapp.Model.QcApproverList;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class DialogShowQcList extends Dialog {

    Activity activity;
    Typeface segoeuil;
    String set_batchid, str_qty, token,str_qc_type;
    Dialog loader_dialog;
    QcApproverList qcApproverList;
    private List<QcApproverList> qcApproverLists;
    List<QcApproverList> QcApproval;
    ShowQcAdapter showQcAdapter;
    RecyclerView recyclerView;
    TextView head,date_tv,qty_tv,name_tv,msg_tv;

    public DialogShowQcList(Activity act) {
        super(act);
        this.activity = act;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_show_qc);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        segoeuil = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoeuil.ttf");



        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        token = sharedPreferences.getString("str_sessiontoken", "");
        set_batchid = sharedPreferences.getString("qc_partid", "");
        str_qc_type= sharedPreferences.getString("qc_type", "");


        QcApproval=new ArrayList<>();

        //set default Loader:
        loader_dialog = new Dialog(getContext());
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        recyclerView=findViewById(R.id.recyclerView);

        head=findViewById(R.id.head);
        date_tv=findViewById(R.id.date_tv);
        qty_tv=findViewById(R.id.qty_tv);
        name_tv=findViewById(R.id.name_tv);
        msg_tv=findViewById(R.id.msg_tv);


        head.setTypeface(segoeuil);
        date_tv.setTypeface(segoeuil);
        qty_tv.setTypeface(segoeuil);
        name_tv.setTypeface(segoeuil);
        msg_tv.setTypeface(segoeuil);


        msg_tv.setVisibility(View.GONE);

        if (Util.Operations.isOnline(getContext())) {
            new show_qclogs().execute();

        }else
        {
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No Internet Connectivity")
                    .show();
        }
    }


    public class show_qclogs extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("partid", set_batchid);
                jsonObject.accumulate("qctype",str_qc_type);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GET_QC_LIST, json,token);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();
            msg_tv.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    JSONArray jsonArray=jo.getJSONArray("qclogs");

                    for(int l=0;l<jsonArray.length();l++)
                    {
                        qcApproverList = new QcApproverList();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        qcApproverList.qty_approved=jsonObject.getString("qty_approved");
                        qcApproverList.qc_date=jsonObject.getString("qc_date");
                        qcApproverList.qc_by=jsonObject.getString("qc_by");
                        QcApproval.add(qcApproverList);
                    }


                    // Setup and Handover data to recyclerview
                    showQcAdapter = new ShowQcAdapter(DialogShowQcList.this, QcApproval);
                    recyclerView.setAdapter(showQcAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                } else {

                    String msg = jo.getString("message");
                    msg_tv.setVisibility(View.VISIBLE);
                    msg_tv.setText(msg);
                    Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
    }





    }








