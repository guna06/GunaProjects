package com.nextinnovation.pt.barcodescanner.Sqmobiles;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.Sqmobiles.utils.Config;
import com.nextinnovation.pt.barcodescanner.Sqmobiles.utils.Util;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReceiveProducts extends Activity {
    ProductsPojo productsPojo;
    RecyclerView recyclerView;
    private List<ProductsPojo> productList=new ArrayList<>();
    ReceiveProductsAdapter receiveProductsAdapter;
    SharedPreferences.Editor editor;
    String session_token,session_email;
    LinearLayout noparts_found,backarrow;
    TextView head;
    Typeface regular,bold,extraBold,light,thin,quondo;
    AVLoadingIndicatorView avi;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivestock);

        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("api_key", "");
        session_email=sharedPreferences.getString("user_role","");


        recyclerView=findViewById(R.id.recyclerView);
        noparts_found=findViewById(R.id.noparts_found);


        noparts_found.setVisibility(View.GONE);
        backarrow=findViewById(R.id.backarrow);
        head=findViewById(R.id.head);
        avi=findViewById(R.id.avi);


        regular= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Regular.ttf");
        bold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Bold.ttf");
        extraBold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Extrabld.ttf");
        light= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Light.ttf");
        thin= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNovaT-Thin.ttf");
        quondo= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Realistica Demo.otf");

        head.setTypeface(bold);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                Intent i=new Intent(getApplicationContext(),DashboardActivity1.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                finish();
            }
        });


        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        receiveProductsAdapter = new ReceiveProductsAdapter(getApplicationContext(), productList);
        recyclerView.setAdapter(receiveProductsAdapter);




        if (Util.Operations.isOnline(getApplicationContext())) {
            new ReceiveProducts_Task().execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }



    //Scan API CALL: ---------------------------------------------------------------------->
    public class ReceiveProducts_Task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            avi.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {


                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\n\"aa\":\"" +""
                        + "\"\n}");
                        Request request = new Request.Builder()
                        .url(Config.currentpath+"getNotReceivedProductStock")
                        .post(body)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer "+session_token)
                        .addHeader("Content-Type", "application/json")
                        .build();

                Response response = client.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            avi.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("success");

                if (status.equals("true")) {

                    JSONArray array=jo.getJSONArray("data");


                        if(array.length()==0)
                        {

                            noparts_found.setVisibility(View.VISIBLE);

                        }
                        else
                        {
                                noparts_found.setVisibility(View.GONE);
                                for(int i=0;i<array.length();i++)
                                {
                                    try{
                                        productsPojo = new ProductsPojo();
                                        JSONObject object=array.getJSONObject(i);
                                        productsPojo.imei=object.getString("imei_number");
                                        productsPojo.brand=object.getString("brand_name");
                                        productsPojo.product=object.getString("product_name");
                                        productsPojo.from=object.getString("sent_from");
                                        productList.add(productsPojo);

                                    }catch (Exception e){

                                    }

                                }




                        }

                } else if(status.equals("false")){




                }
            } catch (Exception e) {
            }


            // Setup and Handover data to recyclerview
            recyclerView.setAdapter(receiveProductsAdapter);

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),DashboardActivity1.class);
        startActivity(i);
        overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
        finish();
    }
}
