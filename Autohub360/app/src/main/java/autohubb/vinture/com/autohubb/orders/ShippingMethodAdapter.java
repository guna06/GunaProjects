package autohubb.vinture.com.autohubb.orders;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.model.ShippingMethodPojo;

/**
 * Created by Guna on 29-11-2017.
 */

public class ShippingMethodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<ShippingMethodPojo> shippingMethodPojos;


    public ShippingMethodAdapter(Context applicationContext, List<ShippingMethodPojo> shippingMethodPojoArrayList) {
        this.context=applicationContext;
        this.shippingMethodPojos = shippingMethodPojoArrayList;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.shipping_method_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final ShippingMethodPojo current= shippingMethodPojos.get(position);

            RadioButton btn = new RadioButton(context);
            btn.setId(position + 1);
            final String itemName = shippingMethodPojos.get(position).shipping;


            btn.setText(itemName);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        myHolder.radioGroup.addView(btn, params);
//        myHolder.radioGroup.addView(btn);



//        myHolder.radio_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b)
//                {
//                    ShippingMethod.selectedItems.add(current.shipping_method_id);
//                }else
//                {
//                    ShippingMethod.selectedItems.remove(current.shipping_method_id);
//                }
//            }
//        });
        //myHolder.cost_tv.setText(current.cost);
    }


    @Override
    public int getItemCount() {
        return shippingMethodPojos.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public RadioButton radio_id;
        public RadioGroup radioGroup;

        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");

//            ship_tv=itemView.findViewById(R.id.ship_tv);
            radioGroup=itemView.findViewById(R.id.radiogroup);

        }
    }
}
