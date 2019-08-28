package com.sqindia.autolane360mobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.sqindia.autolane360mobile.R;
import com.sqindia.autolane360mobile.font.FontsOverride;
import com.sqindia.autolane360mobile.utils.Config;
import com.sqindia.autolane360mobile.utils.HttpUtils;
import com.sqindia.autolane360mobile.utils.Util;

import org.json.JSONObject;



public class LoginActivity extends Activity {

    Button login_btn;
    EditText userid_edt,pwd_edt;
    String str_uname,str_pwd;
    Typeface helevetical;
    TextView version_tv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LoginActivity.this, v1);
        helevetical= Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");

        login_btn=findViewById(R.id.login_btn);
        userid_edt=findViewById(R.id.userid_edt);
        pwd_edt=findViewById(R.id.pwd_edt);
        version_tv=findViewById(R.id.version_tv);


        try {
            String versionName = getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
            version_tv.setText("Released Version : "+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_uname = userid_edt.getText().toString();
                str_pwd = pwd_edt.getText().toString();

                if (Util.Operations.isOnline(LoginActivity.this)) {
                    if (!str_uname.isEmpty() && !str_pwd.isEmpty()) {
                        new staffLogin_Task().execute();
                    } else {
                        Toast.makeText(getApplicationContext(),"Enter All details",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Check Internet Connectivity",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //LOGIN API CALL: ---------------------------------------------------------------------->
    public class staffLogin_Task extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Loading..., please wait.");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email", str_uname);
                jsonObject.accumulate("password", str_pwd);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_LOGIN, json);
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
                Log.e("tag","1"+status);
                if (status.equals("true")) {
                    String token=jo.getString("api_key");
                    Log.e("tag","2"+token);
                    JSONObject object=jo.getJSONObject("userData");
                    Log.e("tag","3"+object);
                    String email = object.getString("email");
                    Log.e("tag","4"+email);
                    String userid = object.getString("user_id");
                    Log.e("tag","5"+userid);
                    String name = object.getString("username");
                    Log.e("tag","6"+name);


                    SharedPreferences putLoginData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor putLoginEditor = putLoginData.edit();
                    putLoginEditor.putString("user_name",name);
                    putLoginEditor.putString("token",token);
                    putLoginEditor.putString("org_user_id",userid);
                    putLoginEditor.putString("login_status","true") ;
                    putLoginEditor.commit();

                    Intent i=new Intent(getApplicationContext(),DashboardActivity.class);
                    startActivity(i);
                    finish();

                } else {

                    Toast.makeText(getApplicationContext(),"Please check User credientials",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

            }
        }
    }
}
