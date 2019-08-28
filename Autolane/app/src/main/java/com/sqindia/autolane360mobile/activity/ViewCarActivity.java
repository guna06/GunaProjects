package com.sqindia.autolane360mobile.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.sqindia.autolane360mobile.R;
import com.sqindia.autolane360mobile.adapter.ViewCarAdapter;
import com.sqindia.autolane360mobile.font.FontsOverride;
import com.sqindia.autolane360mobile.model.CarList;
import com.sqindia.autolane360mobile.utils.Config;
import com.sqindia.autolane360mobile.utils.HttpUtils;
import com.sqindia.autolane360mobile.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ViewCarActivity extends Activity {
    RecyclerView recyclerView;
    CarList carList;
    ViewCarAdapter viewCarAdapter;
    private List<CarList> carListList;
    LinearLayout back,noparts_found;
    Dialog loader_dialog;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String userid_str,token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcar);



        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewCarActivity.this, v1);

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(ViewCarActivity.this);
        editlogin = sharedlogin.edit();
        userid_str = sharedlogin.getString("org_user_id", "");
        token=sharedlogin.getString("token","");

        Log.e("tag","jar---------------->"+userid_str+" "+token);


        recyclerView=findViewById(R.id.recyclerView);
        noparts_found=findViewById(R.id.noparts_found);
        back=findViewById(R.id.back);
        carListList=new ArrayList<>();
        noparts_found.setVisibility(View.GONE);



        if (Util.Operations.isOnline(ViewCarActivity.this)) {
           new getCarList().execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL VIEW MACHINE API
    public class getCarList extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ViewCarActivity.this);
            dialog.setMessage("Loading..., please wait.");
            dialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("adminId",userid_str);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.GET_CAR_LIST,token,json);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    JSONArray jsonArray=jo.getJSONArray("cars");
                    noparts_found.setVisibility(View.GONE);
                    for(int l=0;l<jsonArray.length();l++)
                    {
                        carList = new CarList();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        carList.car_stockid=jsonObject.getString("carstock_id");
                        carList.car_adminid=jsonObject.getString("admin_id");
                        carList.car_usertype=jsonObject.getString("user_type");
                        carList.car_date=jsonObject.getString("date");
                        carList.car_type=jsonObject.getString("car_type");
                        carList.car_year=jsonObject.getString("car_year");
                        carList.car_brand=jsonObject.getString("car_brand");
                        carList.car_model=jsonObject.getString("car_model");

                        carList.car_mileage=jsonObject.getString("car_mileage");
                        carList.car_location=jsonObject.getString("car_location");
                        carList.car_mileage=jsonObject.getString("car_mileage");
                        carList.car_vinstatus=jsonObject.getString("vin_status");
                        carList.car_engine=jsonObject.getString("engine");
                        carList.car_vehicleclass=jsonObject.getString("vehicle_class");
                        carList.car_transmission=jsonObject.getString("transmission");
                        carList.car_doors=jsonObject.getString("pasenger_door");
                        carList.car_manufacture=jsonObject.getString("manufactured");
                        carList.car_ac_condition=jsonObject.getString("ac_condition");
                        carList.car_interior_color=jsonObject.getString("interior_color");
                        carList.car_exterior_color=jsonObject.getString("exterior_color");
                        carList.car_keys=jsonObject.getString("car_keys");
                        carList.car_startcode=jsonObject.getString("start_code");
                        carList.car_description=jsonObject.getString("description");
                        carList.car_price=jsonObject.getString("price");
                        carList.car_image=jsonObject.getString("car_image");
                        carListList.add(carList);
                    }


                    // Setup and Handover data to recyclerview


                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setHasFixedSize(true);
                    viewCarAdapter = new ViewCarAdapter(ViewCarActivity.this, carListList);
                    recyclerView.setAdapter(viewCarAdapter);


                } else {
                    //String message = jo.getString("message");
                    noparts_found.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(i);
        finish();
    }
}
