package com.sqindia.srinarapp.MachiningModule;

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
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.Model.MachineJobList;
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

public class ViewMachineJob extends Activity {

    LinearLayout back,refresh;
    String session_token,str_part,selected_part,selected_month,selected_year;
    SharedPreferences.Editor editor;
    MachineJobList machineJobList;
    private List<MachineJobList> machineJobLists,grouping_arraylist;
    ViewMachineAdapter viewJobListAdapter;
    RecyclerView view_mac_recycler;
    Dialog loader_dialog;
    AutoCompleteTextView ac_part;
    final ArrayList<String> main_part_arraylist=new ArrayList<>();
    int visibleItemCount=0;
    int offset=0;
    int totalItemCount=0;
    int firstVisibleItem=0;
    int previousTotal=0;
    Boolean loading=true;
    int visibleThreshold=5;
    Map<String, String> subpart_hash_part = new HashMap<>();
    Map<String, String> getyear = new HashMap<>();
    Map<String, String> getmonth = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_machine_work);

        hideSoftKeyboard();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewMachineJob.this, v1);
      //  mSwipeRefreshLayout=findViewById(R.id.swipeToRefresh);

        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");

        machineJobLists=new ArrayList<>();
        grouping_arraylist=new ArrayList<>();


        //************* Add Cast:
        back=findViewById(R.id.back);
        view_mac_recycler=findViewById(R.id.view_mac_recycler);
        refresh=findViewById(R.id.refresh);
        ac_part=findViewById(R.id.ac_part);
        ac_part.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        //set default Loader:
        loader_dialog = new Dialog(ViewMachineJob.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);



        machineJobList = new MachineJobList();

        // Setup and Handover data to recyclerview
        viewJobListAdapter = new ViewMachineAdapter(ViewMachineJob.this, machineJobLists);
        view_mac_recycler.setAdapter(viewJobListAdapter);
        viewJobListAdapter.notifyDataSetChanged();
        final LinearLayoutManager mLayoutManager=new LinearLayoutManager(getApplicationContext());
        view_mac_recycler.setLayoutManager(mLayoutManager);


        view_mac_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;

                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached
                    offset=offset+25;


                    //here call your webservice
                    new ViewMachineJobAsync(offset).execute();
                    // Do something
                    loading = true;


                }
            }
        });


        if (Util.Operations.isOnline(ViewMachineJob.this)) {
            new getMainPartsAsync().execute();
            new ViewMachineJobAsync(0).execute();


        }
        else
        {
            new SweetAlertDialog(ViewMachineJob.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No Internet Connectivity")
                    .show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),MachiningEntryActivity.class);
                startActivity(back);
                finish();
            }
        });



        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),ViewMachineJob.class);
                startActivity(back);
                finish();
            }
        });








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
        Intent back=new Intent(getApplicationContext(),MachiningEntryActivity.class);
        startActivity(back);
        finish();
    }




    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL MAIN PART API
    public class getMainPartsAsync extends AsyncTask<String, Void, String> {
        //private ProgressDialog dialog_mainpart;

        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog_mainpart = new ProgressDialog(ViewMachineJob.this);
            dialog_mainpart.setMessage("Loading Main Parts..., please wait.");
            dialog_mainpart.show();*/
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
           /* if (dialog_mainpart.isShowing()) {
                dialog_mainpart.dismiss();
            }*/

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
                    new SweetAlertDialog(ViewMachineJob.this, SweetAlertDialog.WARNING_TYPE)
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
    public class ViewMachineJobAsync extends AsyncTask<String, Void, String> {


        int current_offset;


        public ViewMachineJobAsync(int current_offset)
        {
            this.current_offset=current_offset;
        }


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
                return jsonStr = HttpUtils.makeRequestTokenGET(Config.WEB_URL_GET_MACHINE_ENTRIES+current_offset, session_token);

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
                    JSONArray jsonArray=jo.getJSONArray("machineworks");
                    //machineJobLists.clear();

                    for(int l=0;l<jsonArray.length();l++)
                    {

                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        machineJobList=new MachineJobList();
                        machineJobList._id=jsonObject.getString("batchid");
                        machineJobList.mac_shift=jsonObject.getString("shift");
                        machineJobList.mac_operator=jsonObject.getString("operatorname");
                        machineJobList.mac_machine=jsonObject.getString("machine_line");
                        machineJobList.mac_partid=jsonObject.getString("partid");
                        machineJobList.mac_part=jsonObject.getString("mainpart");
                        machineJobList.mac_subpart=jsonObject.getString("subpart");
                        machineJobList.mac_worktype=jsonObject.getString("worktype");
                        machineJobList.mac_quantitycompleted=jsonObject.getString("qtycompleted");
                        //machineJobList.mac_approved=jsonObject.getString("qtyapproved");
                       // machineJobList.mac_rejected=jsonObject.getString("qtyrejected");
                        machineJobList.mac_date=jsonObject.getString("production_date");
                        machineJobList.mac_remarks=jsonObject.getString("wcremarks");
                        machineJobList.mac_wcentryby=jsonObject.getString("wcentryby");
                        machineJobLists.add(machineJobList);
                    }

                    viewJobListAdapter.notifyDataSetChanged();

                } else {

                    String message = jo.getString("message");
                    //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                    /* new SweetAlertDialog(ViewMachineJob.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(message)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent back_mac=new Intent(getApplicationContext(),MachiningEntryActivity.class);
                                    startActivity(back_mac);
                                    finish();
                                }
                            })
                            .show();*/
                }
            } catch (Exception e) {

            }
        }
    }


    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL VIEW MACHINE API
    public class groupingSubpartsByMain extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog_grouping;


        protected void onPreExecute() {
            super.onPreExecute();
            dialog_grouping = new ProgressDialog(ViewMachineJob.this);
            dialog_grouping.setMessage("Loading..., please wait.");
            dialog_grouping.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("mainpart", selected_part);
                jsonObject.accumulate("worktype", "machining");
                jsonObject.accumulate("partmonth",selected_month);
                jsonObject.accumulate("partyear",selected_year);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GROUPING_SUBPART,json, session_token);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(dialog_grouping.isShowing())
            {
                dialog_grouping.dismiss();
            }


            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    JSONArray jsonArray=jo.getJSONArray("parts");
                    //machineJobLists.clear();
                   // viewJobListAdapter.notifyDataSetChanged();
                    for(int l=0;l<jsonArray.length();l++)
                    {
                        machineJobList = new MachineJobList();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        machineJobList._id=jsonObject.getString("batchid");
                        machineJobList.mac_shift=jsonObject.getString("shift");
                        machineJobList.mac_operator=jsonObject.getString("operatorname");
                        machineJobList.mac_machine=jsonObject.getString("machine_line");
                        machineJobList.mac_partid=jsonObject.getString("partid");
                        machineJobList.mac_part=jsonObject.getString("mainpart");
                        machineJobList.mac_subpart=jsonObject.getString("subpart");
                        machineJobList.mac_worktype=jsonObject.getString("worktype");
                        machineJobList.mac_quantitycompleted=jsonObject.getString("qtycompleted");
                        machineJobList.mac_remarks=jsonObject.getString("wcremarks");
                        machineJobList.mac_wcentryby=jsonObject.getString("wcentryby");
                       // machineJobList.mac_approved=jsonObject.getString("qtyapproved");
                       // machineJobList.mac_rejected=jsonObject.getString("qtyrejected");
                        machineJobList.mac_date=jsonObject.getString("wcentrycreatedat");


                        grouping_arraylist.add(machineJobList);
                        //loader_dialog.dismiss();
                    }


                    // Setup and Handover data to recyclerview
                    viewJobListAdapter = new ViewMachineAdapter(ViewMachineJob.this, grouping_arraylist);
                    view_mac_recycler.setAdapter(viewJobListAdapter);
                    viewJobListAdapter.notifyDataSetChanged();
                    view_mac_recycler.setLayoutManager(new LinearLayoutManager(ViewMachineJob.this));

                } else {

                    //loader_dialog.dismiss();
                    String message = jo.getString("message");
                    new SweetAlertDialog(ViewMachineJob.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(message)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent back_mac=new Intent(getApplicationContext(),ViewMachineJob.class);
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


    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
