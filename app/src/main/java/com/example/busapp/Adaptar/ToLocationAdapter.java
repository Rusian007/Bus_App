package com.example.busapp.Adaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Model.ShortRoute_LocationModel;
import com.example.busapp.R;

import java.util.ArrayList;

public class ToLocationAdapter extends RecyclerView.Adapter<ToLocationAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ShortRoute_LocationModel> locations;

    public ToLocationAdapter(Context mContext, ArrayList<ShortRoute_LocationModel> locations) {
        this.mContext = mContext;
        this.locations = locations;
    }

    @NonNull
    @Override
    public ToLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_location_card, parent, false);
        ToLocationAdapter.ViewHolder viewholder = new ToLocationAdapter.ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ToLocationAdapter.ViewHolder holder, int position) {
        holder.locationText.setText(locations.get(position).getStart_location());
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView locationText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationText = itemView.findViewById(R.id.end_loc);
        }
    }
}
