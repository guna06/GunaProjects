package autohubb.vinture.com.autohubb.MyOrdersModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import autohubb.vinture.com.autohubb.activity.MainActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.model.ShippingMethodPojo;
import autohubb.vinture.com.autohubb.user.ShippingAddress;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;
import es.dmoral.toasty.Toasty;

/**
 * Created by Salman on 26-03-2019.
 */

public class PriceQuotedPageActivity extends Activity{
    ShippingMethodPojo shippingMethodPojo;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    private List<ShippingMethodPojo> shippingMethodPojoArrayList=new ArrayList<>();
    RadioGroup shipping_method_group;
    String navdashboard,selected_address,status,quatationId,getAddressId,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    LinearLayout back_lv;
    RecyclerView recyclerView_requst;
    PriceQuotedListAdapter singleItemListAdapter;
    SingleItemListPojo singleItemListPojo;
    private List<SingleItemListPojo> singleItemListPojos=new ArrayList<>();
    public static AVLoadingIndicatorView av_loader;
    LinearLayout add_address,total_layout,head_layout,accept_decline_linear,noparts_found,shipping_method_linear,choose_address_linear,display_address_linear,cancle_linear,accept_linear;
    TextView tot_price,continue_accept,show_address,selelct_address,quotation_no,quotation_status,total_val,shipping_head,shipping_Address,cancel_btn,accept_btn;
    Bundle extras;
    LinearLayoutManager manager;
    String status_condition,selected_method_id,method_price,finalMethodId,finalMethodPrize,finalAddressId;
    List<String> methodItem=new ArrayList<>();
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pricequoted_page);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(PriceQuotedPageActivity.this, v1);
        Typeface bold_text = Typeface.createFromAsset(getAssets(), "fonts/heleveticalBold.TTF");


        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = sharedPrefces.edit();

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg = sharedlogin.getString("get_otp_from_reg", "");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login = sharedlogin.getString("get_email_from_login", "");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login = sharedlogin.getString("get_username_from_login", "");
        quatationId = sharedlogin.getString("get_quotation_ID", "");
        status = sharedlogin.getString("get_quotation_STATUS", "");
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        status_condition = sharedlogin.getString("status_condition", "");

        shipping_method_group = findViewById(R.id.shipping_method_group);
        back_lv = findViewById(R.id.back_lv);
        shipping_head = findViewById(R.id.shipping_head);
        quotation_no = findViewById(R.id.quotation_no);
        cancel_btn = findViewById(R.id.cancel_btn);
        accept_btn = findViewById(R.id.accept_btn);
        tot_price = findViewById(R.id.tot_price);
        recyclerView_requst = findViewById(R.id.recyclerView_requst);
        cancle_linear = findViewById(R.id.cancle_linear);
        add_address=findViewById(R.id.add_address);
        accept_linear = findViewById(R.id.accept_linear);
        shipping_method_group.removeAllViews();
        av_loader = findViewById(R.id.avi);
        total_val = findViewById(R.id.total_val);
        selelct_address = findViewById(R.id.selelct_address);
        show_address = findViewById(R.id.show_address);
        continue_accept = findViewById(R.id.continue_accept);
        shipping_Address = findViewById(R.id.shipping_Address);
        quotation_status = findViewById(R.id.quotation_status);
        accept_decline_linear = findViewById(R.id.accept_decline_linear);
        noparts_found = findViewById(R.id.noparts_found);
        noparts_found.setVisibility(View.GONE);
        shipping_method_linear = findViewById(R.id.shipping_method_linear);
        choose_address_linear = findViewById(R.id.choose_address_linear);
        display_address_linear = findViewById(R.id.display_address_linear);
        head_layout = findViewById(R.id.head_layout);
        total_layout = findViewById(R.id.total_layout);

        head_layout.setVisibility(View.GONE);
        total_layout.setVisibility(View.GONE);
        shipping_method_linear.setVisibility(View.GONE);


        Intent i = getIntent();
        extras = getIntent().getExtras();


        shipping_method_linear.setVisibility(View.VISIBLE);
        choose_address_linear.setVisibility(View.VISIBLE);
        display_address_linear.setVisibility(View.GONE);
        accept_decline_linear.setVisibility(View.VISIBLE);


        Intent intent = getIntent();
        //quatationId = intent.getStringExtra("QuatationId");
        shipping_Address.setTypeface(bold_text);
        quotation_no.setTypeface(bold_text);
        shipping_head.setTypeface(bold_text);

        manager = new LinearLayoutManager(getApplicationContext());
        recyclerView_requst.setLayoutManager(manager);
        recyclerView_requst.addItemDecoration(new DividerItemDecoration(this.getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView_requst.setHasFixedSize(true);
        singleItemListAdapter = new PriceQuotedListAdapter(PriceQuotedPageActivity.this, singleItemListPojos);
        recyclerView_requst.setAdapter(singleItemListAdapter);


        shipping_method_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selected_method_id = shippingMethodPojoArrayList.get(i).shipping_method_id;
                method_price = shippingMethodPojoArrayList.get(i).cost;
                methodItem.add(selected_method_id);
            }
        });


        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), ShippingAddress.class);
                startActivity(i);
                finish();
            }
        });


        if (status_condition == "" || status_condition == null) {

            shipping_method_linear.setVisibility(View.VISIBLE);
            choose_address_linear.setVisibility(View.GONE);
            display_address_linear.setVisibility(View.GONE);
            if (Util.Operations.isOnline(PriceQuotedPageActivity.this)) {

                new MyRequest_Task().execute();
                new CallShippingMethod_Task().execute();
            } else {
                Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
            }


        } else if (status_condition.equals("address_true")) {

            accept_decline_linear.setVisibility(View.GONE);
            selected_address = i.getStringExtra("Address");
            shipping_method_linear.setVisibility(View.VISIBLE);
            choose_address_linear.setVisibility(View.GONE);
            display_address_linear.setVisibility(View.VISIBLE);
            show_address.setText(selected_address);
            tot_price.setText("0");
            if (Util.Operations.isOnline(PriceQuotedPageActivity.this)) {
                // new CallShippingMethod_Task().execute();
                new MyRequest_Task().execute();

            } else {
                Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
            }


        } else if(status_condition.equals("priceAccepted")){
            if (Util.Operations.isOnline(PriceQuotedPageActivity.this)) {
                //  new CallShippingMethod_Task().execute();
                new MyRequest_Task().execute();
            } else {
                Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
            }
            Toasty.info(getApplicationContext(), "Please Select Address", Toasty.LENGTH_SHORT).show();
            shipping_method_linear.setVisibility(View.GONE);
            choose_address_linear.setVisibility(View.VISIBLE);
            display_address_linear.setVisibility(View.GONE);


        }







        else  {

            Toasty.info(getApplicationContext(),"Please Select Address",Toasty.LENGTH_SHORT).show();
            accept_decline_linear.setVisibility(View.GONE);
            shipping_method_linear.setVisibility(View.VISIBLE);
            choose_address_linear.setVisibility(View.VISIBLE);
            display_address_linear.setVisibility(View.GONE);

            if (Util.Operations.isOnline(PriceQuotedPageActivity.this)) {
                //  new CallShippingMethod_Task().execute();
                new MyRequest_Task().execute();
            }
            else
            {
                Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
            }
        }















        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                accept_linear.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.accept_bg_fill));
                accept_btn.setTextColor(Color.parseColor("#FFFFFF"));

                if(methodItem.size()>0)
                {
                    SharedPreferences putLoginData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor putLoginEditor = putLoginData.edit();
                    putLoginEditor.putString("ShippingMethodId",selected_method_id);
                    putLoginEditor.putString("price",method_price);
                    putLoginEditor.putString("status_condition","priceAccepted");
                    putLoginEditor.commit();


                    accept_decline_linear.setVisibility(View.GONE);
                    shipping_method_linear.setVisibility(View.GONE);
                    choose_address_linear.setVisibility(View.VISIBLE);
                    display_address_linear.setVisibility(View.GONE);




                }
                else
                {
                    Toasty.info(getApplicationContext(),"Please Select Shipping Method",Toasty.LENGTH_LONG).show();
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Decline().execute();
            }
        });


        selelct_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent select_address=new Intent(getApplicationContext(), ShippingAddressActivity.class);
                startActivity(select_address);
                Bundle bundle = new Bundle();
                //Add your data from getFactualResults method to bundle
                bundle.putString("ADDRESS_PAGE_BACKNAV_NAME", "pricequoted");
                select_address.putExtras(bundle);
                finish();
            }
        });


        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });




        continue_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finalMethodId=sharedlogin.getString("ShippingMethodId","");
                finalMethodPrize=sharedlogin.getString("price","");
                finalAddressId=sharedlogin.getString("finalAddressId","");


                Log.e("tag","1234567890----------->"+finalMethodId+" "+finalMethodPrize+" "+finalAddressId);

                new continueToAccept().execute();


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
                        head_layout.setVisibility(View.GONE);
                        total_layout.setVisibility(View.GONE);

                    }
                    else {
                        noparts_found.setVisibility(View.GONE);
                        head_layout.setVisibility(View.VISIBLE);
                        total_layout.setVisibility(View.VISIBLE);


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
            recyclerView_requst.setAdapter(singleItemListAdapter);


        }

    }


    //Shiping Method API CALL: ---------------------------------------------------------------------->
    public class CallShippingMethod_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            //avi.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_GET_SHIPPING_METHOD,get_apikey_from_login);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // avi.setVisibility(View.GONE);

            try{
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    String message=jo.getString("message");
                    JSONArray car_array=jo.getJSONArray("data");
                    if(car_array.length()==0)
                    {
                        shipping_method_linear.setVisibility(View.GONE);
                    }
                    else {
                        shipping_method_linear.setVisibility(View.VISIBLE);

                        for(int i=0;i<car_array.length();i++)
                        {
                            try {
                                shippingMethodPojo = new ShippingMethodPojo();
                                JSONObject jsonObject = car_array.getJSONObject(i);
                                shippingMethodPojo.shipping_method_id=jsonObject.getString("id");
                                shippingMethodPojo.shipping=jsonObject.getString("name");
                                shippingMethodPojo.cost=jsonObject.getString("price");
                                shippingMethodPojoArrayList.add(shippingMethodPojo);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        for (int k = 0; k < shippingMethodPojoArrayList.size();k++)
                        {

                            RadioButton btn = new RadioButton(getApplicationContext());
                            btn.setId(k);
                            final String itemName = shippingMethodPojoArrayList.get(k).shipping;

                            btn.setText(itemName);
                            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                            shipping_method_group.addView(btn, params);
                            // btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.radio_selector));

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


        }
    }


    //CONTINUE TO ACCEPT API CALL: ---------------------------------------------------------------------->

    public class continueToAccept extends AsyncTask<String, Void, String> {



        protected void onPreExecute() {
            super.onPreExecute();
            //PlacedPageActivity.av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("shippingAddressId",finalAddressId);
                jsonObject.accumulate("shippingMethodId",finalMethodId);
                jsonObject.accumulate("shippingTotal",finalMethodPrize);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_ACCEPT_QUOTE+quatationId,get_apikey_from_login,json);



            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //PlacedPageActivity.av_loader.setVisibility(View.GONE);


            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");


                if (status.equals("true")) {

                    String msg=jo.getString("message");
                    Toasty.success(getApplicationContext(),msg,Toasty.LENGTH_SHORT).show();



                    edit.remove("ShippingMethodId");
                    edit.remove("price");
                    edit.remove("finalAddressId");
                    edit.remove("status_condition");
                    methodItem.clear();
                    edit.commit();


                   Intent revert=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(revert);
                    finish();


                } else {

                    String error=jo.getString("error");
                    Toasty.warning(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }
        }
    }


    //DECLINE  API CALL: ---------------------------------------------------------------------->

    public class Decline extends AsyncTask<String, Void, String> {



        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestPut(Config.WEB_URL_QUOTE_DECLINE+quatationId,get_apikey_from_login);



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
                    Toasty.success(getApplicationContext(),msg,Toasty.LENGTH_SHORT).show();


                    Intent revert=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(revert);
                    finish();


                } else {

                    String error=jo.getString("error");
                    Toasty.warning(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }
        }
    }

}
