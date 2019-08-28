package autohubb.vinture.com.autohubb.VehicleModule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;

public class UpdateCompanyActivity extends Activity {
    EditText com_name,email,phone,address1,address2,contact_name,contact_ph;
    TextView submit_company,head_tv,head_top;
    AVLoadingIndicatorView av_loader,dialog_avi;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    LinearLayout back_lv;
    Typeface lato_head;
    Bundle bundle;
    String companyId,companyName,companyEmail,str_phone,str_address1,str_address2,contactname,contact_phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_company_create);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(UpdateCompanyActivity.this, v1);

        lato_head = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/heleveticalBold.TTF");
        com_name=findViewById(R.id.com_name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        address1=findViewById(R.id.address1);
        address2=findViewById(R.id.address2);
        contact_name=findViewById(R.id.contact_name);
        contact_ph=findViewById(R.id.contact_ph);
        submit_company=findViewById(R.id.submit_company);
        dialog_avi=findViewById(R.id.dialog_avi);
        back_lv=findViewById(R.id.back_lv);
        head_tv=findViewById(R.id.head_tv);
        head_tv.setTypeface(lato_head);
        head_top=findViewById(R.id.head_top);

        submit_company.setText("Update");
        head_top.setText("Update Company");

        bundle= getIntent().getExtras();
        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");


        companyId = bundle.getString("Company_Id");
         companyName = bundle.getString("Company_Name");
         companyEmail = bundle.getString("Company_Email");
         str_phone = bundle.getString("Company_Phone");
         str_address1 = bundle.getString("Company_Address1");
         str_address2 = bundle.getString("Company_Address2");
         contactname = bundle.getString("Contact_Name");
         contact_phone = bundle.getString("Contact_ph");



        com_name.setText(companyName);
        email.setText(companyEmail);
        phone.setText(str_phone);
        address1.setText(str_address1);
        address2.setText(str_address2);
        contact_name.setText(contactname);
        contact_ph.setText(contact_phone);



        submit_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new upload_company().execute();
            }
        });



        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateCompanyActivity.this, CommercialCarListActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });
    }




    //UPLAOD COMPANY API CALL: ---------------------------------------------------------------------->
    private class upload_company extends AsyncTask<String,String,String> {

        protected void onPreExecute() {
            super.onPreExecute();
            dialog_avi.setVisibility(View.VISIBLE);
        }




        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            try {

                JSONObject commercial_data = new JSONObject();
                commercial_data.put("userId", get_userid_from_login);
                commercial_data.put("companyName", com_name.getText().toString());
                commercial_data.put("email", email.getText().toString());
                commercial_data.put("phone", phone.getText().toString());
                commercial_data.put("address1", address1.getText().toString());
                commercial_data.put("address2", address2.getText().toString());
                json = commercial_data.toString();

                return jsonStr = HttpUtils.makeRequestTokenPut(Config.WEB_URL_UPDATE_COMPANY_DETAILS+companyId,get_apikey_from_login,json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog_avi.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");


                if (status.equals("true")) {
                    String message=jo.getString("message");



                    JSONArray array=jo.getJSONArray("data");
                    startActivity(new Intent(UpdateCompanyActivity.this, CommercialCarListActivity.class));
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();






                } else {
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }






        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UpdateCompanyActivity.this, CommercialCarListActivity.class));
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        finish();
    }
}
