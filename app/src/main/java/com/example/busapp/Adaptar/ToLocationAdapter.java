package com.example.busapp.Adaptar;

import android.content.Context;
import android.graphics.Color;
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

public class ToLocationAdapter extends RecyclerView.Adapter<ToLocationAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ShortRoute_LocationModel> locations;
    private static boolean isSelected = false;


    IEndLocation locationAdder;

    public ToLocationAdapter(Context mContext, ArrayList<ShortRoute_LocationModel> locations, IEndLocation locationAdder) {

      this.locationAdder = locationAdder;
        this.mContext = mContext;
        this.locations = locations;
    }

    @NonNull
    @Override
    public ToLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_location_card, parent, false);
        ToLocationAdapter.ViewHolder viewholder = new ToLocationAdapter.ViewHolder(view, locationAdder);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ToLocationAdapter.ViewHolder holder, int position) {
        ShortRoute_LocationModel location;
        holder.locationText.setText(locations.get(position).getStart_location());
        holder.priceText.setText(locations.get(position).getPrice());
        location = locations.get(position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // loading Animation from

                final Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce);
                view.startAnimation(animation);

                if (!locationAdder.isSelected()){
                    locationAdder.AddDestinationLocation(location.getStart_location(), location.getPrice());

                    holder.locationText.setTextColor(Color.parseColor("#1cab50"));
                    isSelected = true; // If a seat has already been selected then don't add any more seats to the list
                    // Store the position of the selected seat

                    location.setIs_clicked(true);

                } else if( locationAdder.isSelected() ){ // if a location is selected already
                    // then release the selected seat and enable seat clicking again
                    isSelected = false;

                    locationAdder.RemoveDestinationLocation(location.getStart_location());
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

        TextView locationText,priceText;
        IEndLocation locationAdder;

        public ViewHolder(@NonNull View itemView, IEndLocation locationAdder) {
            super(itemView);
            this.locationAdder = locationAdder;
            this.priceText = itemView.findViewById(R.id.end_price);
            locationText = itemView.findViewById(R.id.end_loc);
        }
    }

    public interface IEndLocation {
        boolean AddDestinationLocation(String locationName, String locationPrice);
        void RemoveDestinationLocation(String locationName);
        boolean isSelected();

    }
}
