package autohubb.vinture.com.autohubb.Shop;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.model.SingleCarDetails;
import autohubb.vinture.com.autohubb.model.ViewCartPojo;
import autohubb.vinture.com.autohubb.utils.Config;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Guna on 29-11-2017.
 */

public class ViewCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//    private Context context;
    public LayoutInflater inflater;
    private List<ViewCartPojo> viewCartPojoList;
    Dialog dialog;
    TextView dialog_head_tv,tv;
    Button ok_btn,cancel_btn;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    ImageView logo_img;
    String get_cart_id,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    Activity activity;

    public ViewCartAdapter(Activity activity, List<ViewCartPojo> viewCartPojoList) {
        this.viewCartPojoList = viewCartPojoList;
        inflater = LayoutInflater.from(activity);
        this.activity = activity;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_cart_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final ViewCartPojo current= viewCartPojoList.get(position);

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(activity);
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");


        myHolder.desc_tv.setText(current.categoryName);
        myHolder.itemname_tv.setText(current.productName);
        myHolder.price_tv.setText(String.valueOf(current.productPrice));

        Glide.with(activity)
                .load(current.productImage)
                .into(myHolder.image_iv);


        myHolder.quantity_edt.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                try {
                    Double price=current.productPrice;

                    current.productQuantity=Integer.parseInt(myHolder.quantity_edt.getText().toString());
                    current.productTotalPrize=price*current.productQuantity;
                    myHolder.price_tv.setText(String.valueOf(current.productTotalPrize));




                } catch (NumberFormatException | NullPointerException ex) {
                    //Handle NumberFormat and NullPointer exceptions here
                } catch (Exception ex) {
                    //Handle generic exception here
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        myHolder.delete_item_lnr.setOnClickListener(new View.OnClickListener() {
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
                tv.setText("Are U Sure Want to Delete Item!!");
                dialog_head_tv.setText("Delete Item!");

                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        get_cart_id=current.cartId;
                        Log.e("tag","DELETE ID----------------->"+get_cart_id);
                        new deleteCartItem().execute();

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
        return viewCartPojoList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView itemname_tv,desc_tv,price_tv;
        public EditText quantity_edt;
        public ImageView image_iv;
        LinearLayout delete_item_lnr,add_driver;
       // CheckBox chk_item;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helevetical.ttf");

            itemname_tv=itemView.findViewById(R.id.itemname_tv);

            desc_tv=itemView.findViewById(R.id.desc_tv);
            delete_item_lnr=itemView.findViewById(R.id.delete_item_lnr);
            image_iv=itemView.findViewById(R.id.image_iv);
            price_tv=itemView.findViewById(R.id.price_tv);
            quantity_edt=itemView.findViewById(R.id.quantity_edt);


            itemname_tv.setTypeface(tf);
            desc_tv.setTypeface(tf);
            price_tv.setTypeface(tf);

        }
    }




    //DELETE API CALL: ---------------------------------------------------------------------->

    public class deleteCartItem extends AsyncTask<String, Void, String> {



        protected void onPreExecute() {
            super.onPreExecute();
            //av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(Config.WEB_URL_GET_DELETE+get_cart_id)
                        .delete(null)
                        .addHeader("x-api-key", get_apikey_from_login)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Postman-Token", "8426e2a5-33b0-4488-a14d-edd69caee808")
                        .build();

                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                String status = jsonObject.getString("status");

                if (status.equals("true")) {


                }



            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
                return "error";
            }
            return "true";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //av_loader.setVisibility(View.GONE);


            if (s.equals("true")){
                //av_loader.setVisibility(View.GONE);

                Toast.makeText(activity,"Cart Deleted Successfully",Toast.LENGTH_LONG).show();
                dialog.dismiss();
                Intent i=new Intent(activity,ViewCartActivity.class);
                activity.startActivity(i);


            }
        }
    }


}
