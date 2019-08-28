package autohubb.vinture.com.autohubb.UserModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.ArrayList;

import autohubb.vinture.com.autohubb.Adapter.CustomAdapterArrayList;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.splash.SplashActivity;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;
import es.dmoral.toasty.Toasty;


public class RegisterActivity extends Activity {
    ArrayList<String> country_arraylist=new ArrayList<>();
    CustomAdapterArrayList countryAdapter;
    Spinner country_spn;
    Typeface helvetica;
    TextView submit_tv;
    EditText fname_et,lname_et,email_et,password_et,agent_et,phone_et;
    String str_country;
    LinearLayout back_lv;
    String verification_msg,typed_email,store_firstname,store_lastname,store_email,store_password,store_otp,store_mobile,store_country,store_reffcode;
    AVLoadingIndicatorView av_loader;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(RegisterActivity.this, v1);
        helvetica = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");

        country_spn=findViewById(R.id.country_spn);
        submit_tv=findViewById(R.id.submit_tv);
        fname_et=findViewById(R.id.fname_et);
        lname_et=findViewById(R.id.lname_et);
        email_et=findViewById(R.id.email_et);
        password_et=findViewById(R.id.t5);
        agent_et=findViewById(R.id.agent_et);
        phone_et=findViewById(R.id.phone_et);
        back_lv=findViewById(R.id.back_lv);
        av_loader = (AVLoadingIndicatorView) findViewById(R.id.avi);


        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),SplashActivity.class);
                startActivity(back);
                finish();
            }
        });


        email_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Log.e("tag","inside email");
                typed_email=email_et.getText().toString();
                hideKeyboard();

                if (Util.Operations.isOnline(RegisterActivity.this)) {
                    new EmailValidation_Task().execute();

                }
                else
                {
                    Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
                }

                return false;
            }
        });


        country_arraylist.add("Select Country");
        country_arraylist.add("Nigeria");
        country_arraylist.add("Ghana");

        countryAdapter = new CustomAdapterArrayList(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, country_arraylist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(helvetica);
                tv.setTextSize(15);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.spin_text));
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(15);
                tv.setTypeface(helvetica);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.greyblack));
                } else {

                    tv.setTextColor(getResources().getColor(R.color.textcolor));
                }
                return view;
            }
        };
        country_spn.setAdapter(countryAdapter);
        country_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_country= (String) adapterView.getItemAtPosition(i);

                Log.e("tag","print Country------------>"+str_country);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email_str=email_et.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(!TextUtils.isEmpty(fname_et.getText()))
                {
                    if(!TextUtils.isEmpty(lname_et.getText()))
                    {
                        if(email_str.length()>0)
                        {
                            if(email_str.matches(emailPattern))
                            {
                                if(!TextUtils.isEmpty(password_et.getText()))
                                {
                                    if(!TextUtils.isEmpty(phone_et.getText()))
                                    {
                                        if(!str_country.equals("Select Country"))
                                        {
                                                try {

                                                    if(verification_msg!=null) {

                                                        if(verification_msg.equals("Email not found"))
                                                        {

                                                            if (Util.Operations.isOnline(RegisterActivity.this)) {
                                                                new RegisterAsync_Task().execute();
                                                            }
                                                            else
                                                            {
                                                                Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            if (Util.Operations.isOnline(RegisterActivity.this)) {
                                                                new EmailValidation_Task().execute();
                                                            }
                                                            else
                                                            {
                                                                Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        if (Util.Operations.isOnline(RegisterActivity.this)) {
                                                            new EmailValidation_Task().execute();
                                                        }
                                                        else
                                                        {
                                                            Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
                                                        }
                                                    }

                                                }catch (Exception e)
                                                {

                                                }
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"Select Country",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else
                                    {
                                        phone_et.requestFocus();
                                        phone_et.setError( "Phone No is Required!" );
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
                                email_et.setError( "please enter Valid Email!" );
                            }
                        }
                        else
                        {
                            email_et.requestFocus();
                            email_et.setError( "Email is Required!" );
                        }

                    }
                    else
                    {
                        lname_et.requestFocus();
                        lname_et.setError( "Last Name is Required!" );
                    }
                }
                else
                {
                    fname_et.requestFocus();
                    fname_et.setError( "First Name is Required!" );
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



    //EMAIL VERIFICATION API CALL: ---------------------------------------------------------------------->
    public class EmailValidation_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email",typed_email);

                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_EMAIL_VERIFICATION,json);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

           /* if (dialog.isShowing()) {
                dialog.dismiss();
            }*/

            av_loader.setVisibility(View.GONE);
            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {

                    verification_msg = jo.getString("msg");



                } else if(status.equals("false")){
                    verification_msg = jo.getString("msg");
                    Toasty.warning(getApplicationContext(), "Already have this email", Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
            }

        }
    }


    //Register API CALL: ---------------------------------------------------------------------->
    public class RegisterAsync_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("Loading..., please wait.");
            dialog.show();*/
            av_loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("first_name",fname_et.getText().toString());
                jsonObject.accumulate("last_name",lname_et.getText().toString());
                jsonObject.accumulate("email",email_et.getText().toString());
                jsonObject.accumulate("password",password_et.getText().toString());
                jsonObject.accumulate("phone",phone_et.getText().toString());
                jsonObject.accumulate("country",str_country);
                jsonObject.accumulate("ref_code",agent_et.getText().toString());

                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_REGISTER,json);

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
            }*/
            av_loader.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {

                    String msg=jo.getString("message");

                    JSONObject object=jo.getJSONObject("data");

                    store_firstname=object.getString("username");
                   // store_lastname=object.getString("last_name");
                    store_email=object.getString("email");
                   /* store_password=object.getString("password");
                    store_otp=object.getString("otp");
                    store_mobile=object.getString("phone");
                    str_country=object.getString("country");
                    store_reffcode=object.getString("ref_code");
*/


                    SharedPreferences putLoginData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor putLoginEditor = putLoginData.edit();
                    putLoginEditor.putString("Register_NAME",store_firstname);
                   // putLoginEditor.putString("get_lname_from_reg",store_lastname);
                   // putLoginEditor.putString("get_otp_from_reg",store_otp);
                    putLoginEditor.putString("Register_EMAIL",store_email) ;
                    putLoginEditor.commit();



                    if(msg.equals("User Registered Successfully! Gohead..."))
                    {
                        Intent verify=new Intent(getApplicationContext(),VerifyOTPActivity.class);
                        startActivity(verify);
                        finish();
                    }








                } else {

                    String message = jo.getString("msg");
                    Toasty.error(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }


    private void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) RegisterActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view =  RegisterActivity.this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View( RegisterActivity.this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}



