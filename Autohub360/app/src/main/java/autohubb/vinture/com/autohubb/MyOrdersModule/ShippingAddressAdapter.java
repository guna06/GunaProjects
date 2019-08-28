package autohubb.vinture.com.autohubb.MyOrdersModule;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.Cart.ShippingMethod;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.Shop.OrderItem;
import autohubb.vinture.com.autohubb.Shop.ViewCartActivity;
import autohubb.vinture.com.autohubb.VehicleModule.OrderPartsOne;
import autohubb.vinture.com.autohubb.activity.MainActivity;
import autohubb.vinture.com.autohubb.model.ShippingAddressPojo;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import es.dmoral.toasty.Toasty;


/**
 * Created by Guna on 29-11-2017.
 */

public class ShippingAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<ShippingAddressPojo> shippingAddressPojos;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String selected_shipping_id,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;

    String navigation_role;

    private RadioButton lastCheckedRB = null;

    public ShippingAddressAdapter(Context applicationContext, List<ShippingAddressPojo> shippingAddressPojoArraylist) {
        this.context=applicationContext;
        this.shippingAddressPojos = shippingAddressPojoArraylist;
        inflater = LayoutInflater.from(context);

    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.shipping_address_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final ShippingAddressPojo current= shippingAddressPojos.get(position);

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(context);
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");

        String f_name=current.firstName;
        String l_name=current.lastName;
        String address_one=current.addressLine1;
        String address_two=current.addressLine2;
        String city=current.city;
        String state=current.state;
        String country=current.country;


        String address_merge=address_one+", "+address_two+ ",  "+city+", "+state+", "+country;
        myHolder.address_content.setText(address_merge);



       myHolder.ship_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               if(ShippingAddressActivity.navigation_role.equals("viewcart"))
               {

                   final ShippingAddressPojo current= shippingAddressPojos.get(position);
                   selected_shipping_id=current.id;

                   new CreateOrder_Task().execute();

               }else
               {
                   SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
                   SharedPreferences.Editor edit = shared.edit();
                   edit.putString("status_condition","address_true");
                   edit.commit();
                   final ShippingAddressPojo current= shippingAddressPojos.get(position);

                   myHolder.ship_click.setBackgroundColor(Color.parseColor("#dadada"));


                   Intent ship=new Intent(context,PriceQuotedPageActivity.class);
                   ship.putExtra("AddressId",current.id);
                   ship.putExtra("Address",myHolder.address_content.getText().toString());
                   ship.putExtra("navdashboard", "true");
                   context.startActivity(ship);
               }



            }
        });


    }


    @Override
    public int getItemCount() {
        Log.e("tag","SIZE------------>"+shippingAddressPojos.size());
        return shippingAddressPojos.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView address_content,address_tv,city_tv,phone_tv,email_tv,submit_tv;
        public ImageView del_ship_address;
        public LinearLayout ship_click;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");

            address_content=itemView.findViewById(R.id.address_content);
            ship_click=itemView.findViewById(R.id.ship_click);
           /* address_tv=itemView.findViewById(R.id.address_tv);
            city_tv=itemView.findViewById(R.id.city_tv);
            phone_tv=itemView.findViewById(R.id.phone_tv);
            email_tv=itemView.findViewById(R.id.email_tv);
            submit_tv=itemView.findViewById(R.id.submit_tv);
            del_ship_address=itemView.findViewById(R.id.del_ship_address);
           */

            address_content.setTypeface(tf);
            /*address_tv.setTypeface(tf);
            city_tv.setTypeface(tf);
            phone_tv.setTypeface(tf);
            email_tv.setTypeface(tf);
            submit_tv.setTypeface(tf);*/
        }
    }



    //Create Order API CALL: ---------------------------------------------------------------------->
    public class CreateOrder_Task extends AsyncTask<String, Void, String> {



        protected void onPreExecute() {
            super.onPreExecute();
            ShippingAddressActivity.avi.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId",get_userid_from_login);
                jsonObject.put("shippingAddressId",selected_shipping_id);
                jsonObject.put("shippingMethodId",ViewCartActivity.selected_method_id);
                jsonObject.put("shippingTotal",500);
                List<OrderItem> orderItemsList = new ArrayList<>();

                orderItemsList = ViewCartActivity.shippingItems.orderItemsList;
                JSONArray array=new JSONArray();
                for(int k=0;k<orderItemsList.size();k++)
                {
                    JSONObject object=new JSONObject();
                    object.put("id",orderItemsList.get(k).id);
                    object.put("price",orderItemsList.get(k).price);
                    object.put("discount","0.00");
                    object.put("total",orderItemsList.get(k).total);
                    object.put("quantity",orderItemsList.get(k).quantity);
                    array.put(object);
                }

                jsonObject.put("cartItems",array);

                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_CREATE_ORDER, get_apikey_from_login,json);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ShippingAddressActivity.avi.setVisibility(View.GONE);

            try{
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {

                    Log.e("tag","12345-------------->"+status);
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_order_confirmation);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");

                    TextView ok_btn = dialog.findViewById(R.id.ok_tv);
                    TextView order_more_parts=dialog.findViewById(R.id.order_more_parts) ;

                    ok_btn.setTypeface(tf);
                    order_more_parts.setTypeface(tf);


                    order_more_parts.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toasty.info(context,"Under Development",Toasty.LENGTH_SHORT).show();
                        }
                    });



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(context, OrderPageActivity.class);
                            intent.putExtra("PAGE","ORDER_PAGE");
                            context.startActivity(intent);
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
}
