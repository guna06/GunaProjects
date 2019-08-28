package autohubb.vinture.com.autohubb.Shop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autohubb.vinture.com.autohubb.MyOrdersModule.ShippingAddressActivity;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.activity.MainActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.model.ShippingMethodPojo;
import autohubb.vinture.com.autohubb.model.ViewCartPojo;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;
import es.dmoral.toasty.Toasty;

public class ViewCartActivity extends Activity {
    public static List<String> selectedItems;
    RecyclerView recyclerView_viewcart;
    ViewCartAdapter viewCartAdapter;
    ViewCartPojo viewCartPojo;
    private List<ViewCartPojo> viewCartPojoList=new ArrayList<>();
    private Map<String, ViewCartPojo> viewCartPojoMap=new HashMap<>();
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    LinearLayout back,noparts_found,back_lv;
    TextView submit_tv,shop_count;
    AVLoadingIndicatorView avi;
    OrderItem orderItem;
    public static ShippingItems shippingItems;
    public float total_prz,total_qty;
    RadioGroup shipping_method_group;
    ShippingMethodPojo shippingMethodPojo;
    private List<ShippingMethodPojo> shippingMethodPojoArrayList=new ArrayList<>();
    List<String> methodItem=new ArrayList<>();
    public static String selected_method_id;
    LinearLayout bottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_cart);

        shippingItems = new ShippingItems();


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewCartActivity.this, v1);

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");

        noparts_found=findViewById(R.id.noparts_found);
        back_lv=findViewById(R.id.back_lv);
        recyclerView_viewcart=findViewById(R.id.recyclerView_viewcart);
        submit_tv=findViewById(R.id.submit_tv);
        shop_count=findViewById(R.id.shop_count);
        avi=findViewById(R.id.avi);

        shipping_method_group=findViewById(R.id.shipping_method_group);
        bottom=findViewById(R.id.bottom);
        shipping_method_group.removeAllViews();

        selectedItems=new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView_viewcart.setLayoutManager(manager);
        recyclerView_viewcart.setHasFixedSize(true);
        viewCartAdapter = new ViewCartAdapter(this, viewCartPojoList);

        recyclerView_viewcart.setAdapter(viewCartAdapter);
        noparts_found.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);

        shipping_method_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                //shipped_tvv1.setText("Air Cargo");
                shippingItems.shippingMethodId = selected_method_id;
                selected_method_id=shippingMethodPojoArrayList.get(i).shipping_method_id;
                methodItem.add(selected_method_id);
            }
        });


        if (Util.Operations.isOnline(ViewCartActivity.this)) {
            //Cart List API calling
            new ViewCart_Task().execute();
            new CallShippingMethod_Task().execute();
        }
        else
        {
            Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
        }


        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent back=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(back);
                finish();
            }
        });


        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(methodItem.size()>0)
                {
                    List<OrderItem> orderItems = new ArrayList<>();


                    for(int i=0;i<viewCartPojoList.size();i++)
                    {
                        OrderItem orderItem = new OrderItem();
                        orderItem.id = viewCartPojoList.get(i).cartId;
                        orderItem.price=viewCartPojoList.get(i).productPrice;
                        //orderItem.discount=viewCartPojoList.get(i).p;
                        orderItem.total=viewCartPojoList.get(i).productTotalPrize;
                        orderItem.quantity=String.valueOf(total_qty);

                        orderItems.add(orderItem);
                    }

                    shippingItems.orderItemsList = orderItems;




                    Intent back=new Intent(getApplicationContext(),ShippingAddressActivity.class);
                    //Create the bundle
                    Bundle bundle = new Bundle();
                    //Add your data from getFactualResults method to bundle
                    bundle.putString("ADDRESS_PAGE_BACKNAV_NAME", "viewcart");
                    back.putExtras(bundle);
                    //Add the bundle to the intent
                    startActivity(back);
                    finish();

                }
                else {
                    Toast.makeText(getApplicationContext(),"Please select Shipping Method",Toast.LENGTH_LONG).show();
                }


            }
        });


    }








    //Create Order API CALL: ---------------------------------------------------------------------->
    public class ViewCart_Task extends AsyncTask<String, Void, String> {
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

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_GET_LIST_CART_METHOD+get_userid_from_login,get_apikey_from_login);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //avi.setVisibility(View.GONE);


            try{

                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                if (status.equals("true")) {
                    String message=jo.getString("message");
                    JSONObject object=jo.getJSONObject("data");
                    JSONArray car_array=object.getJSONArray("cartItems");
                    int c=car_array.length();
                    shop_count.setText("Shop Bag Count: "+String.valueOf(c));
                    if(car_array.length()==0)
                    {
                        noparts_found.setVisibility(View.VISIBLE);
                        bottom.setVisibility(View.GONE);
                    }
                    else {
                        noparts_found.setVisibility(View.GONE);
                        bottom.setVisibility(View.VISIBLE);
                        for(int i=0;i<car_array.length();i++)
                        {
                            try {
                                viewCartPojo = new ViewCartPojo();
                                JSONObject jsonObject = car_array.getJSONObject(i);

                                viewCartPojo.cartId=jsonObject.getString("cartId");
                                viewCartPojo.userId=jsonObject.getString("userId");
                                viewCartPojo.currentMileage=jsonObject.getString("currentMileage");
                                viewCartPojo.comment=jsonObject.getString("comment");

                                viewCartPojo.productId=jsonObject.getString("productId");
                                viewCartPojo.productName=jsonObject.getString("productName");
                                viewCartPojo.productImage=jsonObject.getString("productImage");
                                viewCartPojo.categoryName=jsonObject.getString("categoryName");
                                viewCartPojo.productPrice=jsonObject.getDouble("productPrice");
                                viewCartPojo.productType=jsonObject.getString("productType");

                                viewCartPojoList.add(viewCartPojo);
                                viewCartPojoMap.put(viewCartPojo.cartId, viewCartPojo);
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
            recyclerView_viewcart.setAdapter(viewCartAdapter);
        }
    }


    //Shiping Method API CALL: ---------------------------------------------------------------------->
    public class CallShippingMethod_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(back);
        finish();
    }
}
