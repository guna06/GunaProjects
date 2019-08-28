package com.sqindia.srinarapp.SuperAdmin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.srinarapp.Adapter.CustomAdapter;
import com.sqindia.srinarapp.Adapter.CustomAdapterArrayList;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NewStatusReportActivity extends Activity {

    private RadioGroup radioGroupPeriod;
    private RadioButton radioPeriodButton;
    TextView second_tv;
    Button get_report_btn;
    String str_subpart,str_period_data,session_token,str_dept,str_empid,str_name,str_role,str_part,str_reportby,str_year;
    MaterialBetterSpinner year_spn;
    Typeface segoeui;
    LinearLayout second_lv,back;
    Dialog loader_dialog;
    SharedPreferences.Editor editor;
    ArrayList<String> main_part_arraylist=new ArrayList<>();
    ArrayList<String> subpart_arraylist=new ArrayList<>();
    AutoCompleteTextView ac_mainpart;
    CustomAdapterArrayList subpartAdapter;

    String[] year_data = {"2018","2019","2020"};
    String[] month_data = {"January", "February", "March","April","May","June","July","August","September","October","November","December"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_status_report);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(NewStatusReportActivity.this, v1);


        radioGroupPeriod = (RadioGroup) findViewById(R.id.radioSex);
        get_report_btn = (Button) findViewById(R.id.get_report_btn);
        second_lv=findViewById(R.id.second_lv);
        second_tv=findViewById(R.id.second_tv);
        year_spn=findViewById(R.id.year_spn);
        ac_mainpart=findViewById(R.id.ac_mainpart);
        second_tv.setVisibility(View.GONE);
        year_spn.setVisibility(View.GONE);
        second_lv.setVisibility(View.VISIBLE);
        ac_mainpart.setVisibility(View.GONE);

        back=findViewById(R.id.back);
        ac_mainpart.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        //set default Loader:
        loader_dialog = new Dialog(NewStatusReportActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");


        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        str_dept=sharedPreferences.getString("userpermission","");
        str_empid=sharedPreferences.getString("emp_id","");
        str_name=sharedPreferences.getString("emp_name","");
        str_role=sharedPreferences.getString("str_role","");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(back);
                finish();
            }
        });



        radioGroupPeriod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                int selectedId = radioGroupPeriod.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioPeriodButton = (RadioButton) findViewById(selectedId);

                Toast.makeText(NewStatusReportActivity.this,
                        radioPeriodButton.getText(), Toast.LENGTH_SHORT).show();

                str_period_data= radioPeriodButton.getText().toString();

                if(str_period_data.equals("Year Wise"))
                {
                    second_tv.setVisibility(View.VISIBLE);
                    second_tv.setText("Select Year");
                   second_lv.setVisibility(View.VISIBLE);
                    year_spn.setVisibility(View.VISIBLE);
                    ac_mainpart.setVisibility(View.GONE);
                    year_spn.setHint("Select Year");
                    // Shift Spinner Adapter:
                    final CustomAdapter shiftAdapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, year_data) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return true;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            tv.setTypeface(segoeui);
                            tv.setTextSize(9);
                            tv.setPadding(30, 55, 10, 25);
                            if (position == 0) {
                                tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }


                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            tv.setTextSize(15);
                            tv.setPadding(10, 20, 0, 20);
                            tv.setTypeface(segoeui);
                            if (position == 0) {
                                tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    year_spn.setAdapter(shiftAdapter);

                    //get All Machines Name Async:
                    if (Util.Operations.isOnline(NewStatusReportActivity.this)) {


                        //get All Main Parts:
                        new getMainPartsAsync().execute();


                    }else
                    {
                        new SweetAlertDialog(NewStatusReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("No Internet Connectivity")
                                .show();
                    }

                }
                else if(str_period_data.equals("Month Wise"))
                {

                    second_tv.setVisibility(View.VISIBLE);
                    second_lv.setVisibility(View.VISIBLE);
                    second_tv.setText("Select Month");
                    year_spn.setVisibility(View.VISIBLE);
                    ac_mainpart.setVisibility(View.GONE);
                    year_spn.setHint("Select Month");
                    final CustomAdapter shiftAdapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, month_data) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return true;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            tv.setTypeface(segoeui);
                            tv.setTextSize(9);
                            tv.setPadding(30, 55, 10, 25);
                            if (position == 0) {
                                tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }


                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            tv.setTextSize(15);
                            tv.setPadding(10, 20, 0, 20);
                            tv.setTypeface(segoeui);
                            if (position == 0) {
                                tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    year_spn.setAdapter(shiftAdapter);
                    //get All Machines Name Async:
                    if (Util.Operations.isOnline(NewStatusReportActivity.this)) {


                        //get All Main Parts:
                        new getMainPartsAsync().execute();


                    }else
                    {
                        new SweetAlertDialog(NewStatusReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("No Internet Connectivity")
                                .show();
                    }

                }
                else if(str_period_data.equals("Current Month"))
                {
                    str_reportby="thismonth";
                    second_tv.setVisibility(View.GONE);
                    year_spn.setVisibility(View.GONE);
                    ac_mainpart.setVisibility(View.GONE);
                    second_lv.setVisibility(View.INVISIBLE);
                    ac_mainpart.setVisibility(View.VISIBLE);
                    //get All Machines Name Async:
                    if (Util.Operations.isOnline(NewStatusReportActivity.this)) {


                        //get All Main Parts:
                        new getMainPartsAsync().execute();


                    }else
                    {
                        new SweetAlertDialog(NewStatusReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("No Internet Connectivity")
                                .show();
                    }

                }
                else if(str_period_data.equals("This Week"))
                {
                    str_reportby="thisweek";
                    second_tv.setVisibility(View.GONE);
                    year_spn.setVisibility(View.GONE);
                    second_lv.setVisibility(View.INVISIBLE);
                    ac_mainpart.setVisibility(View.VISIBLE);
                    //get All Machines Name Async:
                    if (Util.Operations.isOnline(NewStatusReportActivity.this)) {


                        //get All Main Parts:
                        new getMainPartsAsync().execute();


                    }else
                    {
                        new SweetAlertDialog(NewStatusReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("No Internet Connectivity")
                                .show();
                    }

                }
                else if(str_period_data.equals("Today"))
                {
                    str_reportby="today";
                    second_tv.setVisibility(View.GONE);
                    year_spn.setVisibility(View.GONE);
                    second_lv.setVisibility(View.INVISIBLE);
                    ac_mainpart.setVisibility(View.VISIBLE);
                    //get All Machines Name Async:
                    if (Util.Operations.isOnline(NewStatusReportActivity.this)) {

                            //get All Main Parts:
                            new getMainPartsAsync().execute();
                    }else
                    {
                        new SweetAlertDialog(NewStatusReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("No Internet Connectivity")
                                .show();
                    }

                }

            }
        });


        year_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(NewStatusReportActivity.this, view);
                str_year = adapterView.getItemAtPosition(i).toString();
                ac_mainpart.setVisibility(View.VISIBLE);
            }
        });



        //Creating the instance of ArrayAdapter containing list of machine names
        ArrayAdapter<String> adapter_mainpart = new ArrayAdapter<String>
                (this, R.layout.autocomplete_dialog,R.id.auto_item, main_part_arraylist);
        ac_mainpart.setThreshold(1);//will start working from first character
        ac_mainpart.setAdapter(adapter_mainpart);//setting the adapter data into the AutoCompleteTextView
        ac_mainpart.setTextColor(Color.parseColor("#3c3c3c"));



        ac_mainpart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(adapterView.getWindowToken(), 0);
                str_part = adapterView.getItemAtPosition(i).toString();
                //get All Sub Parts:
               // new getSubPartsAsync().execute();
            }
        });




        get_report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(str_period_data!=null)
                    {
                        if(str_period_data.equals("Year Wise"))
                        {
                            if(str_year!=null)
                            {
                                if(str_part!=null)
                                {
                                        Intent g=new Intent(getApplicationContext(),ViewManualReport.class);
                                        g.putExtra("pass_type","year");
                                        //g.putExtra("pass_subpart",str_subpart);
                                        g.putExtra("pass_reportby",str_reportby);
                                        g.putExtra("pass_mainpart",str_part);
                                        g.putExtra("pass_year",str_year);
                                        startActivity(g);
                                        finish();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Please Select Main Part",Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please Select Year",Toast.LENGTH_LONG).show();
                            }

                        }
                        else if(str_period_data.equals("Month Wise")){

                            if(str_year!=null)
                            {

                                    if(str_part!=null)
                                    {
                                        Intent g=new Intent(getApplicationContext(),ViewManualReport.class);
                                        g.putExtra("pass_type","month");
                                        //g.putExtra("pass_subpart",str_subpart);
                                        g.putExtra("pass_reportby",str_reportby);
                                        g.putExtra("pass_mainpart",str_part);
                                        g.putExtra("pass_year",str_year);
                                        startActivity(g);
                                        finish();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Please Select Main Part",Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please Select Year",Toast.LENGTH_LONG).show();
                            }
                        }
                        else if(str_period_data.equals("Current Month")){

                            if(str_part!=null)
                            {
                                Intent g=new Intent(getApplicationContext(),ViewManualReport.class);
                                g.putExtra("pass_type","month");
                                g.putExtra("pass_reportby",str_reportby);
                                g.putExtra("pass_mainpart",str_part);
                                g.putExtra("pass_year",str_year);
                                startActivity(g);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please Select Main Part",Toast.LENGTH_LONG).show();
                            }
                        }
                        else if(str_period_data.equals("This Week")){
                            if(str_part!=null)
                            {

                                    Intent g=new Intent(getApplicationContext(),StatusReportActivity.class);
                                    g.putExtra("pass_reportby",str_reportby);
                                    g.putExtra("pass_mainpart",str_part);
                                    startActivity(g);
                                    finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please Select Main Part",Toast.LENGTH_LONG).show();
                            }

                        }
                        else if(str_period_data.equals("Today")){

                            if(str_part!=null)
                            {

                                    Intent g=new Intent(getApplicationContext(),StatusReportActivity.class);
                                    g.putExtra("pass_reportby",str_reportby);
                                    g.putExtra("pass_mainpart",str_part);
                                    startActivity(g);
                                    finish();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please Select Main Part",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please Select Period",Toast.LENGTH_LONG).show();
                    }




            }
        });



        }



    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL MAIN PART API
    @SuppressLint("StaticFieldLeak")
    public class getMainPartsAsync extends AsyncTask<String, Void, String> {

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
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_MAIN_PART, session_token);

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
                    main_part_arraylist.clear();
                    JSONArray jsonArray=jo.getJSONArray("mainparts");
                    for(int l=0;l<jsonArray.length();l++)
                    {
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        String mainpart_name=jsonObject.getString("mainpart");
                        main_part_arraylist.add(mainpart_name);
                    }

                } else {
                    String msg = jo.getString("message");
                    new SweetAlertDialog(NewStatusReportActivity.this, SweetAlertDialog.WARNING_TYPE)
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



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(back);
        finish();
    }





}