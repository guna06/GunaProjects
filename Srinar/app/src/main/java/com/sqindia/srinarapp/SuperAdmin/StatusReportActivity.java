package com.sqindia.srinarapp.SuperAdmin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.srinarapp.Adapter.CustomAdapter;
import com.sqindia.srinarapp.Adapter.CustomAdapterArrayList;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.Model.ManualReportList;

import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StatusReportActivity extends Activity {
    MaterialBetterSpinner time_spn,part_spn,subpart_spn;
    String str_time,session_token,initialqty;

    SharedPreferences.Editor editor;
    Typeface segoeui;
    ArrayList<String> main_part_arraylist=new ArrayList<>();
    ArrayList<String> part_arraylist=new ArrayList<>();
    String[] time_data = {"Today", "This Week", "This Month"};
    CustomAdapterArrayList mainpartAdapter,subpartAdapter;
    Map<String, String> subpart_hash_id = new HashMap<>();
    TextView initialqty_tv,viewreport_tv,dispatched_tv,status_tv;
    LinearLayout back,chumma;
    ProgressDialog progressDoalog;
    Dialog loader_dialog;
    ManualReportList manualReportList;
    private List<ManualReportList> manualReportLists;
    ManualListAdapter manualListAdapter;
    RecyclerView view_mac_recycler;
    TextView mac_cmp_tv,ele_cmp_tv,ass_cmp_tv,mac_pen_tv,ele_pen_tv,ass_pen_tv,mac_app_tv,ele_app_tv,ass_app_tv,show_subpart,show_mainpart,headstatus_tv;
    String mac_completed,mac_approved,ele_completed,ele_approved,ass_completed,ass_approved,get_subpart,get_reportby,get_mainpart;
    Intent extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_report);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(StatusReportActivity.this, v1);

        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");
        extras = getIntent();
        initialqty_tv=findViewById(R.id.initialqty_tv);
        back=findViewById(R.id.back);

        status_tv=findViewById(R.id.status_tv);
        mac_cmp_tv=findViewById(R.id.mac_cmp_tv);
        ele_cmp_tv=findViewById(R.id.ele_cmp_tv);
        ass_cmp_tv=findViewById(R.id.ass_cmp_tv);
        mac_pen_tv=findViewById(R.id.mac_pen_tv);
        ele_pen_tv=findViewById(R.id.ele_pen_tv);
        ass_pen_tv=findViewById(R.id.ass_pen_tv);
        dispatched_tv=findViewById(R.id.dispatched_tv);
        headstatus_tv=findViewById(R.id.headstatus_tv);

        show_mainpart=findViewById(R.id.show_mainpart);
        //show_subpart=findViewById(R.id.show_subpart);

        mac_app_tv=findViewById(R.id.mac_app_tv);
        ele_app_tv=findViewById(R.id.ele_app_tv);
        ass_app_tv=findViewById(R.id.ass_app_tv);

        get_subpart = extras.getStringExtra("pass_subpart");
        get_reportby = extras.getStringExtra("pass_reportby");
        get_mainpart= extras.getStringExtra("pass_mainpart");

        show_mainpart.setText(get_mainpart);


        if(get_reportby.equals("thismonth"))
        {
            headstatus_tv.setText("This Month Report:");
        }


        else if(get_reportby.equals("thisweek"))
        {
            headstatus_tv.setText("This Week Report:");
        }

        else if(get_reportby.equals("today"))
        {
            headstatus_tv.setText("Today Report:");
        }

//token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");


        //set default Loader:
        loader_dialog = new Dialog(StatusReportActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        loader_dialog.show();
        manualReportLists = new ArrayList<>();

        new statusReportAsync().execute();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(), NewStatusReportActivity.class);
                startActivity(back);
                finish();
            }
        });

    }

   //@@@@@@@@@@@@@@@@@@@@@@@@ CALL STATUS REPORT
    public class statusReportAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("mainpart", get_mainpart);
                jsonObject.accumulate("reportby",get_reportby);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_STATUS_REPORT,json, session_token);

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


                    for(int l=0;l<jsonArray.length();l++)
                    {
                        JSONObject jsonObject =jsonArray.getJSONObject(l);

                     /*   initialqty=jsonObject.getString("initialqty");
                        ass_approved=jsonObject.getString("assemblingapproved");*/
                        initialqty_tv.setText(jsonObject.getString("initialqty"));
                        mac_cmp_tv.setText(jsonObject.getString("machiningcompleted"));
                        ele_cmp_tv.setText(jsonObject.getString("platingcompleted"));
                        ass_cmp_tv.setText(jsonObject.getString("assemblingcompleted"));
                        mac_pen_tv.setText(jsonObject.getString("machiningapproved"));
                        ele_pen_tv.setText(jsonObject.getString("platingapproved"));
                        ass_pen_tv.setText(jsonObject.getString("assemblingapproved"));


                        dispatched_tv.setText("Dispatched Qty : "+jsonObject.getString("dispatched_qty"));

                        int mc,ec,ac,ma,ea,aa,mp,ep,ap;
                        mc=Integer.parseInt(mac_cmp_tv.getText().toString());
                        ma=Integer.parseInt(mac_pen_tv.getText().toString());
                        mp=mc-ma;
                        mac_app_tv.setText(String.valueOf(mp));

                        ec=Integer.parseInt(ele_cmp_tv.getText().toString());
                        ea=Integer.parseInt(ass_cmp_tv.getText().toString());
                        ep=ec-ea;
                        ele_app_tv.setText(String.valueOf(ep));


                        ac=Integer.parseInt(ass_cmp_tv.getText().toString());
                        aa=Integer.parseInt(ass_pen_tv.getText().toString());
                        ap=ac-aa;
                        ass_app_tv.setText(String.valueOf(ap));


                        if(initialqty.equals(ass_approved))
                        {
                            status_tv.setText("Target Completed");
                        }
                        else
                        {
                            status_tv.setText("Pending");
                        }

                    }

                } else {
                    String msg = jo.getString("message");
                    new SweetAlertDialog(StatusReportActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    Intent w=new Intent(getApplicationContext(),NewStatusReportActivity.class);
                                    startActivity(w);
                                    finish();

                                }
                            })
                            .show();
                }
            } catch (Exception e) {

            }


        }

    }

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDoalog.incrementProgressBy(1);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(), NewStatusReportActivity.class);
        startActivity(back);
        finish();
    }
}
