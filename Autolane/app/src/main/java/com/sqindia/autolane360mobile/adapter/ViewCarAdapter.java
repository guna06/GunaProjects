package com.sqindia.autolane360mobile.adapter;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import com.bumptech.glide.Glide;
import com.sqindia.autolane360mobile.R;
import com.sqindia.autolane360mobile.activity.ViewCarActivity;
import com.sqindia.autolane360mobile.activity.ViewDetailActivity;
import com.sqindia.autolane360mobile.model.CarList;
import org.json.JSONObject;
import java.util.List;


/**
 * Created by Guna on 29-11-2017.
 */

public class ViewCarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<CarList> carLists;

    String path_carimg1;

    public ViewCarAdapter(ViewCarActivity viewCarActivity, List<CarList> carListList) {

        this.context=viewCarActivity;
        this.carLists=carListList;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewcar_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final CarList current=carLists.get(position);

        String str_carbrand,str_caryear,str_carmodel;
        str_carbrand=current.car_brand;
        str_caryear=current.car_year;
        str_carmodel=current.car_model;
        path_carimg1=current.car_image;

        myHolder.carname.setText(str_caryear +"  "+str_carbrand+" "+str_carmodel);
        myHolder.vinno_tv.setText("Vin: "+current.car_vinstatus);


        String prz=current.car_price;
        if(prz.equals("null"))
        {
            myHolder.priceval_tv.setTextSize(12);
            myHolder.priceval_tv.setText("Price Not Approved");
        }
        else
        {
            myHolder.priceval_tv.setText(prz);
        }

        //myHolder.priceval_tv.setText(current.car_price);
        if(path_carimg1!= null)
        {
            path_carimg1= current.car_image;
        }

        Glide.with(context).load(path_carimg1)
                .into(myHolder.carimg_iv);


        myHolder.viewcar_lr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CarList value=carLists.get(position);
                String n_head_str;
                n_head_str=value.car_year+"  "+value.car_brand+" "+value.car_model;

                myHolder.viewdetails_tv.setBackground(context.getResources().getDrawable(R.drawable.orange_fill_border));
                myHolder.viewdetails_tv.setTextColor(Color.parseColor("#FFFFFF"));

                Intent i=new Intent(context, ViewDetailActivity.class);
                i.putExtra("stockid",value.car_stockid);
                i.putExtra("heading",n_head_str);
                i.putExtra("startcode",value.car_startcode);
                i.putExtra("vinstatus",value.car_vinstatus);
                i.putExtra("location",value.car_location);
                i.putExtra("description",value.car_description);
                i.putExtra("door",value.car_doors);
                i.putExtra("manufacture",value.car_manufacture);
                i.putExtra("ac_condition",value.car_ac_condition);
                i.putExtra("exterior_color",value.car_exterior_color);
                i.putExtra("interior_color",value.car_interior_color);
                i.putExtra("engine",value.car_engine);
                i.putExtra("vehicle_class",value.car_vehicleclass);
                i.putExtra("carkeys",value.car_keys);
                i.putExtra("transmission",value.car_transmission);
                i.putExtra("mileage",value.car_mileage);
                i.putExtra("description",value.car_description);
                i.putExtra("filter", "View");
                context.startActivity(i);
            }
        });


    }


    @Override
    public int getItemCount() {
        return carLists.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView carname,vinno_tv,viewdetails_tv,pricehead_tv,priceval_tv;
        public ImageView carimg_iv;
        LinearLayout viewcar_lr;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");


            carimg_iv=itemView.findViewById(R.id.carimg_iv);
            carname=itemView.findViewById(R.id.carname);
            vinno_tv=itemView.findViewById(R.id.vinno_tv);
            viewdetails_tv = itemView.findViewById(R.id.viewdetails_tv);
            pricehead_tv = itemView.findViewById(R.id.pricehead_tv);
            priceval_tv=itemView.findViewById(R.id.priceval_tv);
           viewcar_lr =  itemView.findViewById(R.id.viewcar_lr);


            carname.setTypeface(tf);
            vinno_tv.setTypeface(tf);
            viewdetails_tv.setTypeface(tf);
            pricehead_tv.setTypeface(tf);
            priceval_tv.setTypeface(tf);
        }
    }
}
