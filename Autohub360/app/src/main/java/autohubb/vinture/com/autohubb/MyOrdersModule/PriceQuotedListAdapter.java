package autohubb.vinture.com.autohubb.MyOrdersModule;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import es.dmoral.toasty.Toasty;

/**
 * Created by Guna on 29-11-2017.
 */

public class PriceQuotedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Activity activity;
    public LayoutInflater inflater;
    private List<SingleItemListPojo> singleItemListPojos;

    Dialog dialog;
    TextView dialog_head_tv,tv;
    Button ok_btn,cancel_btn;
    ImageView logo_img;
    String getQuotId,getQuaItemId;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String quatationId,company_id,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;

    public PriceQuotedListAdapter(PriceQuotedPageActivity priceQuotedPageActivity, List<SingleItemListPojo> singleItemListPojos) {
        this.activity=priceQuotedPageActivity;
        this.singleItemListPojos=singleItemListPojos;
        inflater = LayoutInflater.from(activity);

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mylist_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final SingleItemListPojo current=singleItemListPojos.get(position);


        sharedlogin = PreferenceManager.getDefaultSharedPreferences(activity);
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");
        quatationId=sharedlogin.getString("get_quotation_ID","");


        String str_caryaer=current.vehicleYear;
        String str_carmake=current.vehicleMake;
        String str_carmodel=current.vehicleModel;
        String merge=str_caryaer+" "+str_carmake+" "+str_carmodel;

        myHolder.product_name_tv.setText(current.itemName);
        myHolder.car_name_tv.setText(merge);
        myHolder.vin_name_tv.setText("VIN NO: "+current.vehicleVin);
        myHolder.item_price.setText("₦ "+current.itemPrice);


        //myHolder.product_name_tv.setText(current.productName);
/*
        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#F6F6F6"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }*/



        myHolder.delete_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_logout);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);
                dialog_head_tv=dialog.findViewById(R.id.dialog_head_tv);
                tv=dialog.findViewById(R.id.tv);
                ok_btn=dialog.findViewById(R.id.ok_btn);
                cancel_btn=dialog.findViewById(R.id.cancel_btn);
                logo_img=dialog.findViewById(R.id.logo_img);
                Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helevetical.ttf");

                dialog_head_tv.setTypeface(tf);
                tv.setTypeface(tf);
                ok_btn.setTypeface(tf);
                cancel_btn.setTypeface(tf);
                logo_img.setBackgroundResource(R.drawable.delete_pop);
                tv.setText("Are U Sure Want to Delete this Item!!");
                dialog_head_tv.setText("Delete Item!");

                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getQuaItemId=current.itemId;
                        Log.e("tag","DELETE ID----------------->"+quatationId+" "+getQuaItemId);
                        dialog.dismiss();
                        new deleteRequestItem().execute();

                    }
                });


                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return singleItemListPojos.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView product_name_tv,car_name_tv,vin_name_tv,item_price;
        public LinearLayout delete_request;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helevetical.ttf");

            product_name_tv=itemView.findViewById(R.id.product_name_tv);
            car_name_tv=itemView.findViewById(R.id.car_name_tv);
            vin_name_tv=itemView.findViewById(R.id.vin_name_tv);
            delete_request=itemView.findViewById(R.id.delete_request);
            item_price=itemView.findViewById(R.id.item_price);


            product_name_tv.setTypeface(tf);
            car_name_tv.setTypeface(tf);
            vin_name_tv.setTypeface(tf);
            item_price.setTypeface(tf);
        }
    }







    //DELETE ORDER REQUEST API CALL: ---------------------------------------------------------------------->

    public class deleteRequestItem extends AsyncTask<String, Void, String> {



        protected void onPreExecute() {
            super.onPreExecute();
            PlacedPageActivity.av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("userId",get_userid_from_login);
                jsonObject.accumulate("quoteId",quatationId);
                jsonObject.accumulate("quoteItemId",getQuaItemId);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_REMOVE_QUOTES_ITEM,get_apikey_from_login,json);



            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            PlacedPageActivity.av_loader.setVisibility(View.GONE);


            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");


                if (status.equals("true")) {

                    String msg=jo.getString("message");
                    Toasty.success(activity,msg,Toasty.LENGTH_SHORT).show();

                    Intent verify=new Intent(activity,PlacedPageActivity.class);
                    activity.startActivity(verify);


                } else {

                    String error=jo.getString("error");
                    Toasty.warning(activity,error,Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }
        }
    }
}
