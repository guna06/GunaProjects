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
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.model.ShippingMethodPojo;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;
import es.dmoral.toasty.Toasty;

/**
 * Created by Salman on 26-03-2019.
 */

public class PlacedPageActivity extends Activity{
    ShippingMethodPojo shippingMethodPojo;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    private List<ShippingMethodPojo> shippingMethodPojoArrayList=new ArrayList<>();
    RadioGroup shipping_method_group;
    String navdashboard,selected_address,status,quatationId,getAddressId,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    LinearLayout back_lv;
    RecyclerView recyclerView_requst;
    PlacedListAdapter singleItemListAdapter;
    SingleItemListPojo singleItemListPojo;
    private List<SingleItemListPojo> singleItemListPojos=new ArrayList<>();
    public static AVLoadingIndicatorView av_loader;
    LinearLayout head_layout,total_layout,noparts_found,shipping_method_linear,choose_address_linear,display_address_linear,cancle_linear,accept_linear;
    TextView continue_accept,show_address,selelct_address,quotation_no,quotation_status,total_val,shipping_head,shipping_Address,cancel_btn,accept_btn;
    Bundle extras;
    LinearLayoutManager manager;
    String status_condition,selected_method_id,method_price,finalMethodId,finalMethodPrize,finalAddressId;
    List<String> methodItem=new ArrayList<>();
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    private ShimmerFrameLayout mShimmerViewContainer;
`
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotation_page);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(PlacedPageActivity.this, v1);
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
        status=sharedlogin.getString("get_quotation_STATUS","");
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        status_condition = sharedlogin.getString("status_condition", "");

        shipping_method_group=findViewById(R.id.shipping_method_group);
        back_lv=findViewById(R.id.back_lv);

        quotation_no=findViewById(R.id.quotation_no);
        cancel_btn=findViewById(R.id.cancel_btn);
        total_layout=findViewById(R.id.total_layout);
        head_layout=findViewById(R.id.head_layout);
        recyclerView_requst=findViewById(R.id.recyclerView_requst);

        av_loader =findViewById(R.id.avi);
        total_val=findViewById(R.id.total_val);
        quotation_status=findViewById(R.id.quotation_status);
        noparts_found=findViewById(R.id.noparts_found);
        noparts_found.setVisibility(View.GONE);
        total_layout.setVisibility(View.GONE);
        head_layout.setVisibility(View.GONE);


        quotation_no.setTypeface(bold_text);


        manager = new LinearLayoutManager(getApplicationContext());
        recyclerView_requst.setLayoutManager(manager);
        recyclerView_requst.addItemDecoration(new DividerItemDecoration(this.getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView_requst.setHasFixedSize(true);
        singleItemListAdapter = new PlacedListAdapter(PlacedPageActivity.this, singleItemListPojos);
        recyclerView_requst.setAdapter(singleItemListAdapter);

        if (Util.Operations.isOnline(PlacedPageActivity.this)) {
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
            //av_loader.setVisibility(View.VISIBLE);
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

                            if(car_array.length()==0)
                            {
                                noparts_found.setVisibility(View.VISIBLE);
                                total_layout.setVisibility(View.GONE);
                                head_layout.setVisibility(View.GONE);
                            }
                            else {
                                noparts_found.setVisibility(View.GONE);
                                total_layout.setVisibility(View.VISIBLE);
                                head_layout.setVisibility(View.VISIBLE);
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






}
