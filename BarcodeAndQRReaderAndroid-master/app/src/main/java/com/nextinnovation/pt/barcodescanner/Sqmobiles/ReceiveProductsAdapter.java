package com.nextinnovation.pt.barcodescanner.Sqmobiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nextinnovation.pt.barcodescanner.R;

import java.util.List;


public class ReceiveProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{




    private Context context;
    public LayoutInflater inflater;
    private List<ProductsPojo> productsPojoList;
    Typeface regular,bold,extraBold,light,thin,quondo;

    public ReceiveProductsAdapter(Context applicationContext, List<ProductsPojo> productList) {
        this.context=applicationContext;
        this.productsPojoList=productList;
        inflater = LayoutInflater.from(context);


    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.products_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final ProductsPojo current=productsPojoList.get(position);



        myHolder.imeino_tv.setText(current.imei);
        myHolder.brand_tv.setText(current.brand);
        myHolder.product_tv.setText(current.product);
        myHolder.from_tv.setText(current.from);




        myHolder.receive_lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProductsPojo current=productsPojoList.get(position);
                String imei_no=current.imei;


                Intent i=new Intent(context,MainPageScanActivity.class);
                i.putExtra("stock_pagae","STOCK");
                i.putExtra("Received IMEI",imei_no);
                context.startActivity(i);

            }
        });










    }


    @Override
    public int getItemCount() {
        return productsPojoList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView imeino_tv,product_tv,brand_tv,from_tv;
        public TextView tv1,tv2,tv3,tv4;
        public LinearLayout receive_lnr;

        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            //Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");
            regular= Typeface.createFromAsset(context.getAssets(),"fonts/ProximaNova-Regular.ttf");
            bold= Typeface.createFromAsset(context.getAssets(),"fonts/ProximaNova-Bold.ttf");
            extraBold= Typeface.createFromAsset(context.getAssets(),"fonts/ProximaNova-Extrabld.ttf");
            light= Typeface.createFromAsset(context.getAssets(),"fonts/ProximaNova-Light.ttf");
            thin= Typeface.createFromAsset(context.getAssets(),"fonts/ProximaNovaT-Thin.ttf");
            quondo= Typeface.createFromAsset(context.getAssets(),"fonts/Realistica Demo.otf");


            imeino_tv=itemView.findViewById(R.id.imeino_tv);
            product_tv=itemView.findViewById(R.id.product_tv);
            brand_tv=itemView.findViewById(R.id.brand_tv);
            from_tv=itemView.findViewById(R.id.from_tv);
            receive_lnr=itemView.findViewById(R.id.receive_lnr);

            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);
            tv3=itemView.findViewById(R.id.tv3);
            tv4=itemView.findViewById(R.id.tv4);


            imeino_tv.setTypeface(regular);
            product_tv.setTypeface(regular);
            brand_tv.setTypeface(regular);
            from_tv.setTypeface(regular);

            tv1.setTypeface(regular);
            tv2.setTypeface(regular);
            tv3.setTypeface(regular);
            tv4.setTypeface(regular);

        }
    }
}
