package com.example.busapp.Adaptar;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Booking.SelectSeatActivity;
import com.example.busapp.Model.BusModal;
import com.example.busapp.R;

import java.util.ArrayList;

public class BusLayoutApaptar extends RecyclerView.Adapter<BusLayoutApaptar.ViewHolder> {


    private ArrayList<BusModal> names;
    private Context mContext;
    boolean isClicked = false;

    public BusLayoutApaptar( Context mContext ,ArrayList<BusModal> names) {
        this.names = names;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_bus_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("FROM ON BIND VIEW HOLDER", "on bindCalled : VIEW :)");

        holder.showText.setText(names.get(position).getBusname());

        //On click for each Item
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                int duration = Toast.LENGTH_SHORT;
                if (!isClicked){
                    //holder.itemView.setBackgroundColor(Color.WHITE);
                    holder.showText.setTextColor(Color.BLACK);
                    AnimationDrawable animationDrawable = (AnimationDrawable) holder.itemView.getBackground();
                    animationDrawable.setEnterFadeDuration(500);
                    animationDrawable.setExitFadeDuration(500);
                    animationDrawable.start();
                    isClicked = true;
                } else {
                    holder.showText.setTextColor(Color.WHITE);
                    holder.itemView.setBackgroundColor(Color.BLACK);
                    isClicked = false;
                }

                Intent seatIntent = new Intent(v.getContext(), SelectSeatActivity.class);
                seatIntent.putExtra("BUSNAME", holder.showText.getText().toString());
                v.getContext().startActivity(seatIntent);
                ((Activity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView showText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showText = itemView.findViewById(R.id.recycleText);


        }
    }
}

