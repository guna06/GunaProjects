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
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.Model.AssemblingJobList;
import com.sqindia.srinarapp.Model.QcAssemblingPojo;
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
public class QcAssemblingJob extends Activity {
    LinearLayout back,refresh;
    String session_token,str_dept,str_name,str_role,str_part,navdashboard,selected_part,selected_month,selected_year;
    QcAssemblingPojo assemblingJobList;
    private List<QcAssemblingPojo> assemblingJobLists;
    QcAssembleAdapter assembleQcAdapter;
    RecyclerView view_mac_recycler;
    Typeface segoeui;
    Dialog loader_dialog;
    AutoCompleteTextView ac_part;
    final ArrayList<String> main_part_arraylist=new ArrayList<>();
    Map<String, String> subpart_hash_part = new HashMap<>();
    Bundle extras;
    Map<String, String> getyear = new HashMap<>();
    Map<String, String> getmonth = new HashMap<>();
    LinearLayout empty_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assembling_qc_work);

        hideSoftKeyboard();
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(QcAssemblingJob.this, v1);

        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");
        extras = getIntent().getExtras();

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        str_dept=sharedPreferences.getString("userpermission","");
        str_name=sharedPreferences.getString("emp_name","");
        str_role=sharedPreferences.getString("str_role","");


        //set default Loader:
        loader_dialog = new Dialog(QcAssemblingJob.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        empty_layout=findViewById(R.id.empty_layout);
        assemblingJobLists=new ArrayList<>();
        ac_part=findViewById(R.id.ac_part);
        ac_part.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        //************* Add Cast:
        back=findViewById(R.id.back);
        refresh=findViewById(R.id.refresh);
        view_mac_recycler=findViewById(R.id.view_mac_recycler);
        empty_layout.setVisibility(View.VISIBLE);
        new getMainPartsAsync().execute();

        if (Util.Operations.isOnline(getApplicationContext())) {
            //new viewMachineJobAsync().execute();

        }
        else
        {
            new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No Internet Connectivity")
                    .show();
        }


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),QcAssemblingJob.class);
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

                    Intent back=new Intent(getApplicationContext(),AssemblingEntryActivity.class);
                    startActivity(back);
                    finish();
                }

            }
        });



        //Creating the instance of ArrayAdapter containing list of machine names
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

            Intent back=new Intent(getApplicationContext(),AssemblingEntryActivity.class);
            startActivity(back);
            finish();
        }
    }




    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL MAIN PART API
    public class getMainPartsAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

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
                    new SweetAlertDialog(QcAssemblingJob.this, SweetAlertDialog.WARNING_TYPE)
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
                jsonObject.accumulate("worktype", "assembling");
                jsonObject.accumulate("partmonth", selected_month);
                jsonObject.accumulate("partyear", selected_year);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GET_SUBPARTS_BY_MAIN_PARTS_NEW,json, session_token);
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

                if (status.equals("true")) {
                    JSONArray jsonArray=jo.getJSONArray("subparts");
                    assemblingJobLists.clear();
                    empty_layout.setVisibility(View.GONE);
                    for(int l=0;l<jsonArray.length();l++)
                    {
                        assemblingJobList = new QcAssemblingPojo();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        assemblingJobList.QMP_part_id=jsonObject.getString("partid");
                        assemblingJobList.QMP_mainpart=jsonObject.getString("mainpart");
                        assemblingJobList.QMP_submainpart=jsonObject.getString("subpart");
                        assemblingJobList.QMP_desc=jsonObject.getString("partdescription");
                        assemblingJobList.QMP_month=jsonObject.getString("partmonth");
                        assemblingJobList.QMP_initial=jsonObject.getString("initialqty");
                        assemblingJobList.QMP_completed=jsonObject.getString("assembledcompletedqty");
                        assemblingJobList.QMP_approved=jsonObject.getString("assembledapprovedqty");
                        assemblingJobLists.add(assemblingJobList);
                    }


                    // Setup and Handover data to recyclerview
                    assembleQcAdapter = new QcAssembleAdapter(QcAssemblingJob.this, assemblingJobLists);
                    view_mac_recycler.setAdapter(assembleQcAdapter);
                    view_mac_recycler.setLayoutManager(new LinearLayoutManager(QcAssemblingJob.this));

                } else {
                    String message = jo.getString("message");
                    new SweetAlertDialog(QcAssemblingJob.this, SweetAlertDialog.WARNING_TYPE)
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

                                        Intent back=new Intent(getApplicationContext(),QcAssemblingJob.class);
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



    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
