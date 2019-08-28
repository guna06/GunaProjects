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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONObject;


public class LoginActivity extends Activity {

    Typeface regular,bold,extraBold,light,thin;
    EditText email_et,password_et;
    TextView sidehead1,sidehead2,termsConditionTv,termsConditionTv1,termsConditionTv2;
    Button login_btn;
    String str_email,str_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        regular= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Regular.ttf");
        bold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Bold.ttf");
        extraBold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Extrabld.ttf");
        light= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Light.ttf");
        thin= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNovaT-Thin.ttf");


        email_et=findViewById(R.id.email_et);
        password_et=findViewById(R.id.password_et);
        sidehead1=findViewById(R.id.sidehead1);
        sidehead2=findViewById(R.id.sidehead2);
        termsConditionTv=findViewById(R.id.termsConditionTv);
        termsConditionTv1=findViewById(R.id.termsConditionTv1);
        termsConditionTv2=findViewById(R.id.termsConditionTv2);
        login_btn=findViewById(R.id.login_btn);

        email_et.setTypeface(regular);
        password_et.setTypeface(regular);
        sidehead1.setTypeface(extraBold);
        sidehead2.setTypeface(light);
        termsConditionTv.setTypeface(regular);
        termsConditionTv1.setTypeface(regular);
        termsConditionTv2.setTypeface(regular);
        login_btn.setTypeface(regular);



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 str_email=email_et.getText().toString().trim();
                 str_password=password_et.getText().toString().trim();
                if(!TextUtils.isEmpty(email_et.getText()))
                {
                    if(!TextUtils.isEmpty(password_et.getText()))
                    {
                        if (Util.Operations.isOnline(LoginActivity.this)) {
                            new Login_Async_Task().execute();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Check your Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        password_et.requestFocus();
                        password_et.setError( "Password is Required!" );
                    }
                }
                else
                {
                    email_et.requestFocus();
                    email_et.setError( "Email is Required!" );
                }
            }
        });



    }



    //Login API CALL: ---------------------------------------------------------------------->
    public class Login_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(LoginActivity.this); // this = YourActivity


        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Loading");
            dialog.setMessage("Loading. Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");


                Log.e("tag","PRINT input values---------------->"+str_email+"    "+str_password);

                RequestBody body1 = RequestBody.create(mediaType, "{\n\"email\":\"" +str_email
                        + "\",\n\"password\":\"" + str_password
                        + "\"\n}");



                Request request = new Request.Builder()
                        .url(Config.currentpath+"login")
                        .post(body1)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("User-Agent", "PostmanRuntime/7.15.2")
                        .addHeader("Accept", "*/*")
                        .addHeader("Cache-Control", "no-cache")
                        .build();

                Response response = client.newCall(request).execute();
                return response.body().string();


               // Log.e("tag","print -----"+response.toString());

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();


            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("success");

                if (status.equals("true")) {

                    JSONObject object=jo.getJSONObject("data");
                    String id=object.getString("id");
                    String email=object.getString("email");
                    String role=object.getString("roles");
                    String token=object.getString("access_token");

                   Intent ne=new Intent(getApplicationContext(),DashboardActivity1.class);
                   startActivity(ne);
                   finish();

                    SharedPreferences putLoginData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor putLoginEditor = putLoginData.edit();
                    putLoginEditor.putString("api_key",token);
                    putLoginEditor.putString("user_id",id);
                    putLoginEditor.putString("user_email",email);
                    putLoginEditor.putString("user_role",role);
                    putLoginEditor.putString("login_status","true") ;
                    putLoginEditor.commit();


                    Log.e("tag","Login API------------------>"+token);


                } else {
                    String message = jo.getString("message");
                    final Dialog dialog = new Dialog(LoginActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_alert);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                    Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                    TextView tv_message=(TextView)dialog.findViewById(R.id.tv_message) ;
                    tv_message.setText(message);

                    ok_btn.setTypeface(tf);
                    tv_message.setTypeface(tf);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });

                    dialog.show();


                }
            } catch (Exception e) {
            }

        }
    }

}
