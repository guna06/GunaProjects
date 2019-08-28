package com.sqindia.srinarapp.SuperAdmin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import com.sqindia.srinarapp.Adapter.PartListAdapter;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.Model.PartList;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ViewParts extends Activity {
    LinearLayout back;
    String session_token,str_part;
    SharedPreferences.Editor editor;
    PartList partList;
    private List<PartList> partLists;
    PartListAdapter partListAdapter;
    RecyclerView view_part_recycler;
    Dialog loader_dialog;
    AutoCompleteTextView ac_part;
    final ArrayList<String> main_part_arraylist=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_parts);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewParts.this, v1);
        back=findViewById(R.id.back);
        view_part_recycler=findViewById(R.id.view_part_recycler);
        ac_part=findViewById(R.id.ac_part);
        ac_part.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        //set default Loader:
        loader_dialog = new Dialog(ViewParts.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        new getMainPartsAsync().execute();

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

                //get All Sub Parts:
                new groupingSubpartsByMain().execute();
            }

        });



        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        partLists=new ArrayList<>();



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),FileUploadActivity.class);
                startActivity(back);
                finish();
            }
        });


        if (Util.Operations.isOnline(ViewParts.this)) {

            new viewPartsAsync().execute();
        }
        else
        {
            new SweetAlertDialog(ViewParts.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No Internet Connectivity")
                    .show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent back=new Intent(getApplicationContext(),FileUploadActivity.class);
        startActivity(back);
        finish();
    }


    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL MAIN PART API
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
                    new SweetAlertDialog(ViewParts.this, SweetAlertDialog.WARNING_TYPE)
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
    public class viewPartsAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_VIEW_PARTS, session_token);

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


                    JSONArray jsonArray=jo.getJSONArray("parts");
                    partLists.clear();
                    for(int l=0;l<jsonArray.length();l++)
                    {

                        partList = new PartList();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        partList.PL_mainpart=jsonObject.getString("mainpart");
                        partList.PL_subpart=jsonObject.getString("subpart");
                        partList.PL_Description=jsonObject.getString("partdescription");
                        partList.PL_partid=jsonObject.getString("partid");
                        partList.PL_month=jsonObject.getString("partmonth");
                        partList.PL_year=jsonObject.getString("partyear");
                        partList.PL_stockstatus=jsonObject.getString("stockstatus");
                        partList.PL_lineorder=jsonObject.getString("lineorder");
                        partList.Pl_qty_multifactor=jsonObject.getString("qty_multifactor");
                        partList.PL_initialqty=jsonObject.getString("initialqty");
                        partLists.add(partList);

                    }

                    loader_dialog.dismiss();
                    // Setup and Handover data to recyclerview
                    partListAdapter = new PartListAdapter(ViewParts.this, partLists);
                    view_part_recycler.setAdapter(partListAdapter);
                    partListAdapter.notifyDataSetChanged();
                    view_part_recycler.setLayoutManager(new LinearLayoutManager(ViewParts.this));

                } else {
                    String msg = jo.getString("message");
                    new SweetAlertDialog(ViewParts.this, SweetAlertDialog.WARNING_TYPE)
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
                jsonObject.accumulate("part", str_part);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_FILTER_PARTS, json, session_token);
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
                    JSONArray jsonArray=jo.getJSONArray("parts");
                    partLists.clear();
                    partListAdapter.notifyDataSetChanged();
                    for(int l=0;l<jsonArray.length();l++)
                    {
                        partList = new PartList();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        partList.PL_mainpart=jsonObject.getString("mainpart");
                        partList.PL_subpart=jsonObject.getString("subpart");
                        partList.PL_Description=jsonObject.getString("partdescription");
                        partList.PL_partid=jsonObject.getString("partid");
                        partList.PL_month=jsonObject.getString("partmonth");
                        partList.PL_year=jsonObject.getString("partyear");
                        partList.PL_stockstatus=jsonObject.getString("stockstatus");
                        partList.PL_lineorder=jsonObject.getString("lineorder");
                        partList.Pl_qty_multifactor=jsonObject.getString("qty_multifactor");
                        partList.PL_initialqty=jsonObject.getString("initialqty");
                        partLists.add(partList);
                    }

                    loader_dialog.dismiss();

                    // Setup and Handover data to recyclerview
                    partListAdapter = new PartListAdapter(ViewParts.this, partLists);
                    view_part_recycler.setAdapter(partListAdapter);
                    partListAdapter.notifyDataSetChanged();
                    view_part_recycler.setLayoutManager(new LinearLayoutManager(ViewParts.this));

                } else {
                    String msg = jo.getString("message");
                    new SweetAlertDialog(ViewParts.this, SweetAlertDialog.WARNING_TYPE)
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
