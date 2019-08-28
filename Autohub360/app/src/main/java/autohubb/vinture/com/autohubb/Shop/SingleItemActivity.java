package autohubb.vinture.com.autohubb.Shop;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;

/**
 * Created by Salman on 03-05-2019.
 */

public class SingleItemActivity extends Activity{
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    LinearLayout back_lv;
    TextView itemName,brandName,itemPrice,itemStock,addcart_tv,brand_tv,desc_tv,partno_tv,included_tv;
    ImageView itemimg_iv;
    String productid,image,name,desc,price,stock,brand_str,partno_str,included_str;
    Intent intent;
    public String get_mail_from_reg, get_otp_from_reg, get_apikey_from_login, get_email_from_login, get_userid_from_login, get_username_from_login;
    TextView okay,message,car_type;
    ImageView close_icon,icon_message;
    AVLoadingIndicatorView av_loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);
        back_lv=findViewById(R.id.back_lv);
        itemName=findViewById(R.id.itemName);
        brandName=findViewById(R.id.brandName);
        itemPrice=findViewById(R.id.itemPrice);
        itemStock=findViewById(R.id.itemStock);
        addcart_tv=findViewById(R.id.addcart_tv);
        brand_tv=findViewById(R.id.brand_tv);
        itemimg_iv=findViewById(R.id.itemimg_iv);
        desc_tv=findViewById(R.id.desc);
        partno_tv=findViewById(R.id.partno_tv);
        included_tv=findViewById(R.id.included_tv);
        av_loader = (AVLoadingIndicatorView) findViewById(R.id.avi_2);



        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg = sharedlogin.getString("get_otp_from_reg", "");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login = sharedlogin.getString("get_email_from_login", "");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login = sharedlogin.getString("get_username_from_login", "");

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(SingleItemActivity.this, v1);
        intent = getIntent();
        productid=intent.getStringExtra("purchase_id");
        image=intent.getStringExtra("purchase_image");
        name=intent.getStringExtra("purchase_name");
        desc=intent.getStringExtra("purchase_desc");
        price=intent.getStringExtra("purchase_price");
        stock=intent.getStringExtra("purchase_stock");

        brand_str=intent.getStringExtra("brand");
        partno_str=intent.getStringExtra("partno");
        included_str=intent.getStringExtra("included");

        if(brand_str!=null)
        {
            brand_tv.setText(brand_str);
        }
        else
        {
            brand_tv.setText("Not Mentioned");
        }



        if(partno_str!=null)
        {
            partno_tv.setText(partno_str);
        }
        else
        {
            partno_tv.setText("Not Mentioned");
        }


        if(included_str!=null)
        {
            included_tv.setText(included_str);
        }
        else
        {
            included_tv.setText("Not Mentioned");
        }

        itemName.setText(name);
        itemPrice.setText("₦"+price);
        desc_tv.setText(desc);
        itemStock.setText("Only "+stock+" left in Stock");

        Glide.with(getApplicationContext())
                .load(image)
                .into(itemimg_iv);



        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        addcart_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Addcart_AsyncTask().execute();
            }
        });

    }




    private class Addcart_AsyncTask extends AsyncTask<String,String,String> {

        protected void onPreExecute() {

            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
        }




        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            try {

                JSONObject cart_data = new JSONObject();

                cart_data.put("productId", productid);
                cart_data.put("quantity", "1");



                JSONArray array=new JSONArray();
                array.put(cart_data);



                json = array.toString();
                Log.e("tag","json response---------->"+json);


                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_PUT_ADDCART_METHOD+get_userid_from_login,get_apikey_from_login,json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            av_loader.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                Log.e("tag", "1");

                if (status.equals("true")) {


                    final Dialog dialog = new Dialog(SingleItemActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_confirmation);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);
                    okay=dialog.findViewById(R.id.okay);
                    close_icon=dialog.findViewById(R.id.close_icon);
                    icon_message=dialog.findViewById(R.id.icon_message);
                    message=dialog.findViewById(R.id.message);
                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");

                    okay.setTypeface(tf);
                    message.setTypeface(tf);
                    message.setText("Your Item Added to Bag");
                    icon_message.setBackgroundResource(R.drawable.shop_cart_basket);
                    okay.setText("View Bag");

                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            finish();
                        }
                    });

                    okay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i=new Intent(getApplicationContext(),ViewCartActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });

                    dialog.show();


                } else {
                    Toast.makeText(getApplicationContext(),"Item can't added to Cart",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }
        }
    }


}
