package autohubb.vinture.com.autohubb.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.model.ShowDriver;
import autohubb.vinture.com.autohubb.model.SingleCarDetails;

/**
 * Created by Guna on 29-11-2017.
 */

public class DriverAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<ShowDriver> showDriverList;

    public DriverAdapter(Context applicationContext, List<ShowDriver> driverAdapterList) {
        this.context=applicationContext;
        this.showDriverList=driverAdapterList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.driver_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final ShowDriver current=showDriverList.get(position);

        myHolder.driver_fname.setText(current.driver_fname);
        myHolder.driver_lname.setText(current.driver_lname);
        myHolder.driver_email.setText(current.driver_email);
        myHolder.driver_phone.setText(current.driver_phone);
        myHolder.driver_city.setText(current.driver_city);
        myHolder.driver_state.setText(current.driver_state);


        myHolder.edit_driver_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Under Development",Toast.LENGTH_LONG).show();
                final ShowDriver current=showDriverList.get(position);
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_driver);

                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);
                Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");
                TextView fname,lname,email,phone,city,state;

                fname=dialog.findViewById(R.id.fname);
                lname=dialog.findViewById(R.id.lname);
                email=dialog.findViewById(R.id.email);
                phone=dialog.findViewById(R.id.phone);
                city=dialog.findViewById(R.id.city);
                state=dialog.findViewById(R.id.state);

                fname.setText(current.driver_fname);
                lname.setText(current.driver_lname);
                email.setText(current.driver_email);
                phone.setText(current.driver_phone);
                city.setText(current.driver_city);
                state.setText(current.driver_state);

                fname.setTypeface(tf);
                lname.setTypeface(tf);
                email.setTypeface(tf);
                phone.setTypeface(tf);
                city.setTypeface(tf);
                state.setTypeface(tf);

                dialog.show();

            }
        });



        Glide.with(context)
                .load(R.drawable.driver_image)
                .apply(RequestOptions.circleCropTransform())
                .into(myHolder.driver_logo);
    }


    @Override
    public int getItemCount() {
        return showDriverList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView driver_fname,driver_lname,driver_phone,driver_email,driver_city,driver_state;
        public ImageView driver_logo;
        LinearLayout edit_driver_details;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);

            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helevetical.ttf");
            driver_fname=itemView.findViewById(R.id.driver_fname);
            driver_lname=itemView.findViewById(R.id.driver_lname);
            driver_phone=itemView.findViewById(R.id.driver_phone);
            driver_email=itemView.findViewById(R.id.driver_email);
            driver_city=itemView.findViewById(R.id.driver_city);
            driver_state=itemView.findViewById(R.id.driver_state);
            driver_logo=itemView.findViewById(R.id.driver_logo);
            edit_driver_details=itemView.findViewById(R.id.edit_driver_details);

            driver_fname.setTypeface(tf);
            driver_lname.setTypeface(tf);
            driver_phone.setTypeface(tf);
            driver_email.setTypeface(tf);
            driver_city.setTypeface(tf);
            driver_state.setTypeface(tf);
        }
    }
}
