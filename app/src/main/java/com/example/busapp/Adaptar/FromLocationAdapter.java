package com.example.busapp.Adaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Model.ShortRoute_FromLocation_Model;
import com.example.busapp.R;

import java.util.ArrayList;

public class FromLocationAdapter extends RecyclerView.Adapter<FromLocationAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ShortRoute_FromLocation_Model> locations;

    public FromLocationAdapter(Context mContext, ArrayList<ShortRoute_FromLocation_Model> locations) {
        this.mContext = mContext;
        this.locations = locations;
    }

    @NonNull
    @Override
    public FromLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.from_location_card, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull FromLocationAdapter.ViewHolder holder, int position) {
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
            locationText = itemView.findViewById(R.id.Start_loc);
        }
    }
}
