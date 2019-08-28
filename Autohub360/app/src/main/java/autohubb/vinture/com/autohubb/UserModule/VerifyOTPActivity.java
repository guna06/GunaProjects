package autohubb.vinture.com.autohubb.UserModule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;
import es.dmoral.toasty.Toasty;


public class VerifyOTPActivity extends Activity {
    EditText edt1,edt2,edt3,edt4;
    TextView verify_tv,resend_otp_tv;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String get_mail_from_reg,get_otp_from_reg,otp_merge,get_name_from_reg;
    AVLoadingIndicatorView av_loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_activity);

        av_loader = (AVLoadingIndicatorView) findViewById(R.id.avi_otp);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(VerifyOTPActivity.this, v1);


        sharedlogin = PreferenceManager.getDefaultSharedPreferences(VerifyOTPActivity.this);
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("Register_EMAIL", "");
       // get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_name_from_reg=sharedlogin.getString("Register_NAME","");

        edt1=findViewById(R.id.edt1);
        edt2=findViewById(R.id.edt2);
        edt3=findViewById(R.id.edt3);
        edt4=findViewById(R.id.edt4);
        verify_tv=findViewById(R.id.verify_tv);
        resend_otp_tv=findViewById(R.id.resend_otp_tv);


        edt1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(edt1.getText().toString().length()==1)     //size as per your requirement
                {
                    edt2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });


        edt2.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(edt2.getText().toString().length()==1)     //size as per your requirement
                {
                    edt3.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });


        edt3.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(edt3.getText().toString().length()==1)     //size as per your requirement
                {
                    edt4.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        edt4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(edt4.getText().toString().length()==1)     //size as per your requirement
                {
                    hideKeyboard();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });


        resend_otp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.Operations.isOnline(VerifyOTPActivity.this)) {
                    new ResendOTP_Async().execute();
                }
                else
                {
                    Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
                }

            }
        });




        verify_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s1=edt1.getText().toString();
                String s2=edt2.getText().toString();
                String s3=edt3.getText().toString();
                String s4=edt4.getText().toString();
                otp_merge=s1+s2+s3+s4;

                if (Util.Operations.isOnline(VerifyOTPActivity.this)) {
                    new OTP_Verify_Async().execute();

                }
                else
                {
                    Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
                }




                /*Intent dashboard=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(dashboard);
                finish();*/
            }
        });

    }

    private void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) VerifyOTPActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view =  VerifyOTPActivity.this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View( VerifyOTPActivity.this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //OTP Verification API CALL: ---------------------------------------------------------------------->
    public class OTP_Verify_Async extends AsyncTask<String, Void, String> {



        protected void onPreExecute() {
            super.onPreExecute();

            av_loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email",get_mail_from_reg);
                jsonObject.accumulate("otp",otp_merge);

                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_OTP_VERIFICATION,json);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            /*if (dialog.isShowing()) {
                dialog.dismiss();
            }
*/
            av_loader.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    String msg=jo.getString("message");

                    Toasty.success(getApplicationContext(),msg,Toasty.LENGTH_SHORT).show();
                       Intent j=new Intent(getApplicationContext(),LoginActivity.class);
                       startActivity(j);
                       finish();

                } else if(status.equals("false")){
                    String msg=jo.getString("message");
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }

        }
    }





    //RESEND OTP Verification API CALL: ---------------------------------------------------------------------->
    public class ResendOTP_Async extends AsyncTask<String, Void, String> {


        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email",get_mail_from_reg);

                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_RESENDOTP_VERIFICATION,json);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            /*if (dialog.isShowing()) {
                dialog.dismiss();
            }
*/
            av_loader.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");


                if (status.equals("true")) {
                    String msg=jo.getString("message");

                    Toasty.success(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    edt1.setText("");
                    edt2.setText("");
                    edt3.setText("");
                    edt4.setText("");
                } else {
                    String msg=jo.getString("message");
                    Toasty.error(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }

        }
    }
}



