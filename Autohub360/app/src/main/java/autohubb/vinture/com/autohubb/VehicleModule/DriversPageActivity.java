package autohubb.vinture.com.autohubb.VehicleModule;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.Adapter.DriversAdapter;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.model.DriverList;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;


public class DriversPageActivity extends Activity {
    DriversAdapter driversAdapter;
    DriverList driverList;
    RecyclerView driver_recyclerView;
    TextView nodata_tv1,nodata_tv2,addcompany_tv,head_tv,submit_company;
    private List<DriverList> driverLists =new ArrayList<>();
    Typeface helvetical;
    LinearLayout back_lv,noparts_found,close_lnr;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    AVLoadingIndicatorView av_loader,dialog_avi;
    Typeface lato_head;
    Dialog company_dialog;
    EditText com_name,email,phone,address1,address2,contact_name,contact_ph;
    String companyId,companyName,companyEmail,str_phone,str_address1,str_address2,contactname,contact_phone;
    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driverpage);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(DriversPageActivity.this, v1);
        driver_recyclerView=findViewById(R.id.recyclerView);
        helvetical = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");
        bundle= getIntent().getExtras();
        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editlogin = sharedlogin.edit();

        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");

        companyId = sharedlogin.getString("get_companyid_from_compannylist","");
        av_loader = findViewById(R.id.avi);
        back_lv=findViewById(R.id.back_lv);
        addcompany_tv=findViewById(R.id.addcompany_tv);
        nodata_tv1=findViewById(R.id.nodata_tv1);
        nodata_tv2=findViewById(R.id.nodata_tv2);
        noparts_found=findViewById(R.id.noparts_found);



        lato_head = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/heleveticalBold.TTF");
        addcompany_tv.setTypeface(lato_head);
        nodata_tv1.setTypeface(helvetical);
        nodata_tv2.setTypeface(helvetical);
        noparts_found.setVisibility(View.GONE);


        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        driver_recyclerView.setLayoutManager(manager);
        driver_recyclerView.setHasFixedSize(true);
        driversAdapter = new DriversAdapter(getApplicationContext(), driverLists);
        driver_recyclerView.setAdapter(driversAdapter);


        addcompany_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriversPageActivity.this, AddDriverActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            }
        });


        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),CommercialCarListActivity.class);
                startActivity(i);
                finish();
            }
        });


        new MyDrivers_Task().execute();
    }



    //MY COMPANY API CALL: ---------------------------------------------------------------------->
    public class MyDrivers_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_GET_DRIVER_DETAILS+companyId,get_apikey_from_login);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            av_loader.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    String message=jo.getString("message");


                    JSONObject data_object=jo.getJSONObject("data");

                    JSONArray array=data_object.getJSONArray("drivers");

                    if(array.length()==0)
                    {
                        noparts_found.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        noparts_found.setVisibility(View.GONE);
                        for(int c=0;c<array.length();c++)
                        {
                            try {
                                Log.e("tag","4--------------->");
                                driverList = new DriverList();
                                JSONObject object=array.getJSONObject(c);

                                driverList.id=object.getString("id");
                                Log.e("tag","id--------------->"+ driverList.id);
                                driverList.companyId=object.getString("companyId");
                                driverList.firstName=object.getString("firstName");
                                driverList.lastName=object.getString("lastName");
                                driverList.phone=object.getString("phone");
                                driverList.email=object.getString("email");
                                driverList.city=object.getString("city");
                                driverList.state=object.getString("state");
                                driverList.image=object.getString("image");
                                driverLists.add(driverList);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }



                } else {
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

            }

            // Setup and Handover data to recyclerview
            driver_recyclerView.setAdapter(driversAdapter);

        }
    }

    //UPLAOD COMPANY API CALL: ---------------------------------------------------------------------->
    private class upload_company extends AsyncTask<String,String,String>{

        protected void onPreExecute() {
            super.onPreExecute();
            dialog_avi.setVisibility(View.VISIBLE);
        }




        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            try {

                JSONObject commercial_data = new JSONObject();
                commercial_data.put("userId", get_userid_from_login);
                commercial_data.put("companyName", com_name.getText().toString());
                commercial_data.put("email", email.getText().toString());
                commercial_data.put("phone", phone.getText().toString());
                commercial_data.put("address1", address1.getText().toString());

                commercial_data.put("address2", address2.getText().toString());
                //commercial_data.put("contact_name", contact_name.getText().toString());



                json = commercial_data.toString();
                Log.e("tag","json response---------->"+json);


                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_ADD_COMPANY_DETAILS,get_apikey_from_login,json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog_avi.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");


                if (status.equals("true")) {
                    String message=jo.getString("message");



                    JSONObject object=jo.getJSONObject("data");
                    String companyId=object.getString("companyId");
                    company_dialog.dismiss();
                    Intent i=new Intent(getApplicationContext(),DriversPageActivity.class);
                    startActivity(i);
                    SharedPreferences putCompany = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor putVehicleEditor = putCompany.edit();
                    putVehicleEditor.putString("get_companyid_from_pRegister",companyId);
                    putVehicleEditor.commit();




                } else {
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }






        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(),CommercialCarListActivity.class);
        startActivity(back);
        finish();
    }

}
