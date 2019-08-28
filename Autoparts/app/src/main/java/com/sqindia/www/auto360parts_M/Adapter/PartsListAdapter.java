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

public class PartsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<PartsList> carPartsLists;

    String path_carimg1,path_carimg2,path_vinimg1,path_vinimg2,car_status,get_carid;

    public PartsListAdapter(CarPartsList carPartsList, List<PartsList> carPartsLists) {
        inflater = LayoutInflater.from(context);
        this.context=carPartsList;
        this.carPartsLists=carPartsLists;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewparts_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final PartsList current=carPartsLists.get(position);



        myHolder.maincat_tv.setText(current.carlist_maincat);
        myHolder.subcat_tv.setText(current.carlist_subcat);


        String carpic_path1=  current.carlist_image1;
        String carpic_path2=  current.carlist_image2;

        if(!carpic_path1.equals(""))
        {
            Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_images/"+carpic_path1)
                    .into(myHolder.pic1_iv);
        }

        if(!carpic_path2.equals(""))
        {
            Glide.with(context).load("http://104.197.80.225/autoparts360/assets/img/car_images/"+carpic_path2)
                    .into(myHolder.pic2_iv);
        }

    }


    @Override
    public int getItemCount() {
        return carPartsLists.size();
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
            maincat_tv = (TextView) itemView.findViewById(R.id.maincat_tv);
            subcat_tv = (TextView) itemView.findViewById(R.id.subcat_tv);
            pic1_iv = itemView.findViewById(R.id.pic1_iv);
            pic2_iv = itemView.findViewById(R.id.pic2_iv);

            maincat_tv.setTypeface(tf);
            subcat_tv.setTypeface(tf);

        }
    }




}
