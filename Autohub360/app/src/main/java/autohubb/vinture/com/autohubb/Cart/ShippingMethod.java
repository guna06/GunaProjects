package autohubb.vinture.com.autohubb.Cart;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.MyOrdersModule.ShippingAddressActivity;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.Shop.OrderItem;
import autohubb.vinture.com.autohubb.Shop.ViewCartActivity;
import autohubb.vinture.com.autohubb.activity.MainActivity;
import autohubb.vinture.com.autohubb.model.ShippingMethodPojo;
import autohubb.vinture.com.autohubb.orders.CustomerInfoFragment;
import autohubb.vinture.com.autohubb.orders.ProductImfoFragment;
import autohubb.vinture.com.autohubb.orders.ReturnPolicyFragment;
import autohubb.vinture.com.autohubb.orders.ShippingInfoFragment;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;
import es.dmoral.toasty.Toasty;

public class ShippingMethod extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager pager;
    int currentPage;
    private TabLayout tabLayout;
   // RecyclerView shipping_recyclerview,recyclerView_shippingaddres;
    ShippingMethodPojo shippingMethodPojo;
    private List<ShippingMethodPojo> shippingMethodPojoArrayList=new ArrayList<>();
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String selected_method_id,getAddressId,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    LinearLayout back_lv;
    AVLoadingIndicatorView avi,avi_2;
    TextView place_order_tv;
    public static List<String> selectedItems;
    RadioGroup shipping_method_group;
    Intent intent;
    List<String> methodItem=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_method);

        pager=findViewById(R.id.viewpager);
        back_lv=findViewById(R.id.back_lv);
        avi=findViewById(R.id.avi);
        avi_2=findViewById(R.id.avi_2);
        place_order_tv=findViewById(R.id.place_order_tv);
        shipping_method_group=findViewById(R.id.shipping_method_group);
        shipping_method_group.removeAllViews();


        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");

        intent=getIntent();
        getAddressId=intent.getStringExtra("AddressId");


        selectedItems=new ArrayList<>();

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(1);
        setupViewPager(pager);
        //shipping_recyclerview=findViewById(R.id.shipping_recyclerview);
        //recyclerView_shippingaddres=findViewById(R.id.recyclerView_shippingaddres);

        pager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

        avi.setVisibility(View.GONE);


        shipping_method_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selected_method_id=shippingMethodPojoArrayList.get(i).shipping_method_id;
                methodItem.add(selected_method_id);
            }
        });


       /* LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        shipping_recyclerview.setLayoutManager(manager);
        shipping_recyclerview.setHasFixedSize(true);
        shippingMethodAdapter = new ShippingMethodAdapter(getApplicationContext(), shippingMethodPojoArrayList);
        shipping_recyclerview.setAdapter(shippingMethodAdapter);*/




        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ShippingAddressActivity.class);
                startActivity(i);
                finish();
            }
        });


        if (Util.Operations.isOnline(ShippingMethod.this)) {
            new CallShippingMethod_Task().execute();
        }
        else
        {
            Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
        }






        place_order_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  ViewCartActivity.shippingItems.shippingMethodId = selected_method_id;

                String shippingmethodid=ViewCartActivity.shippingItems.shippingMethodId;
                String shippingaddressid=ViewCartActivity.shippingItems.shippngAddressId;
                String userId=ViewCartActivity.shippingItems.userId;
                List<OrderItem> orderItems = ViewCartActivity.shippingItems.orderItemsList;


                Log.e("tag","***************1------>"+userId);
                Log.e("tag","***************2------>"+shippingaddressid);
                Log.e("tag","***************3------>"+shippingmethodid);
                Log.e("tag","***************4------>"+orderItems);*/

                Log.e("tag","***************1------>"+getAddressId);


              if(methodItem.size()>0)
              {
                  if (Util.Operations.isOnline(ShippingMethod.this)) {
                      new CreateOrder_Task().execute();
                  }
                  else
                  {
                      Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
                  }
              }
              else
              {
                  Toasty.info(getApplicationContext(),"Please Select Shipping Method",Toasty.LENGTH_LONG).show();
              }



            }
        });


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPage = position;
                switch (position) {
                    case 0:

                        break;

                    case 1:

                        break;

                    case 2:

                        break;

                    case 3:

                        break;

                    case 4:

                        break;

                    case 5:

                        break;


                    default:

                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ProductImfoFragment(), "Product Info");
        adapter.addFrag(new ShippingInfoFragment(), "Shipping Info");
        adapter.addFrag(new CustomerInfoFragment(), "Customer Info");
        adapter.addFrag(new ReturnPolicyFragment(), "Return Policy");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    //Shiping Method API CALL: ---------------------------------------------------------------------->
    public class CallShippingMethod_Task extends AsyncTask<String, Void, String> {
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
            avi.setVisibility(View.GONE);

            try{
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    String message=jo.getString("message");
                    JSONArray car_array=jo.getJSONArray("data");
                    if(car_array.length()==0)
                    {
                       // noparts_found.setVisibility(View.VISIBLE);
                    }
                    else {
                       // noparts_found.setVisibility(View.GONE);

                        for(int i=0;i<car_array.length();i++)
                        {
                            try {
                                shippingMethodPojo = new ShippingMethodPojo();
                                JSONObject jsonObject = car_array.getJSONObject(i);
                                shippingMethodPojo.shipping_method_id=jsonObject.getString("id");
                                shippingMethodPojo.shipping=jsonObject.getString("name");
                                Log.e("tag","9");
                                shippingMethodPojo.cost=jsonObject.getString("price");
                                Log.e("tag","10");
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
           // shipping_recyclerview.setAdapter(shippingMethodAdapter);


        }
    }

    //Create Order API CALL: ---------------------------------------------------------------------->
    public class CreateOrder_Task extends AsyncTask<String, Void, String> {
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
                jsonObject.put("userId",get_userid_from_login);
                jsonObject.put("shippingAddressId",getAddressId);
                jsonObject.put("shippingMethodId",selected_method_id);
                List<OrderItem> orderItemsList = new ArrayList<>();

                orderItemsList = ViewCartActivity.shippingItems.orderItemsList;
                JSONArray array=new JSONArray();
                for(int k=0;k<orderItemsList.size();k++)
                {
                    JSONObject object=new JSONObject();
                    object.put("id",orderItemsList.get(k).id);
                    object.put("price",orderItemsList.get(k).price);
                    object.put("discount",orderItemsList.get(k).discount);
                    object.put("total",orderItemsList.get(k).total);
                    object.put("quantity",orderItemsList.get(k).quantity);
                    array.put(object);
                }

                jsonObject.put("orderItems",array);

                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_CREATE_ORDER,get_apikey_from_login,json);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            avi.setVisibility(View.GONE);

            try{
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    String message=jo.getString("message");
                    JSONObject object=jo.getJSONObject("data");

                    String order_id=object.getString("orderId");
                    Log.e("tag","WELCOME! UR ORDER ID IS------->"+order_id);

                    final Dialog dialog = new Dialog(ShippingMethod.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_price_notification);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");

                    TextView ok_btn = dialog.findViewById(R.id.ok_tv);
                    //TextView tv=(TextView)dialog.findViewById(R.id.tv) ;

                    ok_btn.setTypeface(tf);
                    //tv.setTypeface(tf);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else
                {

                }


            }
            catch (Exception e)
            {

            }

            // Setup and Handover data to recyclerview
            // shipping_recyclerview.setAdapter(shippingMethodAdapter);


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




