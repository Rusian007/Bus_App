package com.example.busapp.Adaptar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Model.BusSeatListModel;
import com.example.busapp.R;

import java.util.List;

public class BusSeatAdapter extends RecyclerView.Adapter<BusSeatAdapter.ViewHolder> {

    Context context;
    List<BusSeatListModel> BusSeatNameList;

    public BusSeatAdapter(Context context, List<BusSeatListModel> busSeatNameList) {
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
            BusSeatListModel model = BusSeatNameList.get(position);

            holder.col1Text.setText(checkNameForX(model.getSeatCol1(),holder.col1Text));
            holder.col2Text.setText(checkNameForX(model.getSeatCol2(), holder.col2Text));
            holder.col3Text.setText(checkNameForX(model.getSeatCol3(), holder.col3Text));
            holder.col4Text.setText(checkNameForX(model.getSeatCol4(), holder.col4Text));
            holder.col5Text.setText(checkNameForX(model.getSeatCol5(), holder.col5Text));

        } else {
            return;
        }

        holder.col1Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FROM ON BIND VIEW HOLDER", "on bindCalled :)   "+ holder.col1Text.getText());
            }
        });

        holder.col3Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FROM ON BIND VIEW HOLDER", "on bindCalled :)   "+ holder.col3Text.getText());
            }
        });

    }

    private String checkNameForX(String seat, TextView colText) {

        if (seat.equals("X")){
            colText.setVisibility(View.INVISIBLE);
            return " ";
        }else {
            return seat;
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
