package com.sqindia.autolane360mobile.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.sqindia.autolane360mobile.R;
import com.sqindia.autolane360mobile.activity.ImageActivity;
import com.sqindia.autolane360mobile.model.ImageList;


import java.util.List;

/**
 * Created by Guna on 29-11-2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater1;
    private List<ImageList> imageLists;

    public ImageAdapter(ImageActivity imageActivity, List<ImageList> imagePartsList) {


        this.context=imageActivity;
        this.imageLists=imagePartsList;
        inflater1 = LayoutInflater.from(context);
    }

   /* public ImageAdapter(CarPartsList carPartsList, List<PartsList> carPartsLists) {
        inflater1 = LayoutInflater.from(carPartsList);
        this.context=carPartsList;
        this.partsLists=carPartsLists;


    }
*/

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
        final ImageList current=imageLists.get(position);

        String carpic_path=  current.img_admincarstockimg;
        Log.e("tag","@@@@@@@@@@@@----->"+carpic_path);


        Glide.with(context).load(carpic_path)
                .into(myHolder.pic_iv);

        myHolder.carname.setText(current.img_imagetype);





    }


    @Override
    public int getItemCount() {

        return imageLists.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView carname;
        ImageView pic_iv;

        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");
            carname =  itemView.findViewById(R.id.carname);
            pic_iv = itemView.findViewById(R.id.pic_iv);

            carname.setTypeface(tf);
            /* subcat_tv.setTypeface(tf);*/
        }
    }

}
