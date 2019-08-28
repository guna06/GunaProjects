package com.sqindia.www.auto360parts_M.Adapter;



import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.sqindia.www.auto360parts_M.Activity.CarPartsList;
import com.sqindia.www.auto360parts_M.Model.PartsList;
import com.sqindia.www.auto360parts_M.R;
import java.util.List;

    /**
 * Created by Guna on 29-11-2017.
 */

public class SampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater1;
    private List<PartsList> partsLists;



    public SampleAdapter(CarPartsList carPartsList, List<PartsList> carPartsLists) {
        inflater1 = LayoutInflater.from(carPartsList);
        this.context=carPartsList;
        this.partsLists=carPartsLists;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater1.inflate(R.layout.viewparts_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list

        final MyHolder myHolder= (MyHolder) holder;
        final PartsList current=partsLists.get(position);


        myHolder.maincat_tv.setText(current.carlist_maincat);
        myHolder.subcat_tv.setText(current.carlist_subcat);

        String carpic_path1=  current.carlist_image1;
        String carpic_path2=  current.carlist_image2;

        if(!carpic_path1.equals(""))
        {

            if(current.carlist_maincat.equals("engine"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_engine_images/"+carpic_path1)
                        .into(myHolder.pic1_iv);
            }else if(current.carlist_maincat.equals("body"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_body_images/"+carpic_path1)
                        .into(myHolder.pic1_iv);
            }else if(current.carlist_maincat.equals("transmission")) {
            Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_transmission_images/"+carpic_path1)
                    .into(myHolder.pic1_iv);
            }else if(current.carlist_maincat.equals("interior"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_interior_images/"+carpic_path1)
                        .into(myHolder.pic1_iv);
            }else if(current.carlist_maincat.equals("rim/wheel/brakes"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_wheel_images/"+carpic_path1)
                        .into(myHolder.pic1_iv);
            }else if(current.carlist_maincat.equals("electricals"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_electrical_images/"+carpic_path1)
                        .into(myHolder.pic1_iv);
            }else if(current.carlist_maincat.equals("lights"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_light_images/"+carpic_path1)
                        .into(myHolder.pic1_iv);
            }
        }






        if(!carpic_path2.equals(""))
        {
            if(current.carlist_maincat.equals("engine"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_engine_images/"+carpic_path2)
                        .into(myHolder.pic2_iv);
            }else if(current.carlist_maincat.equals("body"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_body_images/"+carpic_path2)
                        .into(myHolder.pic2_iv);
            }else if(current.carlist_maincat.equals("transmission"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_transmission_images/"+carpic_path2)
                        .into(myHolder.pic2_iv);
            }else if(current.carlist_maincat.equals("interior"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_interior_images/"+carpic_path2)
                        .into(myHolder.pic2_iv);
            }else if(current.carlist_maincat.equals("rim/wheel/brakes"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_wheel_images/"+carpic_path2)
                        .into(myHolder.pic2_iv);
            }else if(current.carlist_maincat.equals("electricals"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_electrical_images/"+carpic_path2)
                        .into(myHolder.pic2_iv);
            }else if(current.carlist_maincat.equals("lights"))
            {
                Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_light_images/"+carpic_path2)
                        .into(myHolder.pic2_iv);
            }
        }
    }


    @Override
    public int getItemCount() {

        return partsLists.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView maincat_tv,subcat_tv;
        ImageView pic1_iv,pic2_iv;

        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");
            maincat_tv =  itemView.findViewById(R.id.maincat_tv);
            subcat_tv =  itemView.findViewById(R.id.subcat_tv);
            pic1_iv = itemView.findViewById(R.id.pic1_iv);
            pic2_iv = itemView.findViewById(R.id.pic2_iv);

            maincat_tv.setTypeface(tf);
            subcat_tv.setTypeface(tf);
        }
    }

}
