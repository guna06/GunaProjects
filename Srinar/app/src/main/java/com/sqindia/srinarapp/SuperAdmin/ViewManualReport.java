package com.sqindia.srinarapp.SuperAdmin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.Model.ManualReportList;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ViewManualReport extends Activity {

    LinearLayout back;
    String session_token,str_manualpart,str_manualsubpart,get_subpart,get_year,get_part,get_type;
    SharedPreferences.Editor editor;
    ManualReportList manualReportList;
    private List<ManualReportList> manualReportLists;
    ManualListAdapter manualListAdapter;
    RecyclerView view_mac_recycler;
    Dialog loader_dialog;
    Intent extras;
    TextView year_tv,show_mainpart,show_subpart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_manual_report);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewManualReport.this, v1);
        extras = getIntent();

        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        get_type= extras.getStringExtra("pass_type");
        get_part= extras.getStringExtra("pass_mainpart");
        get_subpart = extras.getStringExtra("pass_subpart");
        get_year = extras.getStringExtra("pass_year");
        //set default Loader:
        loader_dialog = new Dialog(ViewManualReport.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);


        manualReportLists=new ArrayList<>();

        //************* Add Cast:
        back=findViewById(R.id.back);
        view_mac_recycler=findViewById(R.id.view_mac_recycler);
        year_tv=findViewById(R.id.year_tv);
        show_mainpart=findViewById(R.id.show_mainpart);



        show_mainpart.setText(get_part);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),NewStatusReportActivity.class);
                startActivity(back);
                finish();
            }
        });

        if (Util.Operations.isOnline(ViewManualReport.this)) {

            if(get_type.equals("year"))
            {
                new viewYearReportAsync().execute();
            }
            else if(get_type.equals("month"))
            {
                new viewMonthReportAsync().execute();
            }


        }
        else
        {
            new SweetAlertDialog(ViewManualReport.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No Internet Connectivity")
                    .show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(),NewStatusReportActivity.class);
        startActivity(back);
        finish();
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL VIEW MANUAL REPORT API
    public class viewYearReportAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("mainpart",get_part);
                jsonObject.accumulate("year",get_year);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_MANUAL_REPORT,json, session_token);

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


                    JSONArray jsonArray=jo.getJSONArray("response");
                    manualReportLists.clear();
                    for(int l=0;l<jsonArray.length();l++)
                    {

                        manualReportList = new ManualReportList();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        manualReportList.production_month=jsonObject.getString("productionmonth");
                        manualReportList.part_year=jsonObject.getString("partyear");
                        manualReportList.part_month=jsonObject.getString("partmonth");
                        manualReportList.k_subpart=jsonObject.getString("subpart");
                        manualReportList.initial_qty=jsonObject.getString("initialqty");
                        manualReportList.m_completed=jsonObject.getString("machiningcompleted");
                       // manualReportList.m_approved=jsonObject.getString("machiningapproved");
                        manualReportList.p_completed=jsonObject.getString("platingcompleted");
                        //manualReportList.p_approved=jsonObject.getString("platingapproved");
                        manualReportList.a_completed=jsonObject.getString("assemblingcompleted");
                        //manualReportList.a_approved=jsonObject.getString("assemblingapproved");
                        manualReportList.a_dispatched=jsonObject.getString("dispatched_qty");
                        year_tv.setText("Dispatched Qty "+jsonObject.getString("dispatched_qty"));

                        manualReportLists.add(manualReportList);

                    }


                    manualListAdapter=new ManualListAdapter(ViewManualReport.this,manualReportLists);
                    view_mac_recycler.setAdapter(manualListAdapter);
                    manualListAdapter.notifyDataSetChanged();
                    view_mac_recycler.setLayoutManager(new LinearLayoutManager(ViewManualReport.this));

                } else {

                    String message = jo.getString("message");
                    new SweetAlertDialog(ViewManualReport.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(message)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    Intent back_mac=new Intent(getApplicationContext(),NewStatusReportActivity.class);
                                    startActivity(back_mac);
                                    finish();

                                }
                            })
                            .show();
                }
            } catch (Exception e) {

            }


        }

    }

    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL VIEW MANUAL REPORT API
    public class viewMonthReportAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("mainpart",get_part);
                jsonObject.accumulate("month",get_year);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_MONTH_REPORT,json, session_token);

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

                    JSONArray jsonArray=jo.getJSONArray("response");
                    manualReportLists.clear();
                    for(int l=0;l<jsonArray.length();l++)
                    {
                        manualReportList = new ManualReportList();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        manualReportList.production_month=jsonObject.getString("productionmonth");
                        manualReportList.part_month=jsonObject.getString("partmonth");

                        manualReportList.part_year=jsonObject.getString("partyear");
                        manualReportList.k_subpart=jsonObject.getString("subpart");
                        manualReportList.initial_qty=jsonObject.getString("initialqty");
                        manualReportList.m_completed=jsonObject.getString("machiningcompleted");
                        manualReportList.m_approved=jsonObject.getString("machiningapproved");
                        manualReportList.p_completed=jsonObject.getString("platingcompleted");
                        manualReportList.p_approved=jsonObject.getString("platingapproved");
                        manualReportList.a_completed=jsonObject.getString("assemblingcompleted");
                        manualReportList.a_approved=jsonObject.getString("assemblingapproved");
                        manualReportList.a_dispatched=jsonObject.getString("dispatched_qty");
                        year_tv.setText("Dispatched Qty "+jsonObject.getString("dispatched_qty"));
                        manualReportLists.add(manualReportList);

                    }


                    manualListAdapter=new ManualListAdapter(ViewManualReport.this,manualReportLists);
                    view_mac_recycler.setAdapter(manualListAdapter);
                    manualListAdapter.notifyDataSetChanged();
                    view_mac_recycler.setLayoutManager(new LinearLayoutManager(ViewManualReport.this));

                } else {

                    String message = jo.getString("message");
                    new SweetAlertDialog(ViewManualReport.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(message)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    Intent back_mac=new Intent(getApplicationContext(),NewStatusReportActivity.class);
                                    startActivity(back_mac);
                                    finish();

                                }
                            })
                            .show();
                }
            } catch (Exception e) {

            }


        }

    }

}
