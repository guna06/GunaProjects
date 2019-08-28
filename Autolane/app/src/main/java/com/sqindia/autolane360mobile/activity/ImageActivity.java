package com.sqindia.autolane360mobile.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.autolane360mobile.R;
import com.sqindia.autolane360mobile.adapter.ImageAdapter;
import com.sqindia.autolane360mobile.adapter.ViewCarAdapter;
import com.sqindia.autolane360mobile.font.FontsOverride;
import com.sqindia.autolane360mobile.model.ImageList;
import com.sqindia.autolane360mobile.utils.Config;
import com.sqindia.autolane360mobile.utils.HttpUtils;
import com.sqindia.autolane360mobile.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends Activity {

    public RecyclerView recyclerView;
    ImageList imageList;
    private List<ImageList> imagePartsList = new ArrayList<>();
    LinearLayout back, noparts_found;
    ImageAdapter imageAdapter;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String userid_str,token,stockid;
    Intent i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(ImageActivity.this);
        editlogin = sharedlogin.edit();
        userid_str = sharedlogin.getString("org_user_id", "");
        token=sharedlogin.getString("token","");
        i=getIntent();
        stockid=i.getStringExtra("getstockid");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ImageActivity.this, v1);

        recyclerView = findViewById(R.id.recyclerView);

        back = findViewById(R.id.back);
        noparts_found = findViewById(R.id.noparts_found);



        noparts_found.setVisibility(View.GONE);

        if (Util.Operations.isOnline(ImageActivity.this)) {
            new getPartsLists().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ViewDetailActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), ViewDetailActivity.class);
        startActivity(i);
        finish();
    }





    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL VIEW  PARTS LIST API
    public class getPartsLists extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("carstock_id", "44");
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.GET_IMAGES, token,json);

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

                    JSONArray jsonArray = jo.getJSONArray("cars_img");
                    if(jsonArray.length()>0)
                    {
                        for (int l = 0; l < jsonArray.length(); l++) {
                            imageList = new ImageList();
                            JSONObject jsonObject = jsonArray.getJSONObject(l);
                            imageList.img_stockimgid = jsonObject.getString("carstockimg_id");
                            imageList.img_stockid = jsonObject.getString("carstock_id");
                            imageList.img_imagetype = jsonObject.getString("image_type");
                            imageList.img_admincarstockimg = jsonObject.getString("admincar_stockimg");

                            imagePartsList.add(imageList);
                        }

                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setHasFixedSize(true);
                        imageAdapter = new ImageAdapter(ImageActivity.this, imagePartsList);
                        recyclerView.setAdapter(imageAdapter);
                    }
                    else
                    {
                        noparts_found.setVisibility(View.VISIBLE);
                    }

                } else {
                    noparts_found.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
            }
        }
    }
}
