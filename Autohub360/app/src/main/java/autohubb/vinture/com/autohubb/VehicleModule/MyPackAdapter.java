package autohubb.vinture.com.autohubb.VehicleModule;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.R;

/**
 * Created by Guna on 29-11-2017.
 */

public class MyPackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    List<List<ServicePackListPojo>> vehiclePartsList;
    List<ServicePackListPojo> vehiclePartsSubList = new ArrayList<>();
    String getProductId,get_parts_id,get_parts_name;

    public MyPackAdapter(Context applicationContext, List<List<ServicePackListPojo>> packsPartsList) {
        this.context=applicationContext;
        this.vehiclePartsList=packsPartsList;
        inflater=LayoutInflater.from(context);

    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pack_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;




        final List<ServicePackListPojo> currentItem = vehiclePartsList.get(position);

        final String categoryName = currentItem.get(0).CategoryName;
        myHolder.parts_head_tv.setText(categoryName);


        for(int i=0;i<currentItem.size();i++) {
            TextView btn = new TextView(context);
            btn.setId(i+1);
            final String itemName = currentItem.get(i).name;
            OrderServicePack.packname=currentItem.get(position).servicePackName;

            btn.setText(itemName);
            final int index = i;
          /*  btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("TAG", "The index is" + index);
                    Log.i("TAG", "The position is" + position);
                    get_parts_id=currentItem.get(index).id;
                    get_parts_name=currentItem.get(index).name;

                  *//*  OrderPartsOne.linear_content1.setVisibility(View.GONE);
                    OrderPartsOne.linear_content2.setVisibility(View.VISIBLE);*//*
                }
            });*/

            myHolder.dynamicLayout.addView(btn);
        }


      myHolder.order_kit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderServicePack.layout_one.setVisibility(View.GONE);
                OrderServicePack.layout_two.setVisibility(View.VISIBLE);
                //final List<ServicePackListPojo> currentItem = vehiclePartsList.get(position);
                OrderServicePack.head_tv.setText(currentItem.get(position).servicePackName);

                OrderServicePack.getProductId=currentItem.get(position).productId;
                Log.e("tag","VIRUS------------------>"+getProductId);


               /* SharedPreferences putLoginData = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor putLoginEditor = putLoginData.edit();
                putLoginEditor.putString("get_parts_id",get_parts_id);
                putLoginEditor.putString("get_parts_name",get_parts_name);
                putLoginEditor.commit();*/


            }
        });



    }


    @Override
    public int getItemCount() {
        Log.e("tag","print size of the array"+vehiclePartsSubList.size());

        return vehiclePartsList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView parts_head_tv,order_kit;
        public LinearLayout dynamicLayout,next_arrow;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");


            parts_head_tv=itemView.findViewById(R.id.parts_head_tv);
            dynamicLayout=itemView.findViewById(R.id.dynamic_layout);
            order_kit=itemView.findViewById(R.id.order_kit);

            parts_head_tv.setTypeface(tf);
            order_kit.setTypeface(tf);
        }
    }
}
