package com.sqindia.www.auto360parts_M.Activity;

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

import com.sqindia.www.auto360parts_M.Font.FontsOverride;

import com.sqindia.www.auto360parts_M.R;
import com.sqindia.www.auto360parts_M.Utils.Config;
import com.sqindia.www.auto360parts_M.Utils.HttpUtils;
import com.sqindia.www.auto360parts_M.Utils.Util;

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
                jsonObject.accumulate("admin_email", str_uname);
                jsonObject.accumulate("admin_password", str_pwd);
                jsonObject.accumulate("user_type","broker");
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
                String status = jo.getString("success");
                if (status.equals("true")) {
                    JSONObject object=jo.getJSONObject("data");
                    String userid = object.getString("admin_id");
                    String fname = object.getString("admin_fname");
                    String lname = object.getString("admin_lname");
                    String email = object.getString("admin_email");
                    String phone = object.getString("admin_phone");
                    String country=object.getString("admin_country");
                    String user_type = object.getString("user_type");

                    SharedPreferences putLoginData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor putLoginEditor = putLoginData.edit();
                    putLoginEditor.putString("user_name",fname+lname);
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
