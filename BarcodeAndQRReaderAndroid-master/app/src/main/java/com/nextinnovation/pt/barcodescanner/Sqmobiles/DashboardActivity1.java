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

public class DashboardActivity1 extends Activity {
    Typeface regular,bold,extraBold,light,thin,quondo;
    TextView sidehead1,sidehead2,sidehead3,sidehead4,termsConditionTv,termsConditionTv1,termsConditionTv2
            ,profile,stockstatus,pressscan_tv,quickreview_tv;

    SharedPreferences.Editor editor;
    String session_token;
    LinearLayout profile_lnr,report_lnr,logout,scan_lnr;
    ImageView profile_img,report_img;
    Intent i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard1);



        regular= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Regular.ttf");
        bold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Bold.ttf");
        extraBold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Extrabld.ttf");
        light= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Light.ttf");
        thin= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNovaT-Thin.ttf");
        quondo= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Realistica Demo.otf");


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
        stockstatus=findViewById(R.id.stockstatus);
        profile_lnr=findViewById(R.id.profile_lnr);
        report_lnr=findViewById(R.id.report_lnr);
        profile_img=findViewById(R.id.profile_img);
        logout=findViewById(R.id.logout);
        report_img=findViewById(R.id.report_img);
        scan_lnr=findViewById(R.id.scan_lnr);
        pressscan_tv=findViewById(R.id.pressscan_tv);
        quickreview_tv=findViewById(R.id.quickreview_tv);




        sidehead1.setTypeface(bold);
        sidehead2.setTypeface(regular);
        sidehead3.setTypeface(regular);
        sidehead4.setTypeface(regular);
        termsConditionTv.setTypeface(regular);
        termsConditionTv1.setTypeface(regular);
        termsConditionTv2.setTypeface(regular);

        profile.setTypeface(regular);
        stockstatus.setTypeface(regular);
        pressscan_tv.setTypeface(quondo);
        quickreview_tv.setTypeface(quondo);


        profile_img.setImageResource(R.drawable.profile_color);
        report_img.setImageResource(R.drawable.growth);





        quickreview_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),QuickReviewActivity.class);
                startActivity(intent);
                finish();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(DashboardActivity1.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_logout);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");
                Button cancel_btn = dialog.findViewById(R.id.cancel_btn);
                Button ok_btn =  dialog.findViewById(R.id.ok_btn);
                TextView tv=dialog.findViewById(R.id.tv);


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
                        dialog.dismiss();
                        new Logout_Async_Task().execute();
                    }
                });
                dialog.show();
            }
        });





        scan_lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), MainPageScanActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });




        profile_lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_lnr.setBackgroundResource(R.drawable.fill_circle_round_bg);
                report_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);
                profile_img.setImageResource(R.drawable.profile_white);
                report_img.setBackgroundResource(R.drawable.growth);
                Toast.makeText(getApplicationContext(),"Profile Under Development", Toast.LENGTH_LONG).show();
            }
        });



        report_lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_lnr.setBackgroundResource(R.drawable.circle_round_bg_light);
                report_lnr.setBackgroundResource(R.drawable.fill_circle_round_bg);
                report_img.setImageResource(R.drawable.growth_w);
                profile_img.setBackgroundResource(R.drawable.profile_color);


                Intent i=new Intent(getApplicationContext(),ReceiveProducts.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });

    }



    //Login API CALL: ---------------------------------------------------------------------->
    public class Logout_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(DashboardActivity1.this); // this = YourActivity


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
                        .url("http://mobiles.sqindia.net/api/mobile/auth/logout")
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


                    final Dialog dialog1 = new Dialog(DashboardActivity1.this);
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


                }

            } catch (Exception e) {
            }

        }
    }
}
