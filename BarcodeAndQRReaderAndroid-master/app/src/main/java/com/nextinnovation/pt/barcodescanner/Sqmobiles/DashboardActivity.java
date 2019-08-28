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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nextinnovation.pt.barcodescanner.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

public class DashboardActivity extends Activity {
    Typeface regular,bold,extraBold,light,thin;
    TextView sidehead1,sidehead2,sidehead3,sidehead4,termsConditionTv,termsConditionTv1,termsConditionTv2
            ,profile,salesentry,stocktf,stockstatus;

    SharedPreferences.Editor editor;
    String session_token;
    LinearLayout profile_lnr,stockentry_lnr,saletf_lnr,report_lnr,logout;
    ImageView profile_img,salesentry_img,stocktf_img,report_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        regular= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Regular.ttf");
        bold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Bold.ttf");
        extraBold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Extrabld.ttf");
        light= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Light.ttf");
        thin= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNovaT-Thin.ttf");




        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("api_key", "");


        sidehead1=findViewById(R.id.sidehead1);
        sidehead2=findViewById(R.id.sidehead2);
        sidehead3=findViewById(R.id.sidehead3);
        sidehead4=findViewById(R.id.sidehead4);
        termsConditionTv=findViewById(R.id.termsConditionTv);
        termsConditionTv1=findViewById(R.id.termsConditionTv1);
        termsConditionTv2=findViewById(R.id.termsConditionTv2);
        profile=findViewById(R.id.profile);
        salesentry=findViewById(R.id.salesentry);
        stocktf=findViewById(R.id.stocktf);
        stockstatus=findViewById(R.id.stockstatus);
        profile_lnr=findViewById(R.id.profile_lnr);
        stockentry_lnr=findViewById(R.id.stockentry_lnr);
        saletf_lnr=findViewById(R.id.saletf_lnr);
        report_lnr=findViewById(R.id.report_lnr);
        profile_img=findViewById(R.id.profile_img);
        salesentry_img=findViewById(R.id.salesentry_img);
        stocktf_img=findViewById(R.id.stocktf_img);
        logout=findViewById(R.id.logout);
        report_img=findViewById(R.id.report_img);




        sidehead1.setTypeface(bold);
        sidehead2.setTypeface(bold);
        sidehead3.setTypeface(regular);
        sidehead4.setTypeface(regular);
        termsConditionTv.setTypeface(regular);
        termsConditionTv1.setTypeface(regular);
        termsConditionTv2.setTypeface(regular);

        profile.setTypeface(regular);
        salesentry.setTypeface(regular);
        stocktf.setTypeface(regular);
        stockstatus.setTypeface(regular);


        profile_img.setImageResource(R.drawable.profile_color);
        salesentry_img.setImageResource(R.drawable.entry_color);
        stocktf_img.setImageResource(R.drawable.stocktf_color);
        report_img.setImageResource(R.drawable.report_color);




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(DashboardActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_logout);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
                Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                TextView tv=(TextView)dialog.findViewById(R.id.tv) ;

                cancel_btn.setTypeface(tf);
                ok_btn.setTypeface(tf);
                tv.setTypeface(tf);


                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = shared.edit();
                        edit.putString("login_status","false");
                        edit.commit();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        edit.clear();
                        edit.commit();
                        dialog.dismiss();*/


                        new Logout_Async_Task().execute();
                    }
                });

                dialog.show();
            }
        });




        profile_lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profile_lnr.setBackgroundResource(R.drawable.fill_circle_round_bg);
                stockentry_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);
                saletf_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);
                report_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);

                profile_img.setImageResource(R.drawable.profile_white);
                salesentry_img.setImageResource(R.drawable.entry_color);
                stocktf_img.setImageResource(R.drawable.stocktf_color);
                report_img.setBackgroundResource(R.drawable.report_color);
                Toast.makeText(getApplicationContext(),"Profile Under Development", Toast.LENGTH_LONG).show();
            }
        });



        stockentry_lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                profile_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);
                stockentry_lnr.setBackgroundResource(R.drawable.fill_circle_round_bg);
                saletf_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);
                report_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);
                salesentry_img.setImageResource(R.drawable.entry_white);
                profile_img.setImageResource(R.drawable.profile_color);
                stocktf_img.setImageResource(R.drawable.stocktf_color);
                report_img.setBackgroundResource(R.drawable.report_color);

                Intent sales_entry=new Intent(getApplicationContext(),SalesEntryActivity.class);
                startActivity(sales_entry);
                finish();
            }
        });


        saletf_lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profile_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);
                stockentry_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);
                saletf_lnr.setBackgroundResource(R.drawable.fill_circle_round_bg);
                report_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);

                stocktf_img.setImageResource(R.drawable.stocktf);
                salesentry_img.setImageResource(R.drawable.entry_color);
                profile_img.setImageResource(R.drawable.profile_color);
                report_img.setBackgroundResource(R.drawable.report_color);

                Intent j=new Intent(getApplicationContext(),StockTransferActivity.class);
                startActivity(j);
                finish();

            }
        });


        report_lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profile_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);
                stockentry_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);
                saletf_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);
                report_lnr.setBackgroundResource(R.drawable.fill_circle_round_bg);

                report_img.setBackgroundResource(R.drawable.report_white);
                profile_img.setBackgroundResource(R.drawable.profile_color);
                stocktf_img.setBackgroundResource(R.drawable.stocktf_color);
                salesentry_img.setBackgroundResource(R.drawable.entry_color);
                Toast.makeText(getApplicationContext(),"Stock Status Under Development", Toast.LENGTH_LONG).show();
            }
        });


    }




    //Login API CALL: ---------------------------------------------------------------------->
    public class Logout_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(DashboardActivity.this); // this = YourActivity


        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("SQMobiles Alert!!");
            dialog.setMessage("Logout... Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");

                RequestBody body = RequestBody.create(mediaType, "{\n    \"aa\": \"aa\",\n    \"aa\": \"aa\"\n}");

                Request request = new Request.Builder()
                        .url("http://104.197.80.225/sqmobiles/public/api/mobile/auth/logout")
                        .post(body)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer "+session_token)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Cache-Control", "no-cache")
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
            Log.e("tag","G0"+s);
            dialog.dismiss();


            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("success");

                if (status.equals("true")) {
                    Log.e("tag","G1");
                  String message=jo.getString("message");
                  Log.e("tag","G2"+message);


                    final Dialog dialog1 = new Dialog(DashboardActivity.this);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setCancelable(false);
                    dialog1.setContentView(R.layout.dialog_alert);
                    dialog1.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                    Button ok_btn = (Button) dialog1.findViewById(R.id.ok_btn);
                    TextView tv_message=(TextView)dialog1.findViewById(R.id.tv_message) ;
                    tv_message.setText(message);
                    tv_message.setText(message);

                    ok_btn.setTypeface(tf);
                    tv_message.setTypeface(tf);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit = shared.edit();
                            edit.putString("login_status","false");
                            edit.commit();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });

                    dialog1.show();


                } else {
                    String message=jo.getString("message");
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
            }

        }
    }
}