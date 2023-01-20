package com.example.busapp.Adaptar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Model.BusSeatModel;

import java.util.List;

public class BusSeatAdapter extends RecyclerView.Adapter<BusSeatAdapter.ViewHolder> {

    Context context;
    List<BusSeatModel> BusSeatNameList;

    public BusSeatAdapter(Context context, List<BusSeatModel> busSeatNameList) {
        this.context = context;
        BusSeatNameList = busSeatNameList;
    }

    @NonNull
    @Override
    public BusSeatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BusSeatAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

   public class ViewHolder extends RecyclerView.ViewHolder{

       public ViewHolder(@NonNull View itemView) {
           super(itemView);
       }
   }
}
