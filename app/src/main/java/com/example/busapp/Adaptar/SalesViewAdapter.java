package com.example.busapp.Adaptar;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.busapp.Model.SalesModel;
import com.example.busapp.R;

import java.util.ArrayList;

public class SalesViewAdapter extends RecyclerView.Adapter<SalesViewAdapter.ViewHolder>{
    private ArrayList<SalesModel> SalesModelList;
    Context context;

    public SalesViewAdapter(Context context, ArrayList<SalesModel> SalesList){
        this.context = context;
        this.SalesModelList = SalesList;
    }
    public void clearData() {
        SalesModelList.clear(); // Assuming SalesInfoList is your data list
        notifyDataSetChanged(); // Notify RecyclerView to update its view
    }


    @NonNull
    @Override
    public SalesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_info_card, parent, false);
        SalesViewAdapter.ViewHolder viewHolder = new SalesViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SalesViewAdapter.ViewHolder holder, int position) {
        holder.info.setText(SalesModelList.get(position).getInformation());
        if (SalesModelList.get(position).isSetBold()) {
            // Set text to bold
            holder.info.setTypeface(holder.info.getTypeface(), Typeface.BOLD);
        } else {
            // Set text to normal
            holder.info.setTypeface(holder.info.getTypeface(), Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return SalesModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView info;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.info = itemView.findViewById(R.id.info);
        }
    }
}
