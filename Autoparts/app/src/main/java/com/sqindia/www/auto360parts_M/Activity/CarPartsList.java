package com.sqindia.www.auto360parts_M.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.sqindia.www.auto360parts_M.Adapter.SampleAdapter;
import com.sqindia.www.auto360parts_M.Font.FontsOverride;
import com.sqindia.www.auto360parts_M.Model.PartsList;
import com.sqindia.www.auto360parts_M.R;
import com.sqindia.www.auto360parts_M.Utils.Config;
import com.sqindia.www.auto360parts_M.Utils.HttpUtils;
import com.sqindia.www.auto360parts_M.Utils.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CarPartsList extends Activity {
    public RecyclerView recyclerView;
    PartsList partsList;
    private List<PartsList> carPartsLists = new ArrayList<>();
    LinearLayout back, noparts_found;
    SampleAdapter sampleAdapter;
    Intent intent;
    String part_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carpartslist);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(CarPartsList.this, v1);

        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        noparts_found = findViewById(R.id.noparts_found);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        sampleAdapter = new SampleAdapter(CarPartsList.this, carPartsLists);
        recyclerView.setAdapter(sampleAdapter);

        intent = getIntent();
        part_id = intent.getStringExtra("show_carpart_id");
        noparts_found.setVisibility(View.GONE);

        if (Util.Operations.isOnline(CarPartsList.this)) {
            new getPartsLists().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ViewCarPartsActivity.class);
                startActivity(i);
                finish();
            }
        });
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
                jsonObject.put("car_id", part_id);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.GET_CAR_FULL_DETAILS, json);

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

                    JSONArray jsonArray = jo.getJSONArray("part_details");
                    if(jsonArray.length()>0)
                    {
                        for (int l = 0; l < jsonArray.length(); l++) {
                            partsList = new PartsList();
                            JSONObject jsonObject = jsonArray.getJSONObject(l);
                            partsList.carlist_partid = jsonObject.getString("part_id");
                            partsList.carlist_maincat = jsonObject.getString("main_cat");
                            partsList.carlist_subcat = jsonObject.getString("sub_cat");
                            partsList.carlist_subcat = jsonObject.getString("sub_cat");
                            partsList.carlist_image1 = jsonObject.getString("image1");
                            partsList.carlist_image2 = jsonObject.getString("image2");
                            carPartsLists.add(partsList);
                        }

                        sampleAdapter.notifyDataSetChanged();
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


