package autohubb.vinture.com.autohubb.user;

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
import android.view.View;
import android.view.ViewGroup;
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
import autohubb.vinture.com.autohubb.MyOrdersModule.ShippingAddressActivity;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;

public class ShippingAddress extends Activity {

    EditText fname_et,lname_et,address1_et,address2_et,state_et,city_et,phone_et,email_et;
    Spinner country_spn;
    ArrayList<String> country_arraylist=new ArrayList<>();
    CustomAdapterArrayList countryAdapter;
    String str_country;
    Typeface helvetica;
    TextView submit_tv;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    AVLoadingIndicatorView avi;
    LinearLayout back_lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_activity);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ShippingAddress.this, v1);
        helvetica = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");


        fname_et=findViewById(R.id.fname_et);
        lname_et=findViewById(R.id.lname_et);
        address1_et=findViewById(R.id.address1_et);
        address2_et=findViewById(R.id.address2_et);
        state_et=findViewById(R.id.state_et);
        city_et=findViewById(R.id.city_et);
        phone_et=findViewById(R.id.phone_et);
        email_et=findViewById(R.id.email_et);
        country_spn=findViewById(R.id.country_spn);
        submit_tv=findViewById(R.id.submit_tv);
        avi=findViewById(R.id.avi);
        back_lv=findViewById(R.id.back_lv);


        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");



        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ShippingAddressActivity.class);
                startActivity(i);
                finish();
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
                tv.setTextSize(20);
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
                tv.setTextSize(20);
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
                        if(email_str.length()>0) {

                            if(!TextUtils.isEmpty(phone_et.getText()))
                            {
                                if(!TextUtils.isEmpty(address1_et.getText()))
                                {
                                    if(!TextUtils.isEmpty(city_et.getText()))
                                    {
                                        if(!TextUtils.isEmpty(state_et.getText()))
                                        {
                                            if(!str_country.equals("Select Country"))
                                            {
                                               new shippingAddressTask().execute();
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(),"Select Country",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        else
                                        {
                                            state_et.requestFocus();
                                            state_et.setError( "State is Required!" );
                                        }
                                    }
                                    else
                                    {
                                        city_et.requestFocus();
                                        city_et.setError( "City is Required!" );
                                    }
                                }
                                else
                                {
                                    address1_et.requestFocus();
                                    address1_et.setError( "Address is Required!" );
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



    //Create Shipping Address API CALL: ---------------------------------------------------------------------->
    public class shippingAddressTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();

            avi.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("userId",get_userid_from_login);
                jsonObject.accumulate("firstName",fname_et.getText().toString());
                jsonObject.accumulate("lastName",lname_et.getText().toString());
                jsonObject.accumulate("addressLine1",address1_et.getText().toString());
                jsonObject.accumulate("addressLine2",address2_et.getText().toString());
                jsonObject.accumulate("city",city_et.getText().toString());
                jsonObject.accumulate("state",state_et.getText().toString());
                jsonObject.accumulate("country",str_country);
                jsonObject.accumulate("postCode","");
                jsonObject.accumulate("phone",phone_et.getText().toString());
                jsonObject.accumulate("email",email_et.getText().toString());
                //jsonObject.accumulate("ref_code",agent_et.getText().toString());

                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_ADD_SHIPPING_DETAILS,get_apikey_from_login,json);

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
                String status = jo.getString("status");

                if (status.equals("true")) {

                    String message=jo.getString("message");

                    JSONObject object=jo.getJSONObject("data");
                    String shiiping_id=object.getString("shippingAddressId");
                    Log.e("tag","PRinting-------------->"+shiiping_id);

                    Intent i=new Intent(getApplicationContext(),ShippingAddressActivity.class);
                    startActivity(i);
                    finish();


                } else {

                    String message = jo.getString("msg");
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),ShippingAddressActivity.class);
        startActivity(i);
        finish();
    }
}
