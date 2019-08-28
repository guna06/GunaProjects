package autohubb.vinture.com.autohubb.orders;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.VehicleModule.OrderPartsOne;
import autohubb.vinture.com.autohubb.model.VehicleParts;
import es.dmoral.toasty.Toasty;

/**
 * Created by Guna on 29-11-2017.
 */

public class OrderPartsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    List<List<VehicleParts>> vehiclePartsList;
    List<VehicleParts> vehiclePartsSubList = new ArrayList<>();
    String get_parts_id,get_parts_name;


    public OrderPartsAdapter(Context applicationContext, List<List<VehicleParts>> vehicleParts) {
        this.context=applicationContext;
        this.vehiclePartsList=vehicleParts;
        inflater=LayoutInflater.from(context);
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_parts_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;




        final List<VehicleParts> currentItem = vehiclePartsList.get(position);

        final String categoryName = currentItem.get(0).CategoryName;
        myHolder.parts_head_tv.setText(categoryName);


        for(int i=0;i<currentItem.size();i++) {
            RadioButton btn = new RadioButton(context);
            btn.setId(i+1);
            final String itemName = currentItem.get(i).name;


            btn.setText(itemName);
            final int index = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.i("TAG", "The position is" + position);
                    get_parts_id=currentItem.get(index).id;
                    get_parts_name=currentItem.get(index).name;
                    //Toast.makeText(context, "You selected the item "+itemName+" from the category "+categoryName+" "+currentItem.get(index).id, Toast.LENGTH_SHORT).show();

                }
            });

            myHolder.dynamicLayout.addView(btn);
        }


        myHolder.next_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG", "The index is" + get_parts_id);

                if(get_parts_id !=null)
                {
                    OrderPartsOne.linear_content1.setVisibility(View.GONE);
                    OrderPartsOne.linear_content2.setVisibility(View.VISIBLE);
                    OrderPartsOne.getProductId=get_parts_id;
                }else
                {
                    Toasty.info(context,"please select any parts item",Toasty.LENGTH_SHORT).show();
                }


              /*  SharedPreferences putLoginData = PreferenceManager.getDefaultSharedPreferences(context);
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
        public TextView parts_head_tv;
        public LinearLayout dynamicLayout,next_arrow;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");


            parts_head_tv=itemView.findViewById(R.id.parts_head_tv);
            dynamicLayout=itemView.findViewById(R.id.dynamic_layout);
            next_arrow=itemView.findViewById(R.id.next_arrow);

            parts_head_tv.setTypeface(tf);
        }
    }
}
