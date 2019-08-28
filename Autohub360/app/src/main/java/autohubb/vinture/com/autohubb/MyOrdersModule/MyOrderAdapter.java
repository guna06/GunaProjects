package autohubb.vinture.com.autohubb.MyOrdersModule;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.model.SingleCarDetails;
import autohubb.vinture.com.autohubb.utils.Config;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Guna on 29-11-2017.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context activity;
    public LayoutInflater inflater;
    private List<MyOrdersPojo> myOrdersPojos;
    SingleCarDetails singleCarDetails=new SingleCarDetails();
    Dialog dialog;
    TextView dialog_head_tv,tv;
    Button ok_btn,cancel_btn;
    ImageView logo_img;
    String get_request_id;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String get_status,get_order_id,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;

    public MyOrderAdapter(FragmentActivity activity, List<MyOrdersPojo> myOrdersPojoList) {

        this.activity=activity;
        this.myOrdersPojos=myOrdersPojoList;
        inflater = LayoutInflater.from(activity);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.myorders_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final MyOrdersPojo current=myOrdersPojos.get(position);


        sharedlogin = PreferenceManager.getDefaultSharedPreferences(activity);
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");




        myHolder.order_id.setText(current.orderNumber);
        myHolder.order_status.setText(current.orderStatus);


            if(current.orderStatus.equals("Placed"))
            {
                myHolder.status_lnr.setBackground(ContextCompat.getDrawable(activity,R.drawable.placed_bg));
            }
            else if(current.orderStatus.equals("Delivered"))
            {
                myHolder.status_lnr.setBackground(ContextCompat.getDrawable(activity,R.drawable.accept_bg_fill));
            }
            else if(current.orderStatus.equals("Shipped"))
            {
                myHolder.status_lnr.setBackground(ContextCompat.getDrawable(activity,R.drawable.price_quoted_bg));
            }
            else if(current.orderStatus.equals("Not Paid"))
            {
                myHolder.status_lnr.setBackground(ContextCompat.getDrawable(activity,R.drawable.pendingpay_bg));

            }




        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#F6F6F6"));
            //holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }



        myHolder.next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MyOrdersPojo current=myOrdersPojos.get(position);

                get_order_id=current.orderId;
                get_status=current.orderStatus;
                SharedPreferences putquotData = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor putQuotEditor = putquotData.edit();
                putQuotEditor.putString("get_order_ID",get_order_id);
                putQuotEditor.putString("get_order_STATUS",get_status);
                putQuotEditor.commit();






                Intent i=new Intent(activity,OrderPageActivity.class);
                i.putExtra("order_status",get_status);
                activity.startActivity(i);

            }
        });


    }


    @Override
    public int getItemCount() {
        return myOrdersPojos.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView order_id,order_status;
        public LinearLayout delete_request,status_lnr,next_page;



        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helevetical.ttf");

            order_id=itemView.findViewById(R.id.order_id);
            order_status=itemView.findViewById(R.id.order_status);
            status_lnr=itemView.findViewById(R.id.status_lnr);
            next_page=itemView.findViewById(R.id.next_page);


            order_id.setTypeface(tf);
            order_status.setTypeface(tf);

        }
    }







    //DELETE ORDER REQUEST API CALL: ---------------------------------------------------------------------->

    public class deleteRequestItem extends AsyncTask<String, Void, String> {



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
                        .url(Config.WEB_URL_REQUEST_DELETE+get_request_id)
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

                Toast.makeText(activity,"Request Item Deleted Successfully",Toast.LENGTH_LONG).show();
                dialog.dismiss();



            }
        }
    }
}
