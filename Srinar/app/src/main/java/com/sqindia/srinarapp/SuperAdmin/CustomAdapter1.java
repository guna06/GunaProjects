package com.sqindia.srinarapp.SuperAdmin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sqindia.srinarapp.Model.OperatorList;
import com.sqindia.srinarapp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter1 extends RecyclerView.Adapter<CustomAdapter1.ViewHolder> {
    private ArrayList<String> names;
    private List<OperatorList> operatorLists;

    public CustomAdapter1(ArrayList<String> names, List<OperatorList> operatorListListModel) {

        this.operatorLists=operatorListListModel;
        this.names = names;
    }


    @Override
    public CustomAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomAdapter1.ViewHolder holder, int position) {
        holder.textViewName.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public void filterList(ArrayList<String> filterdNames) {

        this.names = filterdNames;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;

        ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
        }
    }
}
