package com.example.busapp.Adaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Model.BusSeatModel;
import com.example.busapp.R;

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
        View view = LayoutInflater.from(context).inflate(R.layout.bus_seat_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusSeatAdapter.ViewHolder holder, int position) {

        if(BusSeatNameList != null && BusSeatNameList.size()>0 ){
            BusSeatModel model = BusSeatNameList.get(position);
            holder.col1Text.setText(model.getSeat1());
            holder.col2Text.setText(model.getSeat2());

        } else {
            return;
        }

    }

    @Override
    public int getItemCount() {
        return BusSeatNameList.size();
    }

   public class ViewHolder extends RecyclerView.ViewHolder{

        TextView col1Text, col2Text, col3Text, col4Text, col5Text;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);

           col1Text = itemView.findViewById(R.id.col1Text);
           col2Text = itemView.findViewById(R.id.col2Text);
           col3Text = itemView.findViewById(R.id.col3Text);
           col4Text = itemView.findViewById(R.id.col4Text);
           col5Text = itemView.findViewById(R.id.col5Text);
       }
   }
}
