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
import com.wang.avi.AVLoadingIndicatorView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.Shop.BankAdapter1;
import autohubb.vinture.com.autohubb.activity.MainActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;
import es.dmoral.toasty.Toasty;

/**
 * Created by Salman on 11-04-2019.
 */

public class OrderPageActivity extends Activity {
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;

    RadioGroup shipping_method_group;
    String status,orderId,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    LinearLayout back_lv,transaction;
    RecyclerView recyclerView_requst,recyclerView_banklist;
    OrderListAdapter orderListAdapter;
    OrderSingleItemListPojo singleItemListPojo;
    private List<OrderSingleItemListPojo> singleItemListPojos=new ArrayList<>();
    public static AVLoadingIndicatorView av_loader;
    LinearLayout accept_decline_linear,noparts_found,shipping_method_linear,choose_address_linear,display_address_linear,cancle_linear,accept_linear;
    TextView submit_payment,show_address,selelct_address,quotation_no,quotation_status,total_val,shipping_head,shipping_Address,cancel_btn,accept_btn;
    Bundle extras;
    LinearLayoutManager manager;
    String quatationId,status_condition,selected_method_id,method_price,finalMethodId,finalMethodPrize,finalAddressId;
    List<String> methodItem=new ArrayList<>();
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    Intent i;
    BankPojo bankPojo;
    BankAdapter1 bankAdapter;
    public static String bank_id;
    private List<BankPojo> bankPojoList=new ArrayList<>();
    EditText transactionId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_page_activity);
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(OrderPageActivity.this, v1);
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
        orderId=sharedlogin.getString("get_order_ID","");

        i=getIntent();
        status_condition=i.getStringExtra("order_status");
        Log.e("tag","oooooooooooooooooo----->"+status_condition);
        shipping_method_group=findViewById(R.id.shipping_method_group);
        back_lv=findViewById(R.id.back_lv);
        recyclerView_banklist=findViewById(R.id.recyclerView_banklist);
        quotation_no=findViewById(R.id.quotation_no);
        submit_payment=findViewById(R.id.submit_payment);
        transaction=findViewById(R.id.transaction);
        recyclerView_requst=findViewById(R.id.recyclerView_requst);
        transactionId=findViewById(R.id.transactionId);

        av_loader =findViewById(R.id.avi);
        total_val=findViewById(R.id.total_val);

        Intent i=getIntent();
        transaction.setVisibility(View.GONE);



        noparts_found=findViewById(R.id.noparts_found);
        noparts_found.setVisibility(View.GONE);



        manager = new LinearLayoutManager(getApplicationContext());
        recyclerView_requst.setLayoutManager(manager);
        recyclerView_requst.addItemDecoration(new DividerItemDecoration(this.getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView_requst.setHasFixedSize(true);
        orderListAdapter = new OrderListAdapter(OrderPageActivity.this, singleItemListPojos);
        recyclerView_requst.setAdapter(orderListAdapter);



        manager = new LinearLayoutManager(getApplicationContext());
        recyclerView_banklist.setLayoutManager(manager);
        recyclerView_banklist.addItemDecoration(new DividerItemDecoration(this.getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView_banklist.setHasFixedSize(true);
        bankAdapter = new BankAdapter1(OrderPageActivity.this, bankPojoList);
        recyclerView_banklist.setAdapter(bankAdapter);


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

        if(status_condition.equals("Not Paid"))
        {
            transaction.setVisibility(View.VISIBLE);
            new BankList_Task().execute();
        }
        else
        {
            transaction.setVisibility(View.GONE);
        }



        if (Util.Operations.isOnline(OrderPageActivity.this)) {

            new MyRequest_Task().execute();

        }
        else
        {
            Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
        }


























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
            av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_DETAIL_ORDERS_ITEM+orderId,get_apikey_from_login);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            av_loader.setVisibility(View.GONE);

            try{

                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                if (status.equals("true")) {
                    String message=jo.getString("message");
                    JSONObject data_object=jo.getJSONObject("data");
                    JSONArray car_array=data_object.getJSONArray("orderItems");
                    Log.e("tag","Printing Length---------->"+car_array.length());
                    if(car_array.length()==0)
                    {
                        noparts_found.setVisibility(View.VISIBLE);
                    }
                    else {
                        singleItemListPojos.clear();
                        noparts_found.setVisibility(View.GONE);
                        for(int i=0;i<car_array.length();i++)
                        {
                            try {
                                singleItemListPojo = new OrderSingleItemListPojo();
                                JSONObject jsonObject = car_array.getJSONObject(i);
                                //singleItemListPojo.itemId=jsonObject.getString("itemId");
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

                    singleItemListPojo.orderId=data_object.getString("orderId");
                    String quoteNumber=data_object.getString("orderNumber");
                    Log.e("tag","gunakkkkkkkkkk------------>"+quoteNumber);
                    quotation_no.setText(quoteNumber);
                    total_val.setText("₦ "+data_object.getString("grandTotal"));
                    quotation_status.setText(data_object.getString("orderStatus"));

                }
                else
                {

                }

            }
            catch (Exception e)
            {

            }

            // Setup and Handover data to recyclerview
            recyclerView_requst.setAdapter(orderListAdapter);


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
                    Intent i=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {

                }

            }
            catch (Exception e)
            {

            }
/*
            // Setup and Handover data to recyclerview
            recyclerView_requst.setAdapter(acceptListAdapter);*/


        }

    }



}
