package autohubb.vinture.com.autohubb.Shop;


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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import autohubb.vinture.com.autohubb.MyOrdersModule.AcceptPageActivity;
import autohubb.vinture.com.autohubb.MyOrdersModule.BankPojo;
import autohubb.vinture.com.autohubb.MyOrdersModule.OrderPageActivity;
import autohubb.vinture.com.autohubb.MyOrdersModule.PlacedPageActivity;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import es.dmoral.toasty.Toasty;

/**
 * Created by Guna on 29-11-2017.
 */

public class BankAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Activity activity;
    public LayoutInflater inflater;
    private List<BankPojo> bankPojoList;
    Dialog dialog;
    Button cancel_btn;
    String getQuaItemId;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String quatationId,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    private int lastSelectedPosition = -1;

    public BankAdapter1(OrderPageActivity orderPageActivity, List<BankPojo> bankPojoList) {
        this.activity=orderPageActivity;
        this.bankPojoList=bankPojoList;
        inflater = LayoutInflater.from(activity);
    }



   /* public BankAdapter1(AcceptPageActivity acceptPageActivity, List<BankPojo> bankPojoList) {
        this.activity=acceptPageActivity;
        this.bankPojoList=bankPojoList;
        inflater = LayoutInflater.from(activity);
    }
*/



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.payment_item, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final BankPojo current=bankPojoList.get(position);


        sharedlogin = PreferenceManager.getDefaultSharedPreferences(activity);
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");
        quatationId=sharedlogin.getString("get_quotation_ID","");


        myHolder.name.setText(current.name);
        myHolder.accountName.setText(current.accountName);
        myHolder.accountNumber.setText(current.accountNumber);
        myHolder.branch.setText(current.branch);
        myHolder.bank_name.setText(current.name);


        myHolder.rdb.setChecked(lastSelectedPosition == position);


       /* myHolder.rdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    // Do your coding
                    final BankPojo current=bankPojoList.get(position);
                    AcceptPageActivity.bank_id=current.id;
                    Log.e("tag","printing id---------->"+AcceptPageActivity.bank_id);
                }
                else{
                    // Do your coding
                }
            }
        });
*/
    }


    @Override
    public int getItemCount() {
        return bankPojoList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView name,accountName_h,accountName,accountNumber,accountNumber_h,branch,bank_name;
        public RadioButton rdb;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helevetical.ttf");

            name=itemView.findViewById(R.id.name);
            accountName=itemView.findViewById(R.id.accountName);
            accountNumber=itemView.findViewById(R.id.accountNumber);
            accountName_h=itemView.findViewById(R.id.accountName_h);
            accountNumber_h=itemView.findViewById(R.id.accountNumber_h);
            branch=itemView.findViewById(R.id.branch);
            bank_name=itemView.findViewById(R.id.bank_name);
            rdb = itemView. findViewById(R.id.radiobutton1);

            name.setTypeface(tf);
            accountName.setTypeface(tf);
            accountNumber.setTypeface(tf);
            accountName_h.setTypeface(tf);
            accountNumber_h.setTypeface(tf);
            branch.setTypeface(tf);
            bank_name.setTypeface(tf);





            rdb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    if(compoundButton.isChecked())
                    {

                    }else
                    {

                    }
                }
            });



          /*  rdb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();



                }
            });*/
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
