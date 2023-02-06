package com.example.busapp.Adaptar;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Model.ShortRoute_LocationModel;
import com.example.busapp.R;

import java.util.ArrayList;

public class FromLocationAdapter extends RecyclerView.Adapter<FromLocationAdapter.ViewHolder> {
    private Context mContext;
    IStartLocation Startlocation;
    private static boolean isSelected = false;
    private static  int seatPos;
    private ArrayList<ShortRoute_LocationModel> locations;


    public FromLocationAdapter(Context mContext, ArrayList<ShortRoute_LocationModel> locations, IStartLocation location) {
        this.mContext = mContext;
        this.locations = locations;
        this.Startlocation = location;
    }

    @NonNull
    @Override
    public FromLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.from_location_card, parent, false);
        ViewHolder viewholder = new ViewHolder(view, Startlocation);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull FromLocationAdapter.ViewHolder holder, int position) {
        ShortRoute_LocationModel location;
        holder.locationText.setText(locations.get(position).getStart_location());
        location = locations.get(position);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // loading Animation from

                    final Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce);
                    view.startAnimation(animation);

                    if (!location.isIs_clicked() && !isSelected){

                        holder.locationText.setTextColor(Color.parseColor("#1cab50"));
                        isSelected = true; // If a seat has already been selected then don't add any more seats to the list
                      // Store the position of the selected seat
                        seatPos = position;

                        Startlocation.AddStartLocation(location.getStart_location());
                        location.setIs_clicked(true);
                    } else if(position == seatPos){ // if the current seat matches the selected seat
                        // then release the selected seat and enable seat clicking again
                        isSelected = false;
                        Startlocation.RemoveStartLocation(location.getStart_location());
                        location.setIs_clicked(false);
                        holder.locationText.setTextColor(Color.WHITE);
                    }

                }
            });


    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationText;
        IStartLocation location;

        public ViewHolder(@NonNull View itemView, IStartLocation loc) {
            super(itemView);
            this.location = loc;
            locationText = itemView.findViewById(R.id.Start_loc);
        }
    }
    public interface IStartLocation{
        boolean AddStartLocation(String locationName);
        void RemoveStartLocation(String locationName);
    }
}
