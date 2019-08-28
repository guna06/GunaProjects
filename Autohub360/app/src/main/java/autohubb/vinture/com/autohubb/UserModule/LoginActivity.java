package autohubb.vinture.com.autohubb.UserModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.activity.MainActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.SharedPrefManager;
import autohubb.vinture.com.autohubb.splash.SplashActivity;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;
import es.dmoral.toasty.Toasty;


public class LoginActivity extends Activity {
    LinearLayout back_lv;
    TextView submit_tv;
    EditText email_et,password_et;
    TextInputLayout til_email;
    AVLoadingIndicatorView av_loader;
    String push_token,apikey,email;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LoginActivity.this, v1);

        back_lv=findViewById(R.id.back_lv);
        submit_tv=findViewById(R.id.submit_tv);
        password_et=findViewById(R.id.password_et);
        email_et=findViewById(R.id.email_et);
        av_loader = (AVLoadingIndicatorView) findViewById(R.id.avi);
        til_email=findViewById(R.id.til_email);




/*
        //left to right:
        TranslateAnimation anim = new TranslateAnimation(-5000f, 0f, 0f, 0f);  // might need to review the docs
        anim.setDuration(2000); // set how long you want the animation
        mail_lr.setAnimation(anim);
        mail_lr.setVisibility(View.VISIBLE);

        //right to left:
        TranslateAnimation anim2 = new TranslateAnimation(+5000f, 0f, 0f, 0f);  // might need to review the docs
        anim2.setDuration(2000); // set how long you want the animation
        pwd_lr.setAnimation(anim2);
        pwd_lr.setVisibility(View.VISIBLE);*/


        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),SplashActivity.class);
                startActivity(back);
                finish();
            }
        });


        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(email_et.getText()))
                {
                    if(!TextUtils.isEmpty(password_et.getText()))
                    {
                        if (Util.Operations.isOnline(LoginActivity.this)) {
                            new LoginAsync_Task().execute();
                        }
                        else
                        {
                            Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(),SplashActivity.class);
        startActivity(back);
        finish();
    }




    //Login API CALL: ---------------------------------------------------------------------->
    public class LoginAsync_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Loading..., please wait.");
            dialog.show();*/

            av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email",email_et.getText().toString());
                jsonObject.accumulate("password",password_et.getText().toString());

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_LOGIN,json);



            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            av_loader.setVisibility(View.GONE);

           /* if (dialog.isShowing()) {
                dialog.dismiss();
            }
*/

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");


                if (status.equals("true")) {

                    JSONObject object=jo.getJSONObject("data");

                    apikey=object.getString("api_key");
                    email=object.getString("email");
                    String userid=object.getString("user_id");
                    String username=object.getString("username");



                    SharedPreferences putLoginData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor putLoginEditor = putLoginData.edit();
                    putLoginEditor.putString("get_apikey_from_login",apikey);
                    putLoginEditor.putString("get_email_from_login",email);
                    putLoginEditor.putString("get_userid_from_login",userid);
                    putLoginEditor.putString("get_username_from_login",username) ;
                    putLoginEditor.putString("login_status","true") ;
                    putLoginEditor.commit();

                    push_token = SharedPrefManager.getInstance(LoginActivity.this).getDeviceToken();
                    Log.e("tag","PRINT TOKEN"+push_token);
                    //if token is not null
                    if (push_token != null) {
                        //displaying the token
                        //Toast.makeText(getApplicationContext(),push_token,Toast.LENGTH_LONG).show();
                    } else {
                        //if token is null that means something wrong
                        //Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                    }
                    new SendToken_Task().execute();
                    Intent verify=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(verify);
                    finish();






                } else {
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }




    //Login API CALL: ---------------------------------------------------------------------->
    public class SendToken_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Loading..., please wait.");
            dialog.show();*/

            // av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email",email);
                jsonObject.accumulate("token",push_token);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_PUSHNOTIFICATION,apikey,json);



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
                    String msg=jo.getString("message");
                    Log.e("tag","PRINT MSG"+msg);




                } else {
                    String msg=jo.getString("message");
                    Log.e("tag","PRINT MSG"+msg);
                    //Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }


}



