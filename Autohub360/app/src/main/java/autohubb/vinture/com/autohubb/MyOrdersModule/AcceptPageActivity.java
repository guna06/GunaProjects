package autohubb.vinture.com.autohubb.MyOrdersModule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.model.ShippingMethodPojo;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;
import es.dmoral.toasty.Toasty;

/**
 * Created by Salman on 26-03-2019.
 */

public class AcceptPageActivity extends Activity{

    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    RadioGroup shipping_method_group;
    String status,quatationId,getAddressId,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    LinearLayout back_lv;
    RecyclerView recyclerView_requst,recyclerView_banklist;
    AcceptListAdapter acceptListAdapter;
    SingleItemListPojo singleItemListPojo;
    private List<SingleItemListPojo> singleItemListPojos=new ArrayList<>();

    BankPojo bankPojo;
    BankAdapter bankAdapter;
    private List<BankPojo> bankPojoList=new ArrayList<>();
    public static AVLoadingIndicatorView av_loader;
    public static String bank_id;
    LinearLayout noparts_found;
    TextView submit_payment,quotation_no,quotation_status,total_val,cancel_btn;
    LinearLayoutManager manager;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    private ShimmerFrameLayout mShimmerViewContainer;
    EditText transactionId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_page);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(AcceptPageActivity.this, v1);
        Typeface bold_text = Typeface.createFromAsset(getAssets(), "fonts/heleveticalBold.TTF");


        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = sharedPrefces.edit();

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");
        quatationId=sharedlogin.getString("get_quotation_ID","");
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        shipping_method_group=findViewById(R.id.shipping_method_group);
        back_lv=findViewById(R.id.back_lv);
        transactionId=findViewById(R.id.transactionId);
        submit_payment=findViewById(R.id.submit_payment);
        quotation_no=findViewById(R.id.quotation_no);


        recyclerView_requst=findViewById(R.id.recyclerView_requst);
        recyclerView_banklist=findViewById(R.id.recyclerView_banklist);

        av_loader =findViewById(R.id.avi);
        total_val=findViewById(R.id.total_val);

        Intent i=getIntent();


        noparts_found=findViewById(R.id.noparts_found);
        noparts_found.setVisibility(View.GONE);


        manager = new LinearLayoutManager(getApplicationContext());
        recyclerView_requst.setLayoutManager(manager);
        recyclerView_requst.addItemDecoration(new DividerItemDecoration(this.getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView_requst.setHasFixedSize(true);
        acceptListAdapter = new AcceptListAdapter(AcceptPageActivity.this, singleItemListPojos);
        recyclerView_requst.setAdapter(acceptListAdapter);


        manager = new LinearLayoutManager(getApplicationContext());
        recyclerView_banklist.setLayoutManager(manager);
        recyclerView_banklist.addItemDecoration(new DividerItemDecoration(this.getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView_banklist.setHasFixedSize(true);
        bankAdapter = new BankAdapter(AcceptPageActivity.this, bankPojoList);
        recyclerView_banklist.setAdapter(bankAdapter);



        if (Util.Operations.isOnline(AcceptPageActivity.this)) {

            new MyRequest_Task().execute();
            new BankList_Task().execute();
        }
        else
        {
            Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
        }







        submit_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(transactionId.getText()))
                {
                   new Payment_Task().execute();
                }else
                {
                    transactionId.requestFocus();
                    transactionId.setError( "Transaction Id is Required!" );
                }

            }
        });


        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    //MyList API CALL: ---------------------------------------------------------------------->
    public class MyRequest_Task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
           // av_loader.setVisibility(View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_DETAIL_QUOTES_ITEM+quatationId,get_apikey_from_login);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //av_loader.setVisibility(View.GONE);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);

            try{

                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                if (status.equals("true")) {
                    String message=jo.getString("message");
                    JSONObject data_object=jo.getJSONObject("data");
                            JSONArray car_array=data_object.getJSONArray("quoteItems");
                            Log.e("tag","Printing Length---------->"+car_array.length());
                            if(car_array.length()==0)
                            {
                                noparts_found.setVisibility(View.VISIBLE);
                            }
                            else {
                                noparts_found.setVisibility(View.GONE);
                                for(int i=0;i<car_array.length();i++)
                                {
                                    try {
                                        singleItemListPojo = new SingleItemListPojo();
                                        JSONObject jsonObject = car_array.getJSONObject(i);
                                        singleItemListPojo.itemId=jsonObject.getString("itemId");
                                        singleItemListPojo.itemPrice=jsonObject.getString("itemPrice");
                                        singleItemListPojo.comment=jsonObject.getString("comment");
                                        singleItemListPojo.currentMileage=jsonObject.getString("currentMileage");
                                        singleItemListPojo.vehicleId=jsonObject.getString("vehicleId");
                                        singleItemListPojo.itemConditionId=jsonObject.getString("itemConditionId");
                                        singleItemListPojo.itemConditionName=jsonObject.getString("itemConditionName");


                                        singleItemListPojo.itemName=jsonObject.getString("itemName");
                                        singleItemListPojo.vehicleVin=jsonObject.getString("vehicleVin");
                                        singleItemListPojo.vehicleYear=jsonObject.getString("vehicleYear");
                                        singleItemListPojo.vehicleMake=jsonObject.getString("vehicleMake");
                                        singleItemListPojo.vehicleModel=jsonObject.getString("vehicleModel");

                                        singleItemListPojos.add(singleItemListPojo);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                    singleItemListPojo.quoteId=data_object.getString("quoteId");
                            String quoteNumber=data_object.getString("quoteNumber");
                            Log.e("tag","gunakkkkkkkkkk------------>"+quoteNumber);
                    quotation_no.setText(data_object.getString("quoteNumber"));
                    total_val.setText("₦ "+data_object.getString("itemTotal"));
                    quotation_status.setText(data_object.getString("quotStatus"));

                }
                else
                {

                }

            }
            catch (Exception e)
            {

            }

            // Setup and Handover data to recyclerview
            recyclerView_requst.setAdapter(acceptListAdapter);


        }

    }


    //MyBank List API CALL: ---------------------------------------------------------------------->
    public class BankList_Task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            // av_loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_QUOTE_BANK_LIST,get_apikey_from_login);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //av_loader.setVisibility(View.GONE);

            try{

                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                if (status.equals("true")) {
                    String message=jo.getString("message");
                    JSONObject data_object=jo.getJSONObject("data");
                    JSONArray banks_array=data_object.getJSONArray("banks");
                    Log.e("tag","Printing Length---------->"+banks_array.length());
                    if(banks_array.length()==0)
                    {
                        noparts_found.setVisibility(View.VISIBLE);
                    }
                    else {
                        noparts_found.setVisibility(View.GONE);
                        for(int i=0;i<banks_array.length();i++)
                        {
                            try {
                                bankPojo = new BankPojo();
                                JSONObject jsonObject = banks_array.getJSONObject(i);
                                bank_id=jsonObject.getString("id");
                                bankPojo.id=jsonObject.getString("id");
                                bankPojo.name=jsonObject.getString("name");
                                bankPojo.accountName=jsonObject.getString("accountName");
                                bankPojo.accountNumber=jsonObject.getString("accountNumber");
                                bankPojo.sortCode=jsonObject.getString("sortCode");
                                bankPojo.branch=jsonObject.getString("branch");

                                bankPojoList.add(bankPojo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }



                }
                else
                {

                }

            }
            catch (Exception e)
            {

            }

            // Setup and Handover data to recyclerview
            recyclerView_banklist.setAdapter(bankAdapter);


        }

    }



    //Payment API CALL: ---------------------------------------------------------------------->
    public class Payment_Task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            // av_loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("orderId",quatationId);
                jsonObject.accumulate("bankId",bank_id);
                jsonObject.accumulate("txnId",transactionId.getText().toString().trim());
                jsonObject.accumulate("status","underVerification");
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_QUOTE_PAYMENT,get_apikey_from_login,json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //av_loader.setVisibility(View.GONE);


            try{

                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                if (status.equals("true"))
                {
                    finish();
                }
                else
                {

                }

            }
            catch (Exception e)
            {

            }

           /* // Setup and Handover data to recyclerview
            recyclerView_requst.setAdapter(acceptListAdapter);*/


        }

    }


}
