package autohubb.vinture.com.autohubb.Shop;


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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import autohubb.vinture.com.autohubb.R;

/**
 * Created by Guna on 29-11-2017.
 */

public class ShopListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    List<ShopList> shoppingItemList;


    public ShopListAdapter(FragmentActivity activity, List<ShopList> shoppingItemList) {
        this.context=activity;
        this.shoppingItemList=shoppingItemList;
        inflater = LayoutInflater.from(context);
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.shopping_item_child_ui, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final ShopList current=shoppingItemList.get(position);

        final String name=current.name;
        final String price=current.price;
        final String image=current.image;


        myHolder.item_name.setText(name);
        myHolder.item_price.setText(price);




        Glide.with(context)
                .load(image)
                .into(myHolder.item_img);

        myHolder.wholeset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ShopList current=shoppingItemList.get(position);
                String id=current.id;
                String image=current.image;
                String name=current.name;
                String desc=current.description;
                String price=current.price;
                String stock=current.currentStock;
                String brand=current.brand;
                String partno=current.partno;
                String included=current.included;


                Intent order_service=new Intent(context,SingleItemActivity.class);
                order_service.putExtra("purchase_id",id);
                order_service.putExtra("purchase_image",image);
                order_service.putExtra("purchase_name",name);
                order_service.putExtra("purchase_desc",desc);
                order_service.putExtra("purchase_price",price);
                order_service.putExtra("purchase_stock",stock);
                order_service.putExtra("brand",brand);
                order_service.putExtra("partno",partno);
                order_service.putExtra("included",included);
                context.startActivity(order_service);

            }
        });

    }


    @Override
    public int getItemCount() {
        return shoppingItemList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView item_name,item_price;
        public ImageView item_img;
        RelativeLayout wholeset;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");

            item_name=itemView.findViewById(R.id.item_name);
            item_img=itemView.findViewById(R.id.item_img);
            item_price=itemView.findViewById(R.id.item_price);
            wholeset=itemView.findViewById(R.id.wholeset);



            item_name.setTypeface(tf);
            item_price.setTypeface(tf);



        }
    }
}
