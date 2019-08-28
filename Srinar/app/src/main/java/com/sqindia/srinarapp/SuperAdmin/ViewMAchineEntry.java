package com.sqindia.srinarapp.SuperAdmin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.sqindia.srinarapp.Adapter.MachineListAdapter;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.Model.MachineList;

import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ViewMAchineEntry extends Activity {

    LinearLayout back;
    String session_token;
    SharedPreferences.Editor editor;
    MachineList machineList;
    private List<MachineList> machineListListModel;
    MachineListAdapter machineListAdapter;
    RecyclerView view_mac_recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_machine);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewMAchineEntry.this, v1);


        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        machineListListModel=new ArrayList<>();

        //************* Add Cast:
        back=findViewById(R.id.back);
        view_mac_recycler=findViewById(R.id.recyclerView);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),AddMachine.class);
                startActivity(back);
                finish();
            }
        });


        if (Util.Operations.isOnline(ViewMAchineEntry.this)) {

            new viewMachineAsync().execute();
        }
        else
        {
            new SweetAlertDialog(ViewMAchineEntry.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No Internet Connectivity")
                    .show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent back=new Intent(getApplicationContext(),AddMachine.class);
        startActivity(back);
        finish();
    }


    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL VIEW MACHINE API
    public class viewMachineAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_VIEW_MACHINE, session_token);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");


                if (status.equals("true")) {
                    JSONArray jsonArray=jo.getJSONArray("machine");
                    for(int l=0;l<jsonArray.length();l++)
                    {
                        machineList = new MachineList();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        machineList._id=jsonObject.getString("machineid");
                        machineList.machine_name=jsonObject.getString("machinename");
                        machineList.machine_dept=jsonObject.getString("machinedept");


                        machineListListModel.add(machineList);
                    }


                  // Setup and Handover data to recyclerview
                    machineListAdapter = new MachineListAdapter(ViewMAchineEntry.this, machineListListModel);
                    view_mac_recycler.setAdapter(machineListAdapter);
                    machineListAdapter.notifyDataSetChanged();
                    view_mac_recycler.setLayoutManager(new LinearLayoutManager(ViewMAchineEntry.this));

                } else {
                    new SweetAlertDialog(ViewMAchineEntry.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText("Server Error")

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
