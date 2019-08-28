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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.MachiningModule.MachiningEntryActivity;
import com.sqindia.srinarapp.Model.DispatchedList;
import com.sqindia.srinarapp.R;
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

public class DispatchProduct extends Activity {
    LinearLayout back;
    Dialog loader_dialog;
    SharedPreferences.Editor editor;
    String session_token,str_part,selected_id,selected_part,selected_month,selected_year,eligibleqtytodispatch,edt_val,emp_id;
    ArrayList<String> main_part_arraylist=new ArrayList<>();
    AutoCompleteTextView ac_part;
    Map<String, String> subpart_hash_id = new HashMap<>();
    Map<String, String> subpart_hash_part = new HashMap<>();
    Map<String, String> subpart_hash_month = new HashMap<>();
    Map<String, String> subpart_hash_year = new HashMap<>();
    TextView noresults_tv;
    DispatchedList dispatchedList;
    DispatchedAdapter dispatchedAdapter;
    private List<DispatchedList> dispatchedLists;
    RecyclerView recyclerView;
    EditText edt_dispatchqty;
    Button dispatch_btn;
    int text_val;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispatch_product);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(DispatchProduct.this, v1);

        //set default Loader:
        loader_dialog = new Dialog(DispatchProduct.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);


        back=findViewById(R.id.back);
        ac_part=findViewById(R.id.ac_part);
        ac_part.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        noresults_tv=findViewById(R.id.noresults_tv);
        recyclerView=findViewById(R.id.recyclerView);
        noresults_tv.setVisibility(View.VISIBLE);
        dispatchedLists=new ArrayList<>();
        edt_dispatchqty=findViewById(R.id.edt_dispatchqty);
        edt_dispatchqty.setVisibility(View.GONE);
        dispatch_btn=findViewById(R.id.dispatch_btn);
        dispatch_btn.setVisibility(View.GONE);


        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        emp_id=sharedPreferences.getString("emp_id","");

        if (Util.Operations.isOnline(DispatchProduct.this)) {

            new getMainPartsAsync().execute();
        }
        else
        {
            new SweetAlertDialog(DispatchProduct.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No Internet Connectivity")
                    .show();
        }





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(back);
                finish();
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

                selected_id=subpart_hash_id.get(str_part);
                selected_part=subpart_hash_part.get(str_part);
                selected_month=subpart_hash_month.get(str_part);
                selected_year=subpart_hash_year.get(str_part);



                //get All Sub Parts:
                new groupingDispatchParts().execute();
            }

        });



        dispatch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int eligible=Integer.parseInt(eligibleqtytodispatch);
                edt_val=edt_dispatchqty.getText().toString();
                try{
                     text_val=Integer.parseInt(edt_val);
                }catch(Exception e)
                {
                    //Toast.makeText(getApplicationContext(),"Invlid Dispatch qty",Toast.LENGTH_LONG).show();
                }



                if (Util.Operations.isOnline(DispatchProduct.this)) {
                    if(!edt_dispatchqty.getText().toString().trim().equals("")) {
                        if (eligible >= text_val) {
                            if(!edt_dispatchqty.getText().toString().trim().equals("0"))
                            {
                                new DispatchedAsync().execute();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "This value is Invalid", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "This value is not eligible to dispatch", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Invalid Dispatch Qty", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    new SweetAlertDialog(DispatchProduct.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("No Internet Connectivity")
                            .show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(),DashboardActivity.class);
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
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_CALL_DISPATCH_MAINPARTS, session_token);

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
                        String part_id=jsonObject.getString("partid");
                        String part=jsonObject.getString("part");
                        String mainpart_part=jsonObject.getString("mainpart");
                        String part_month=jsonObject.getString("partmonth");
                        String part_year=jsonObject.getString("partyear");
                        main_part_arraylist.add(part);

                        subpart_hash_id.put(jsonObject.getString("part"),jsonObject.getString("partid"));
                        subpart_hash_part.put(jsonObject.getString("part"),jsonObject.getString("mainpart"));
                        subpart_hash_month.put(jsonObject.getString("part"),jsonObject.getString("partmonth"));
                        subpart_hash_year.put(jsonObject.getString("part"),jsonObject.getString("partyear"));
                    }

                } else {
                    new SweetAlertDialog(DispatchProduct.this, SweetAlertDialog.WARNING_TYPE)
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


    //@@@@@@@@@@@@@@@@@@@@@@@@ GROUPING DISPATCHED MACHINES
    public class groupingDispatchParts extends AsyncTask<String, Void, String> {

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
                jsonObject.accumulate("partmonth", selected_month);
                jsonObject.accumulate("partyear", selected_year);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GET_DISPATCH_BY_MAINPARTS, json, session_token);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();
            noresults_tv.setVisibility(View.GONE);
            edt_dispatchqty.setVisibility(View.VISIBLE);
            dispatch_btn.setVisibility(View.VISIBLE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                 eligibleqtytodispatch=jo.getString("eligibleqtytodispatch");
                 edt_dispatchqty.setHint("Eligible Dispatched Qty "+ eligibleqtytodispatch);

                if (status.equals("true")) {
                    JSONArray jsonArray = jo.getJSONArray("parts");
                    for (int l = 0; l < jsonArray.length(); l++) {
                        dispatchedList = new DispatchedList();
                        JSONObject jsonObject = jsonArray.getJSONObject(l);
                        dispatchedList._id = jsonObject.getString("partid");
                        dispatchedList.dis_part = jsonObject.getString("mainpart");
                        dispatchedList.dis_subpart = jsonObject.getString("subpart");
                        dispatchedList.dis_desc = jsonObject.getString("partdescription");
                        dispatchedList.dis_initial = jsonObject.getString("initialqty");
                        dispatchedList.dis_assemble_approved = jsonObject.getString("assembledapprovedqty");
                        dispatchedList.dis_dispatched = jsonObject.getString("dispatched_qty");
                        dispatchedList.dis_pending = jsonObject.getString("pendingdispatchqty");
                        dispatchedLists.add(dispatchedList);
                    }


                    // Setup and Handover data to recyclerview
                    dispatchedAdapter =new DispatchedAdapter(DispatchProduct.this,dispatchedLists);
                    recyclerView.setAdapter(dispatchedAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(DispatchProduct.this));

                } else {
                    String message = jo.getString("message");
                    new SweetAlertDialog(DispatchProduct.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(message)
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


    //@@@@@@@@@@@@@@@@@@@@@@@@ GROUPING DISPATCHED MACHINES
    public class DispatchedAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("employeeid", emp_id);
                jsonObject.accumulate("mainpart", selected_part);
                jsonObject.accumulate("partmonth", selected_month);
                jsonObject.accumulate("partyear", selected_year);
                jsonObject.accumulate("dispatched_qty", edt_val);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GET_DISPATCH, json, session_token);
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

                    new SweetAlertDialog(DispatchProduct.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("MESSAGE!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent i=new Intent(getApplicationContext(),DispatchProduct.class);
                                    startActivity(i);
                                    finish();

                                }
                            })

                            .show();



                } else {
                    String message = jo.getString("message");
                    new SweetAlertDialog(DispatchProduct.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(message)

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
