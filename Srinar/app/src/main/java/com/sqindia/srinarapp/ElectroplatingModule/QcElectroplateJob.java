package com.sqindia.srinarapp.ElectroplatingModule;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.sqindia.srinarapp.AssemblingModule.AssemblingEntryActivity;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.MachiningModule.MachiningEntryActivity;
import com.sqindia.srinarapp.MachiningModule.QcMachineJob;
import com.sqindia.srinarapp.MachiningModule.ViewMachineJob;
import com.sqindia.srinarapp.Model.ElectroplateJobList;

import com.sqindia.srinarapp.Model.QcPlatePojo;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.SuperAdmin.DashboardActivity;
import com.sqindia.srinarapp.SuperAdmin.QADirection;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class QcElectroplateJob extends Activity {

    LinearLayout back,refresh;
    String session_token,str_part,navdashboard,selected_part,selected_month,selected_year;
    SharedPreferences.Editor editor;
    QcPlatePojo qcPlatePojo;
    private List<QcPlatePojo> electroplateJobLists;
    PlateQcJobListAdapter qcJobListAdapter;
    RecyclerView view_mac_recycler;
    Dialog loader_dialog;
    AutoCompleteTextView ac_part;
    final ArrayList<String> main_part_arraylist=new ArrayList<>();
    Bundle extras;
    Map<String, String> subpart_hash_part = new HashMap<>();
    Map<String, String> getyear = new HashMap<>();
    Map<String, String> getmonth = new HashMap<>();
    LinearLayout empty_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qc_electroplate_work);
        extras = getIntent().getExtras();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(QcElectroplateJob.this, v1);


        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        electroplateJobLists=new ArrayList<>();

        //************* Add Cast:
        refresh=findViewById(R.id.refresh);
        back=findViewById(R.id.back);
        view_mac_recycler=findViewById(R.id.view_mac_recycler);
        empty_layout=findViewById(R.id.empty_layout);
        //set default Loader:
        loader_dialog = new Dialog(QcElectroplateJob.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        ac_part=findViewById(R.id.ac_part);
        ac_part.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        empty_layout.setVisibility(View.VISIBLE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),QcElectroplateJob.class);
                startActivity(back);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (extras != null) {
                    navdashboard = extras.getString("navdashboard");
                    if (navdashboard.equals("true")) {
                        Intent back=new Intent(getApplicationContext(),QADirection.class);
                        startActivity(back);
                        finish();
                    }
                } else {

                    Intent back=new Intent(getApplicationContext(),ElectroplateEntryActivity.class);
                    startActivity(back);
                    finish();
                }


            }
        });




        if (Util.Operations.isOnline(getApplicationContext())) {
            new getMainPartsAsync().execute();
           // new viewMachineJobAsync().execute();
        }
        else
        {
            new SweetAlertDialog(QcElectroplateJob.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No Internet Connectivity")
                    .show();
        }



        ArrayAdapter<String> adapter_machine = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, main_part_arraylist);

        ac_part.setThreshold(1);//will start working from first character
        ac_part.setAdapter(adapter_machine);//setting the adapter data into the AutoCompleteTextView
        ac_part.setTextColor(Color.parseColor("#3c3c3c"));


        ac_part.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getWindowToken(), 0);
                str_part = arg0.getItemAtPosition(arg2).toString();
                selected_part=subpart_hash_part.get(str_part);
                selected_month=getmonth.get(str_part);
                selected_year=getyear.get(str_part);
                //get All Sub Parts:
                new groupingSubpartsByMain().execute();
            }

        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (extras != null) {
            navdashboard = extras.getString("navdashboard");
            if (navdashboard.equals("true")) {
                Intent back=new Intent(getApplicationContext(),QADirection.class);
                startActivity(back);
                finish();
            }
        } else {

            Intent back=new Intent(getApplicationContext(),ElectroplateEntryActivity.class);
            startActivity(back);
            finish();
        }
    }


    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL MAIN PART API
    public class getMainPartsAsync extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog_mainpart;

        protected void onPreExecute() {
            super.onPreExecute();

            dialog_mainpart = new ProgressDialog(QcElectroplateJob.this);
            dialog_mainpart.setMessage("Loading Main Parts..., please wait.");
            dialog_mainpart.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_OC_MAINPART, session_token);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog_mainpart.isShowing()) {
                dialog_mainpart.dismiss();
            }
            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    main_part_arraylist.clear();

                    JSONArray jsonArray=jo.getJSONArray("mainparts");

                    for(int l=0;l<jsonArray.length();l++)
                    {
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        String mainpart_name=jsonObject.getString("part");

                        main_part_arraylist.add(mainpart_name);

                        subpart_hash_part.put(jsonObject.getString("part"),jsonObject.getString("mainpart"));
                        getmonth.put(jsonObject.getString("part"),jsonObject.getString("partmonth"));
                        getyear.put(jsonObject.getString("part"),jsonObject.getString("partyear"));
                    }


                } else {
                    new SweetAlertDialog(QcElectroplateJob.this, SweetAlertDialog.WARNING_TYPE)
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


  /*  //@@@@@@@@@@@@@@@@@@@@@@@@ CALL VIEW MACHINE API
    public class viewMachineJobAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_GET_ELECTROPLATE_WORK, session_token);

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
                Log.e("tag","Status"+status);

                if (status.equals("true")) {
                    JSONArray jsonArray=jo.getJSONArray("electroplatingworks");
                    electroplateJobLists.clear();
                    for(int l=0;l<jsonArray.length();l++)
                    {
                        electroplateJobList = new ElectroplateJobList();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        electroplateJobList._id=jsonObject.getString("batchid");
                        electroplateJobList.ele_shift=jsonObject.getString("shift");
                        electroplateJobList.ele_operator=jsonObject.getString("operatorname");
                        electroplateJobList.ele_machine=jsonObject.getString("machine_line");
                        electroplateJobList.ele_worktype=jsonObject.getString("worktype");
                        electroplateJobList.ele_part=jsonObject.getString("mainpart");
                        electroplateJobList.ele_subpart=jsonObject.getString("subpart");
                        electroplateJobList.ele_quantitycompleted=jsonObject.getString("qtycompleted");
                        electroplateJobList.ele_approved=jsonObject.getString("qtyapproved");
                        electroplateJobList.ele_rejected=jsonObject.getString("qtyrejected");
                        electroplateJobList.ele_date=jsonObject.getString("wcentrycreatedat");
                        electroplateJobLists.add(electroplateJobList);
                    }


                    // Setup and Handover data to recyclerview
                    qcJobListAdapter = new PlateQcJobListAdapter(QcElectroplateJob.this, electroplateJobLists);
                    view_mac_recycler.setAdapter(qcJobListAdapter);
                    qcJobListAdapter.notifyDataSetChanged();
                    view_mac_recycler.setLayoutManager(new LinearLayoutManager(QcElectroplateJob.this));
                    loader_dialog.dismiss();
                } else {
                    loader_dialog.dismiss();
                    String message = jo.getString("message");
                    new SweetAlertDialog(QcElectroplateJob.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(message)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    if (extras != null) {
                                        navdashboard = extras.getString("navdashboard");
                                        if (navdashboard.equals("true")) {
                                            Intent back=new Intent(getApplicationContext(),QADirection.class);
                                            startActivity(back);
                                            finish();
                                        }
                                    } else {

                                        Intent back=new Intent(getApplicationContext(),ElectroplateEntryActivity.class);
                                        startActivity(back);
                                        finish();
                                    }
                                }
                            })
                            .show();
                }
            } catch (Exception e) {
            }
        }
    }*/

    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL VIEW MACHINE API
    public class groupingSubpartsByMain extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("mainpart", selected_part);
                jsonObject.accumulate("worktype", "electroplating");
                jsonObject.accumulate("partmonth", selected_month);
                jsonObject.accumulate("partyear", selected_year);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GET_SUBPARTS_BY_MAIN_PARTS_NEW, json, session_token);
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
                Log.e("tag", "Status" + status);

                if (status.equals("true")) {
                    electroplateJobLists.clear();
                    empty_layout.setVisibility(View.GONE);
                    JSONArray jsonArray = jo.getJSONArray("subparts");


                    for (int l = 0; l < jsonArray.length(); l++) {
                        qcPlatePojo = new QcPlatePojo();
                        JSONObject jsonObject = jsonArray.getJSONObject(l);
                        qcPlatePojo.QMP_part_id=jsonObject.getString("partid");
                        qcPlatePojo.QMP_mainpart=jsonObject.getString("mainpart");
                        qcPlatePojo.QMP_submainpart=jsonObject.getString("subpart");
                        qcPlatePojo.QMP_desc=jsonObject.getString("partdescription");
                        qcPlatePojo.QMP_month=jsonObject.getString("partmonth");
                        qcPlatePojo.QMP_initial=jsonObject.getString("initialqty");
                        qcPlatePojo.QMP_completed=jsonObject.getString("electroplatedcompletedqty");
                        qcPlatePojo.QMP_approved=jsonObject.getString("electroplatedapprovedqty");
                        electroplateJobLists.add(qcPlatePojo);
                    }


                    // Setup and Handover data to recyclerview
                    qcJobListAdapter = new PlateQcJobListAdapter(QcElectroplateJob.this, electroplateJobLists);
                    view_mac_recycler.setAdapter(qcJobListAdapter);
                    qcJobListAdapter.notifyDataSetChanged();
                    view_mac_recycler.setLayoutManager(new LinearLayoutManager(QcElectroplateJob.this));

                } else {
                    String message = jo.getString("message");
                    new SweetAlertDialog(QcElectroplateJob.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(message)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    if (extras != null) {
                                        navdashboard = extras.getString("navdashboard");
                                        if (navdashboard.equals("true")) {
                                            Intent back=new Intent(getApplicationContext(),QADirection.class);
                                            startActivity(back);
                                            finish();
                                        }
                                    } else {

                                        Intent back=new Intent(getApplicationContext(),QcElectroplateJob.class);
                                        startActivity(back);
                                        finish();
                                    }
                                }
                            })
                            .show();
                }
            } catch (Exception e) {
            }
        }
    }
}
