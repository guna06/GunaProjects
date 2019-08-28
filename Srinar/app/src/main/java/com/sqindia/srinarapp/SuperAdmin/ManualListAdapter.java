package com.sqindia.srinarapp.SuperAdmin;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sqindia.srinarapp.Model.ManualReportList;
import com.sqindia.srinarapp.R;


import java.util.List;


/**
 * Created by Guna on 29-11-2017.
 */

public class ManualListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<ManualReportList> manualReportLists;
    SharedPreferences.Editor editor;
    String token,get_batchid,get_quantity,session_token,str_dept,str_name,str_role;
    Typeface lato;

    public ManualListAdapter(ViewManualReport viewManualReport, List<ManualReportList> manualReportLists) {

        this.context=viewManualReport;
        this.manualReportLists=manualReportLists;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_manual_report, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final ManualReportList current=manualReportLists.get(position);

        String year=current.part_year;
        String month=current.part_month;
        String upToNCharacters = month.substring(0, Math.min(month.length(), 3));
        myHolder.month_tv.setText(upToNCharacters+"/"+year);
        myHolder.initial_tv.setText(current.initial_qty);
        myHolder.mc_tv.setText(current.m_completed);
        //myHolder.ma_tv.setText(current.m_approved);
        myHolder.ec_tv.setText(current.p_completed);
        //myHolder.ea_tv.setText(current.p_approved);
        myHolder.ac_tv.setText(current.a_completed);
        //myHolder.aa_tv.setText(current.a_approved);
        myHolder.subpart_tv.setText(current.k_subpart);





      /*  int mc,ec,ac,ma,ea,aa,mp,ep,ap;
        mc=Integer.parseInt(myHolder.mc_tv.getText().toString());
        ma=Integer.parseInt(myHolder.ma_tv.getText().toString());
        mp=mc-ma;
        myHolder.mp_tv.setText(String.valueOf(mp));


       ec=Integer.parseInt(myHolder.ec_tv.getText().toString());
        ea=Integer.parseInt(myHolder.ea_tv.getText().toString());
        ep=ec-ea;
        myHolder.ep_tv.setText(String.valueOf(ep));


        ac=Integer.parseInt(myHolder.ac_tv.getText().toString());
        aa=Integer.parseInt(myHolder.aa_tv.getText().toString());
        ap=ac-aa;
        myHolder.ap_tv.setText(String.valueOf(ap));*/
    }


    @Override
    public int getItemCount() {
        return manualReportLists.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView month_tv,initial_tv,mc_tv,ma_tv,ec_tv,ea_tv,ac_tv,aa_tv,ap_tv,mp_tv,ep_tv,subpart_tv;
        // create constructor to get widget reference

        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");
            month_tv = itemView.findViewById(R.id.month_tv);
            initial_tv = itemView.findViewById(R.id.initial_tv);
            mc_tv=itemView.findViewById(R.id.mc_tv);
            ma_tv = itemView.findViewById(R.id.ma_tv);
            ec_tv =itemView.findViewById(R.id.ec_tv);
            ea_tv=itemView.findViewById(R.id.ea_tv);
            aa_tv=itemView.findViewById(R.id.aa_tv);
            ac_tv=itemView.findViewById(R.id.ac_tv);
            ap_tv=itemView.findViewById(R.id.ap_tv);
            initial_tv=itemView.findViewById(R.id.initial_tv);

            mp_tv=itemView.findViewById(R.id.mp_tv);
            ep_tv=itemView.findViewById(R.id.ep_tv);
            ap_tv=itemView.findViewById(R.id.ap_tv);
            subpart_tv=itemView.findViewById(R.id.subpart_tv);


            month_tv.setTypeface(tf);
            initial_tv.setTypeface(tf);
            mc_tv.setTypeface(tf);
            ma_tv.setTypeface(tf);
            ec_tv.setTypeface(tf);
            ea_tv.setTypeface(tf);
            initial_tv.setTypeface(tf);
            aa_tv.setTypeface(tf);
            ap_tv.setTypeface(tf);
            ac_tv.setTypeface(tf);
            subpart_tv.setTypeface(tf);

            mp_tv.setTypeface(tf);
            ep_tv.setTypeface(tf);
            ap_tv.setTypeface(tf);

        }
    }
}
