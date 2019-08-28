package com.sqindia.www.auto360parts_M.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;
import com.sqindia.www.auto360parts_M.Adapter.ViewPartsAdapter;
import com.sqindia.www.auto360parts_M.Font.FontsOverride;
import com.sqindia.www.auto360parts_M.Model.PhotoItem;

import com.sqindia.www.auto360parts_M.R;
import com.sqindia.www.auto360parts_M.Utils.Config;
import com.sqindia.www.auto360parts_M.Utils.HttpUtils;
import com.sqindia.www.auto360parts_M.Utils.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewCarPartsActivity extends Activity {
    RecyclerView recyclerView;
    PhotoItem photoItem;
    ViewPartsAdapter viewPartsAdapter;
    private List<PhotoItem> photoItems;
    LinearLayout back,noparts_found;
    Dialog loader_dialog;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String userid_str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcarparts);



        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewCarPartsActivity.this, v1);

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(ViewCarPartsActivity.this);
        editlogin = sharedlogin.edit();
        userid_str = sharedlogin.getString("org_user_id", "");

        recyclerView=findViewById(R.id.recyclerView);
        back=findViewById(R.id.back);
        photoItems=new ArrayList<>();

        //set default Loader:
        loader_dialog = new Dialog(ViewCarPartsActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        noparts_found=findViewById(R.id.noparts_found);

        noparts_found.setVisibility(View.GONE);

        if (Util.Operations.isOnline(ViewCarPartsActivity.this)) {
            new getCarParts().execute();
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
    public class getCarParts extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ViewCarPartsActivity.this);
            dialog.setMessage("Loading..., please wait.");
            dialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user_id",userid_str);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.GET_CAR_PARTS,json);

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
                    JSONArray jsonArray=jo.getJSONArray("message");
                    noparts_found.setVisibility(View.GONE);
                    for(int l=0;l<jsonArray.length();l++)
                    {
                        photoItem = new PhotoItem();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        photoItem.car_id=jsonObject.getString("car_id");
                        photoItem.car_brand=jsonObject.getString("car_brand");
                        photoItem.car_model=jsonObject.getString("car_model");
                        photoItem.car_year=jsonObject.getString("car_year");
                        photoItem.car_mileage=jsonObject.getString("car_mileage");
                        photoItem.car_price=jsonObject.getString("car_price");
                        photoItem.car_location=jsonObject.getString("car_location");
                        photoItem.car_image1=jsonObject.getString("car_image1");
                        photoItem.car_image2=jsonObject.getString("car_image2");
                        photoItem.vin_image1=jsonObject.getString("car_vin_image1");
                        photoItem.vin_image2=jsonObject.getString("car_vin_image2");
                        photoItem.car_vinno=jsonObject.getString("car_vin_no");
                        photoItem.car_status=jsonObject.getString("car_status");
                        photoItems.add(photoItem);
                    }


                    // Setup and Handover data to recyclerview


                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setHasFixedSize(true);
                    viewPartsAdapter = new ViewPartsAdapter(ViewCarPartsActivity.this, photoItems);
                    recyclerView.setAdapter(viewPartsAdapter);


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
