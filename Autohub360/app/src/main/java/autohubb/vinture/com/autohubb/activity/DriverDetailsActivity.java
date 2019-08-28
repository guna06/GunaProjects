package autohubb.vinture.com.autohubb.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.Adapter.DriverAdapter;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.model.ShowDriver;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;

public class DriverDetailsActivity extends Activity {
    RecyclerView recyclerView_driver;
    ShowDriver showDriverList;
    DriverAdapter driverAdapter;
    private List<ShowDriver> driverAdapterList = new ArrayList<>();
    LinearLayout back_lv, noparts_found;
    TextView nodata_tv1, nodata_tv2;
    Typeface helvetical;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String get_mail_from_reg, get_otp_from_reg, get_apikey_from_login, get_email_from_login, get_userid_from_login, get_username_from_login;
    public static String DRIVER_DETAILS_URL = "http://192.168.1.6/autohubbapi/vehicle/getDrivers/vehicleId/19";
    Intent i;
    String get_vehiucle_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_details);
        i=getIntent();
        get_vehiucle_id=i.getStringExtra("pass_vehicle_id");

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(DriverDetailsActivity.this, v1);

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg = sharedlogin.getString("get_otp_from_reg", "");

        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login = sharedlogin.getString("get_email_from_login", "");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login = sharedlogin.getString("get_username_from_login", "");


        nodata_tv1 = findViewById(R.id.nodata_tv1);
        nodata_tv2 = findViewById(R.id.nodata_tv2);
        noparts_found = findViewById(R.id.noparts_found);
        recyclerView_driver=findViewById(R.id.recyclerView_driver);
        back_lv=findViewById(R.id.back_lv);

        nodata_tv1.setTypeface(helvetical);
        nodata_tv2.setTypeface(helvetical);


        noparts_found.setVisibility(View.GONE);


        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView_driver.setLayoutManager(manager);
        recyclerView_driver.setHasFixedSize(true);
        driverAdapter = new DriverAdapter(getApplicationContext(), driverAdapterList);
        recyclerView_driver.setAdapter(driverAdapter);


        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView_driver = findViewById(R.id.recyclerView_driver);

        new DriverList_Task().execute();
    }


    //MYCARS API CALL: ---------------------------------------------------------------------->

    public class DriverList_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DriverDetailsActivity.this);
            dialog.setMessage("Loading..., please wait.");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_DRIVER_INFO+get_vehiucle_id,get_apikey_from_login);

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
            try{
                JSONArray car_array=new JSONArray(s);
                Log.e("tag","Array----------------->"+car_array.length());
                for(int i=0;i<car_array.length();i++)
                {

                    try {
                        showDriverList = new ShowDriver();
                        JSONObject jsonObject = car_array.getJSONObject(i);
                        showDriverList.driver_id=jsonObject.getString("driver_id");
                        showDriverList.vehicle_id=jsonObject.getString("vehicle_id");
                        showDriverList.cmp_id=jsonObject.getString("cmp_id");
                        showDriverList.driver_fname=jsonObject.getString("driver_fname");
                        showDriverList.driver_lname=jsonObject.getString("driver_lname");
                        showDriverList.driver_phone=jsonObject.getString("driver_phone");
                        showDriverList.driver_email=jsonObject.getString("driver_email");
                        showDriverList.driver_city=jsonObject.getString("driver_city");
                        showDriverList.driver_state=jsonObject.getString("driver_state");
                        showDriverList.driver_image=jsonObject.getString("driver_image");

                        driverAdapterList.add(showDriverList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
            catch (Exception e)
            {

            }

// Setup and Handover data to recyclerview
            recyclerView_driver.setAdapter(driverAdapter);

/*
            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");



                if (status.equals("success")) {


                    JSONArray privatecar=jo.getJSONArray("car_detail");

                    if(privatecar.length()==0)
                    {
                        noparts_found.setVisibility(View.VISIBLE);
                    }
                    Log.e("tag","printing---->"+privatecar.length());
                    for(int p=0;p<privatecar.length();p++)
                    {
                        commercialCarList = new CommercialCarListPojo();
                        JSONObject jsonObject_private =privatecar.getJSONObject(p);
                        commercialCarList.commercial_id=jsonObject_private.getString("commercial_id");
                        commercialCarList.user_id=jsonObject_private.getString("user_id");
                        commercialCarList.car_type=jsonObject_private.getString("car_type");
                        commercialCarList.com_name=jsonObject_private.getString("com_name");
                        commercialCarList.com_email=jsonObject_private.getString("com_email");
                        commercialCarList.com_phone=jsonObject_private.getString("com_phone");
                        commercialCarList.address1=jsonObject_private.getString("address1");
                        commercialCarList.address2=jsonObject_private.getString("address2");
                        commercialCarList.city=jsonObject_private.getString("city");
                        commercialCarList.contact_name=jsonObject_private.getString("contact_name");
                        commercialCarList.contact_phone=jsonObject_private.getString("contact_phone");
                        commercialCarList.truck_type=jsonObject_private.getString("truck_type");
                        commercialCarList.vin_number=jsonObject_private.getString("vin_number");
                        commercialCarList.car_make=jsonObject_private.getString("car_make");
                        commercialCarList.car_model=jsonObject_private.getString("car_model");
                        commercialCarList.car_year=jsonObject_private.getString("car_year");
                        commercialCarList.mileage_range=jsonObject_private.getString("mileage_range");
                        commercialCarList.actual_mileage=jsonObject_private.getString("actual_mileage");
                        commercialCarList.created_at=jsonObject_private.getString("created_at");
                        commercialCarList.car_img_id=jsonObject_private.getString("car_img_id");
                        commercialCarList.img_url=jsonObject_private.getString("img_url");
                        carListList.add(commercialCarList);
                    }

                    // Setup and Handover data to recyclerview
                    private_recyclerView.setAdapter(privateCarAdapter);


                } else {
                    //String message = jo.getString("message");
                    noparts_found.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
            }
*/

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
