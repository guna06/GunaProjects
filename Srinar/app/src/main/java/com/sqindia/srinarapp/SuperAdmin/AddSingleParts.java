package com.sqindia.srinarapp.SuperAdmin;

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
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.srinarapp.Adapter.CustomAdapter;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.MachiningModule.MachiningEntryActivity;

import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddSingleParts extends Activity {
    MaterialBetterSpinner stock_spn,lineorder_spn,month_spn,year_spin;
    SharedPreferences.Editor editor;
    TextView head_titletv;
    LinearLayout back;
    Button add_part_machine;
    String[] stock_data = {"IN HOUSE","BOUGHT OUT"};
    String[] line_data = {"MEA","MA"};
    String[] month_data = {"January", "February", "March","April","May","June","July","August","September","October","November","December"};
    String[] year_data = {"2017", "2018", "2019"};
    String session_token,str_stock,str_line,str_month,str_year;
    Typeface segoeui;
    String str_mainpart,str_subpart,str_desc,str_schedule,str_multifactor,str_shhedule,get_initialqty;
    EditText mainpart_edt,subpart_edt,des_edt,schedule_edt,multifactor_edt;
    String get_main,get_sub,get_desc,get_initial,get_month,get_year,get_stock,get_line,get_partid,get_multifactor;
    Dialog loader_dialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_single_screen);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(AddSingleParts.this, v1);
        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");

        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");


        stock_spn=findViewById(R.id.stock_spn);
        lineorder_spn=findViewById(R.id.lineorder_spn);
        month_spn=findViewById(R.id.month_spn);
        year_spin=findViewById(R.id.year_spn);
        add_part_machine=findViewById(R.id.add_part_machine);
        mainpart_edt=findViewById(R.id.mainpart_edt);
        subpart_edt=findViewById(R.id.subpart_edt);
        des_edt=findViewById(R.id.des_edt);
        schedule_edt=findViewById(R.id.schedule_edt);
        back=findViewById(R.id.back);
        head_titletv=findViewById(R.id.head_titletv);

        multifactor_edt=findViewById(R.id.multifactor_edt);

        //set default Loader:
        loader_dialog = new Dialog(AddSingleParts.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        stock_spn.setVisibility(View.VISIBLE);
        lineorder_spn.setVisibility(View.VISIBLE);
        month_spn.setVisibility(View.VISIBLE);
        year_spin.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        try {


            get_main = intent.getStringExtra("mainpart");
            get_sub = intent.getStringExtra("subpart");
            get_desc = intent.getStringExtra("description");
            get_initial = intent.getStringExtra("initialqty");
            get_partid=intent.getStringExtra("partid");
            get_multifactor=intent.getStringExtra("multifactor");


            mainpart_edt.setText(get_main);
            subpart_edt.setText(get_sub);
            des_edt.setText(get_desc);
            schedule_edt.setText(get_initial);
            multifactor_edt.setText(get_multifactor);



        }catch (NullPointerException e)
        {
            get_main="";
            get_sub="";
            get_desc="";
            get_initial="";
            get_month="";
            get_year="";
            get_stock="";
            get_line="";

        }

            if(get_main!=null)
            {
                if(!get_main.equals(""))
                {
                    add_part_machine.setText("Update Part");
                    head_titletv.setText("Update Part Details");
                    stock_spn.setVisibility(View.INVISIBLE);
                    lineorder_spn.setVisibility(View.INVISIBLE);
                    month_spn.setVisibility(View.INVISIBLE);
                    year_spin.setVisibility(View.INVISIBLE);



                }
                else if(get_main.equals(""))
                {
                    add_part_machine.setText("Add Part");
                    head_titletv.setText("Add Part Details");
                    stock_spn.setVisibility(View.VISIBLE);
                    lineorder_spn.setVisibility(View.VISIBLE);
                    month_spn.setVisibility(View.VISIBLE);
                    year_spin.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                get_main="";
                add_part_machine.setText("Add Part");
                head_titletv.setText("Add Part Details");
            }




        // Stock Spinner Adapter:
        final CustomAdapter stockAdapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, stock_data) {
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
        stock_spn.setAdapter(stockAdapter);


        stock_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(AddSingleParts.this, view);
                str_stock = adapterView.getItemAtPosition(i).toString();
            }
        });



        // Line Spinner Adapter:
        final CustomAdapter lineAdapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, line_data) {
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
        lineorder_spn.setAdapter(lineAdapter);


        lineorder_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(AddSingleParts.this, view);
                str_line = adapterView.getItemAtPosition(i).toString();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),FileUploadActivity.class);
                startActivity(back);
                finish();
            }
        });



        // Month Spinner Adapter:
        final CustomAdapter monthAdapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, month_data) {
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
        month_spn.setAdapter(monthAdapter);

        month_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(AddSingleParts.this, view);
                str_month = adapterView.getItemAtPosition(i).toString();
            }
        });

        // year Spinner Adapter:
        final CustomAdapter yearAdapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, year_data) {
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
        year_spin.setAdapter(yearAdapter);


        year_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(AddSingleParts.this, view);
                str_year = adapterView.getItemAtPosition(i).toString();
            }
        });


        add_part_machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    str_mainpart = mainpart_edt.getText().toString().trim();
                    str_subpart = subpart_edt.getText().toString().trim();
                    str_desc = des_edt.getText().toString().trim();
                    str_schedule = schedule_edt.getText().toString().trim();
                    str_multifactor=multifactor_edt.getText().toString().trim();
                    str_shhedule=schedule_edt.getText().toString().trim();
                }catch (Exception e)
                {

                }



                if (add_part_machine.getText().toString().equals("Add Part")) {

                    if (Util.Operations.isOnline(AddSingleParts.this)){
                        if(!str_mainpart.equals("")) {
                            if(!str_subpart.equals("")){
                                if(!str_desc.equals("")){


                                    if (!str_schedule.equals("")) {
                                        if(!str_multifactor.equals("")) {
                                            if (str_stock != null) {
                                                if (str_line != null) {
                                                    if (str_month != null) {
                                                        if (str_year != null) {


                                                            int store_multifactor=Integer.parseInt(str_multifactor);
                                                            int store_scheduleqty=Integer.parseInt(str_schedule);
                                                            int store_schedule=store_scheduleqty*store_multifactor;
                                                            get_initialqty=String.valueOf(store_schedule);

                                                            new addSinglePartMachine().execute();


                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Year is Empty", Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Month is Empty", Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Line Order is Empty", Toast.LENGTH_LONG).show();
                                                }

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Stock Order is Empty", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Quantity Multifactor is Empty", Toast.LENGTH_LONG).show();
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Initial Qty is Empty", Toast.LENGTH_LONG).show();
                                    }

                                }

                                else {
                                    Toast.makeText(getApplicationContext(),"Part Description is Empty",Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Sub Part is Empty",Toast.LENGTH_LONG).show();
                            }
                        }

                        else {
                            Toast.makeText(getApplicationContext(),"Mainpart is Empty",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No Internet Connectivity",Toast.LENGTH_LONG).show();
                    }

                } else if (add_part_machine.getText().toString().equals("Update Part")) {


                    if (Util.Operations.isOnline(AddSingleParts.this)){
                        if(!str_mainpart.equals("")) {
                            if(!str_subpart.equals("")){
                                if(!str_desc.equals("")){
                                    if (!str_schedule.equals("")) {
                                        if(!str_multifactor.equals("")) {




                                                            int store_multifactor=Integer.parseInt(str_multifactor);
                                                            int store_scheduleqty=Integer.parseInt(str_schedule);
                                                            int store_schedule=store_scheduleqty*store_multifactor;
                                            get_initialqty=String.valueOf(store_schedule);

                                                            new updateSinglePartMachine().execute();
                                                        }




                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Quantity Multifactor is Empty", Toast.LENGTH_LONG).show();
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Initial Qty is Empty", Toast.LENGTH_LONG).show();
                                    }

                                }

                                else {
                                    Toast.makeText(getApplicationContext(),"Part Description is Empty",Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Sub Part is Empty",Toast.LENGTH_LONG).show();
                            }
                        }

                        else {
                            Toast.makeText(getApplicationContext(),"Mainpart is Empty",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No Internet Connectivity",Toast.LENGTH_LONG).show();
                    }





            }
        });


}




    //@@@@@@@@@@@@@@@@@@@@@@@@ ALLOCATE MACHINE WORK API
    public class addSinglePartMachine extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("mainpart", str_mainpart);
                jsonObject.accumulate("subpart", str_subpart);
                jsonObject.accumulate("partdescription", str_desc);
                jsonObject.accumulate("scheduleqty", str_schedule);
                jsonObject.accumulate("qtymultifactor", str_multifactor);
                jsonObject.accumulate("initialqty", get_initialqty);
                jsonObject.accumulate("stockstatus", str_stock);
                jsonObject.accumulate("lineorder", str_line);
                jsonObject.accumulate("month", str_month);
                jsonObject.accumulate("year", str_year);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_ADD_SINGLE_PARTS,json, session_token);

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
                    new SweetAlertDialog(AddSingleParts.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("SUCCESS MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    Intent job_mac=new Intent(getApplicationContext(),AddSingleParts.class);
                                    startActivity(job_mac);
                                    finish();
                                }
                            })
                            .show();


                } else {
                    new SweetAlertDialog(AddSingleParts.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {


                                }
                            })
                            .show();
                }
            } catch (Exception e) {

            }
        }

    }

    //@@@@@@@@@@@@@@@@@@@@@@@@ ALLOCATE MACHINE WORK API
    public class updateSinglePartMachine extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("mainpart", str_mainpart);
                jsonObject.accumulate("subpart", str_subpart);
                jsonObject.accumulate("partdescription", str_desc);
                jsonObject.accumulate("scheduleqty", str_schedule);
                jsonObject.accumulate("stockstatus", str_stock);
                jsonObject.accumulate("qtymultifactor", str_multifactor);
                jsonObject.accumulate("initialqty", get_initialqty);
                jsonObject.accumulate("partid",get_partid);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_UPDATE_SINGLE_PARTS,json, session_token);

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
                    new SweetAlertDialog(AddSingleParts.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("SUCCESS MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    Intent job_mac=new Intent(getApplicationContext(),AddSingleParts.class);
                                    startActivity(job_mac);
                                    finish();
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(AddSingleParts.this, SweetAlertDialog.WARNING_TYPE)
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