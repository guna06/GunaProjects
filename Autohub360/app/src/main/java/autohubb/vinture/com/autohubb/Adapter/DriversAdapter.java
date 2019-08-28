package autohubb.vinture.com.autohubb.Adapter;


import android.app.Dialog;
import android.content.Context;
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
import autohubb.vinture.com.autohubb.VehicleModule.CommercialCarListActivity;
import autohubb.vinture.com.autohubb.model.DriverList;
import autohubb.vinture.com.autohubb.model.SingleCarDetails;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;


/**
 * Created by Guna on 29-11-2017.
 */

public class DriversAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<DriverList> driverLists;
    SingleCarDetails singleCarDetails=new SingleCarDetails();
    Dialog company_dialog;
    TextView head_tv,submit_company;
    LinearLayout close_lnr;
    EditText com_name,email,phone,address1,address2,contact_name,contact_ph;
    AVLoadingIndicatorView av_loader,dialog_avi;
    Typeface lato_head;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String company_id,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;

    public DriversAdapter(Context applicationContext, List<DriverList> driverLists) {
        this.context=applicationContext;
        this.driverLists = driverLists;
        inflater = LayoutInflater.from(context);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.driver_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final DriverList current= driverLists.get(position);

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(context);
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");

        myHolder.driver_fname.setText(current.firstName);
        myHolder.driver_lname.setText(current.lastName);
        myHolder.driver_phone.setText(current.phone);
        myHolder.driver_email.setText(current.email);

        myHolder.driver_city.setText(current.city);
        myHolder.driver_state.setText(current.state);



        Glide.with(context)
                .load(current.image)

                .into(myHolder.driver_logo);



    }


    @Override
    public int getItemCount() {

        return driverLists.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView driver_fname,driver_lname,driver_phone,driver_email,driver_city,driver_state;
        public ImageView driver_logo;
        LinearLayout add_car_lnr,add_driver;
        ImageView update_company;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");

            driver_fname=itemView.findViewById(R.id.driver_fname);
            driver_lname=itemView.findViewById(R.id.driver_lname);
            driver_phone=itemView.findViewById(R.id.driver_phone);
            driver_email=itemView.findViewById(R.id.driver_email);
            driver_city=itemView.findViewById(R.id.driver_city);
            driver_state=itemView.findViewById(R.id.driver_state);
            driver_logo=itemView.findViewById(R.id.driver_logo);
            add_car_lnr=itemView.findViewById(R.id.add_car_lnr);
            update_company=itemView.findViewById(R.id.update_company);
            add_driver=itemView.findViewById(R.id.add_driver);

            driver_fname.setTypeface(tf);
            driver_lname.setTypeface(tf);
            driver_phone.setTypeface(tf);
            driver_email.setTypeface(tf);
            driver_city.setTypeface(tf);
            driver_state.setTypeface(tf);
        }
    }




    //UPDATE COMPANY API CALL: ---------------------------------------------------------------------->
    private class UpdateCompany extends AsyncTask<String,String,String> {

        protected void onPreExecute() {
            super.onPreExecute();
            dialog_avi.setVisibility(View.VISIBLE);
        }




        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            try {

                JSONObject commercial_data = new JSONObject();
                commercial_data.put("userId", get_userid_from_login);
                commercial_data.put("companyName", com_name.getText().toString());
                commercial_data.put("email", email.getText().toString());
                commercial_data.put("phone", phone.getText().toString());
                commercial_data.put("address1", address1.getText().toString());

                commercial_data.put("address2", address2.getText().toString());
                //commercial_data.put("contact_name", contact_name.getText().toString());



                json = commercial_data.toString();
                Log.e("tag","json response---------->"+json);


                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_UPDATE_COMPANY_DETAILS+company_id,get_apikey_from_login,json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog_avi.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");


                if (status.equals("true")) {
                    String message=jo.getString("message");



                    JSONObject object=jo.getJSONObject("data");
                    //String companyId=object.getString("companyId");
                    company_dialog.dismiss();
                    Intent i=new Intent(context,CommercialCarListActivity.class);
                    context.startActivity(i);



                } else {
                    Toast.makeText(context,"Login Failed",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }






        }
    }

}
