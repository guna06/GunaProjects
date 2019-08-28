package autohubb.vinture.com.autohubb.VehicleModule;



import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.model.CompanyList;
import autohubb.vinture.com.autohubb.model.SingleCarDetails;


/**
 * Created by Guna on 29-11-2017.
 */

public class CompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<CompanyList> companyListList;
    SingleCarDetails singleCarDetails=new SingleCarDetails();
    Dialog company_dialog;
    TextView head_tv,submit_company;
    LinearLayout close_lnr;
    EditText com_name,email,phone,address1,address2,contact_name,contact_ph;
    AVLoadingIndicatorView av_loader,dialog_avi;
    Typeface lato_head;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String d_id,company_id,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;


    public CompanyAdapter(Context applicationContext, List<CompanyList> companyListList) {
        this.context=applicationContext;
        this.companyListList = companyListList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.company_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final CompanyList current= companyListList.get(position);

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(context);
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");

        myHolder.driver_fname.setText(current.company_name);
        myHolder.driver_email.setText(current.company_email);
        myHolder.driver_phone.setText(current.company_phone);
        myHolder.driver_city.setText(current.address1);
        myHolder.driver_state.setText(current.address2);
        myHolder.driver_city.setText(current.country);


        myHolder.add_car_lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CompanyList current= companyListList.get(position);
                String id_company=current.id;
                Log.e("tag","printing id----------------0>"+id_company);
                Intent i=new Intent(context,RegisterCarActivity.class);
                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("get_companyid_from_compannylist1",id_company);
                editor.apply();
                editor.commit();
                context.startActivity(i);
            }
        });


        myHolder.add_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CompanyList current= companyListList.get(position);
                d_id=current.id;
                Intent d=new Intent(context,DriversPageActivity.class);
                SharedPreferences putCompany = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor putVehicleEditor = putCompany.edit();
                putVehicleEditor.putString("get_companyid_from_compannylist",d_id);
                putVehicleEditor.commit();
                context.startActivity(d);
            }
        });


      /*  myHolder.delete_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Under Development",Toast.LENGTH_LONG).show();
            }
        });
*/

        myHolder.update_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CompanyList current= companyListList.get(position);
                company_id=current.id;


                Intent i=new Intent(context,UpdateCompanyActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("Company_Id", company_id);
                bundle.putString("Company_Name", current.company_name);
                bundle.putString("Company_Email", current.company_email);
                bundle.putString("Company_Phone",current.company_phone );
                bundle.putString("Company_Address1", current.address1);
                bundle.putString("Company_Address2", current.address2);
                bundle.putString("Contact_Name", current.contact_name);
                bundle.putString("Contact_ph", current.contact_phone);
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });



    }


    @Override
    public int getItemCount() {
        return companyListList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView driver_fname,driver_phone,driver_email,driver_city,driver_state;
        public ImageView driver_logo,delete_company;
        LinearLayout add_car_lnr,add_driver;
        ImageView update_company;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");

            driver_fname=itemView.findViewById(R.id.driver_fname);
            driver_phone=itemView.findViewById(R.id.driver_phone);
            driver_email=itemView.findViewById(R.id.driver_email);
            driver_city=itemView.findViewById(R.id.driver_city);
            driver_state=itemView.findViewById(R.id.driver_state);
            driver_logo=itemView.findViewById(R.id.driver_logo);
            add_car_lnr=itemView.findViewById(R.id.add_car_lnr);
            update_company=itemView.findViewById(R.id.update_company);
            add_driver=itemView.findViewById(R.id.add_driver);
           // delete_company=itemView.findViewById(R.id.delete_company);

            driver_fname.setTypeface(tf);
            driver_phone.setTypeface(tf);
            driver_email.setTypeface(tf);
            driver_city.setTypeface(tf);
            driver_state.setTypeface(tf);
        }
    }
}
