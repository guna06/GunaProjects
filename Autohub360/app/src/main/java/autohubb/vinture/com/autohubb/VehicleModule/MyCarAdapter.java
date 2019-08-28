package autohubb.vinture.com.autohubb.VehicleModule;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.activity.DriverDetailsActivity;
import autohubb.vinture.com.autohubb.model.SingleCarDetails;
import es.dmoral.toasty.Toasty;

/**
 * Created by Guna on 29-11-2017.
 */

public class MyCarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<CommercialCarListPojo> commercialCarLists;
    SingleCarDetails singleCarDetails=new SingleCarDetails();


  public MyCarAdapter(FragmentActivity activity, List<CommercialCarListPojo> carListList) {
        this.context=activity;
        this.commercialCarLists=carListList;
        inflater = LayoutInflater.from(context);
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mycar_row_new, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final CommercialCarListPojo current=commercialCarLists.get(position);


        String str_caryaer=current.year;
        String str_carmake=current.make;
        String str_carmodel=current.model;
        String merge=str_caryaer+" "+str_carmake+" "+str_carmodel;

        myHolder.carname.setText(merge);

        myHolder.mileageval_tv.setText(current.actual_mileage+" Miles");
        myHolder.mileagerange_val.setText(current.mileage_range);
        myHolder.company_name_tv.setText(current.companyname);
        myHolder.location_val.setText(current.country);

        Log.e("tag", "image-------->" + current.carImage);

        Glide.with(context)
                .load(current.carImage)

                .into(myHolder.carimg_iv);



        myHolder.vinno_tv.setText("VIN: "+current.vin);

        if(current.mileage_range.equals(""))
        {
            myHolder.mileagerange_val.setText("Not Mentioned");
        }


        if(current.companyname.equals("null"))
        {
            myHolder.company_name_tv.setText("Not Mentioned");
            myHolder.company_name_tv_head.setVisibility(View.GONE);
            myHolder.company_name_tv.setVisibility(View.GONE);
        }


        if(current.business_type.equals("commerical"))
        {
            myHolder.driver_info.setVisibility(View.VISIBLE);
            myHolder.cartype_tv.setText("Commercial");
            myHolder.company_name_tv_head.setVisibility(View.VISIBLE);
            myHolder.company_name_tv.setVisibility(View.VISIBLE);

        }
        else
        {
            myHolder.driver_info.setVisibility(View.GONE);
            myHolder.cartype_tv.setText("Private");
            myHolder.company_name_tv_head.setVisibility(View.GONE);
            myHolder.company_name_tv.setVisibility(View.GONE);
        }




        myHolder.orderservice_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CommercialCarListPojo current=commercialCarLists.get(position);
                String servicepack_carname=current.year+" "+current.make+" "+current.model;
                String servicepack_vinno=current.vin;
                String servicepack_type=current.business_type;
                String servicepack_mileagerange=current.mileage_range;
                String servicepack_actualmileage=current.actual_mileage;
                String servicepack_car_id=current.vehicle_id;
                String image=current.carImage;



               Intent order_service=new Intent(context,OrderServicePack.class);
                order_service.putExtra("car_id_for_servicepack",servicepack_car_id);
                order_service.putExtra("car_name_for_servicepack",servicepack_carname);
                order_service.putExtra("car_vinno_for_servicepack",servicepack_vinno);
                order_service.putExtra("car_mileagerange_for_servicepack",servicepack_mileagerange);
                order_service.putExtra("car_actualmilrage_for_servicepack",servicepack_actualmileage);
                order_service.putExtra("car_type_for_servicepack",servicepack_type);
                order_service.putExtra("car_image_for_servicepack",image);
                context.startActivity(order_service);
            }
        });




        myHolder.orderdetails_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CommercialCarListPojo current=commercialCarLists.get(position);
                String vehicle_id=current.vehicle_id;

                String single_carname=current.year+" "+current.make+" "+current.model;
                String single_vinno=current.vin;
                String single_type=current.business_type;
                String single_mileagerange=current.mileage_range;
                String single_actualmileage=current.actual_mileage;


                Intent order_parts=new Intent(context,OrderPartsOne.class);
                order_parts.putExtra("car_id_for_parts",vehicle_id);
                order_parts.putExtra("car_name_for_parts",single_carname);
                order_parts.putExtra("car_vinno_for_parts",single_vinno);
                order_parts.putExtra("car_mileagerange_for_parts",single_mileagerange);
                order_parts.putExtra("car_actualmilrage_for_parts",single_actualmileage);
                order_parts.putExtra("car_type_for_parts",single_type);
                context.startActivity(order_parts);
            }
        });



        myHolder.lnr_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CommercialCarListPojo current=commercialCarLists.get(position);
                String selelcted_vehicle_id=current.vehicle_id;
                Log.e("tag","Print Vehicle ID"+selelcted_vehicle_id);
                Intent driver=new Intent(context,DriverDetailsActivity.class);
                driver.putExtra("pass_vehicle_id",selelcted_vehicle_id);
                context.startActivity(driver);
            }
        });


        myHolder.info_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(context, "Under Development!!", Toast.LENGTH_SHORT, true).show();
            }
        });






    }


    @Override
    public int getItemCount() {
        return commercialCarLists.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView company_name_tv_head,company_name_tv,vieworder_tv,location_head,mileagerange_head,mileagehead_tv,orderdetails_tv,carname,vinno_tv,cartype_tv,mileageval_tv,mileagerange_val,location_val,orderservice_tv;
        public ImageView carimg_iv,driver_info;
        LinearLayout lnr_driver,info_icon;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");

            carname=itemView.findViewById(R.id.carname);
            driver_info=itemView.findViewById(R.id.driver_info);
            carimg_iv=itemView.findViewById(R.id.carimg_iv);
            cartype_tv=itemView.findViewById(R.id.cartype_tv);
            mileageval_tv=itemView.findViewById(R.id.mileageval_tv);
            mileagerange_val=itemView.findViewById(R.id.mileagerange_val);
            location_val=itemView.findViewById(R.id.location_val);
            vinno_tv=itemView.findViewById(R.id.vinno_tv);
            orderservice_tv=itemView.findViewById(R.id.orderservice_tv);
            orderdetails_tv=itemView.findViewById(R.id.orderdetails_tv);
            mileagehead_tv=itemView.findViewById(R.id.mileagehead_tv);
            mileagerange_head=itemView.findViewById(R.id.mileagerange_head);
            location_head=itemView.findViewById(R.id.location_head);
           // vieworder_tv=itemView.findViewById(R.id.vieworder_tv);
            lnr_driver=itemView.findViewById(R.id.lnr_driver);
            company_name_tv=itemView.findViewById(R.id.company_name_tv);
            company_name_tv_head=itemView.findViewById(R.id.company_name_tv_head);
            info_icon=itemView.findViewById(R.id.info_icon);

            carname.setTypeface(tf);
            vinno_tv.setTypeface(tf);
            cartype_tv.setTypeface(tf);
            mileageval_tv.setTypeface(tf);
            mileagerange_val.setTypeface(tf);
         //   vieworder_tv.setTypeface(tf);
            company_name_tv.setTypeface(tf);
            company_name_tv_head.setTypeface(tf);
            location_val.setTypeface(tf);
            vinno_tv.setTypeface(tf);
            orderservice_tv.setTypeface(tf);
            orderdetails_tv.setTypeface(tf);
            mileagehead_tv.setTypeface(tf);
            mileagerange_head.setTypeface(tf);
            location_head.setTypeface(tf);
        }
    }
}
