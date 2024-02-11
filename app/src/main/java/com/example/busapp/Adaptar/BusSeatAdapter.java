package com.example.busapp.Adaptar;

import android.content.Context;
import android.graphics.Color;
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
    IBusSeat SeatAdder;

    public BusSeatAdapter(Context context, List<BusSeatListModel> busSeatNameList, IBusSeat Seatadder) {
        this.SeatAdder = Seatadder;
        this.context = context;
        BusSeatNameList = busSeatNameList;
    }

    @NonNull
    @Override
    public BusSeatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bus_seat_card, parent, false);
        return new ViewHolder(view, SeatAdder);
    }

    @Override
    public void onBindViewHolder(@NonNull BusSeatAdapter.ViewHolder holder, int position) {
        BusSeatListModel model;
        if (BusSeatNameList != null && BusSeatNameList.size() > 0) {
            model = BusSeatNameList.get(position);

            holder.col1Text.setText(checkNameForX(model.getSeatCol1(), holder.col1Text));
            holder.col2Text.setText(checkNameForX(model.getSeatCol2(), holder.col2Text));
            holder.col3Text.setText(checkNameForX(model.getSeatCol3(), holder.col3Text));
            holder.col4Text.setText(checkNameForX(model.getSeatCol4(), holder.col4Text));
            holder.col5Text.setText(checkNameForX(model.getSeatCol5(), holder.col5Text));

        } else {
            return;
        }

        SetOnClickListener(model, holder);


    }

    // Onclick Lister for per-seat
    private void SetOnClickListener(BusSeatListModel model, ViewHolder Viewholder) {
        /////////////////////////////////
        if (model.getSeatCol1Status()) {
            Viewholder.col1Text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!model.isSeatCol1Clicked()) {
                        onItemClick(Viewholder.col1Text);
                        model.setIsClickedSeatCol1(true);
                        boolean SeatsRestricted = SeatAdder.AddSeat(Viewholder.col1Text.getText().toString());

                        if (SeatsRestricted) {
                            onItemUnClick(Viewholder.col1Text);
                        }
                    } else {
                        model.setIsClickedSeatCol1(false);
                        onItemUnClick(Viewholder.col1Text);
                    }

                }
            });
        } else {
            Viewholder.col1Text.setTextColor(Color.parseColor("#ff0000"));
            Viewholder.col1Text.setBackgroundColor(Color.BLACK);
        }

        /////////////////////////////////
        if (model.getSeatCol2Status()) {
            Viewholder.col2Text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!model.isSeatCol2Clicked()) {
                        onItemClick(Viewholder.col2Text);
                        model.setIsClickedSeatCol2(true);
                        boolean SeatsRestricted = SeatAdder.AddSeat(Viewholder.col2Text.getText().toString());

                        if (SeatsRestricted) {
                            onItemUnClick(Viewholder.col2Text);
                        }
                    } else {
                        model.setIsClickedSeatCol2(false);
                        onItemUnClick(Viewholder.col2Text);
                    }

                }
            });
        } else {
            Viewholder.col2Text.setTextColor(Color.parseColor("#ff0000"));
            Viewholder.col2Text.setBackgroundColor(Color.BLACK);
        }

        /////////////////////////////////
        if (model.getSeatCol3Status()) {
            Viewholder.col3Text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!model.isSeatCol3Clicked()) {
                        onItemClick(Viewholder.col3Text);
                        model.setIsClickedSeatCol3(true);
                        boolean SeatsRestricted = SeatAdder.AddSeat(Viewholder.col3Text.getText().toString());

                        if (SeatsRestricted) {
                            onItemUnClick(Viewholder.col3Text);
                        }
                    } else {
                        model.setIsClickedSeatCol3(false);
                        onItemUnClick(Viewholder.col3Text);
                    }

                }
            });
        } else {
            Viewholder.col3Text.setTextColor(Color.parseColor("#ff0000"));
            Viewholder.col3Text.setBackgroundColor(Color.BLACK);
        }

        /////////////////////////////////
        if (model.getSeatCol4Status()) {
            Viewholder.col4Text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!model.isSeatCol4Clicked()) {
                        onItemClick(Viewholder.col4Text);
                        model.setIsClickedSeatCol4(true);
                        boolean SeatsRestricted = SeatAdder.AddSeat(Viewholder.col4Text.getText().toString());

                        if (SeatsRestricted) {
                            onItemUnClick(Viewholder.col4Text);
                        }
                    } else {
                        model.setIsClickedSeatCol4(false);
                        onItemUnClick(Viewholder.col4Text);
                    }

                }
            });
        } else {
            Viewholder.col4Text.setTextColor(Color.parseColor("#ff0000"));
            Viewholder.col4Text.setBackgroundColor(Color.BLACK);
        }

        /////////////////////////////////
        if (model.getSeatCol5Status()) {
            Viewholder.col5Text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!model.isSeatCol5Clicked()) {
                        onItemClick(Viewholder.col5Text);
                        model.setIsClickedSeatCol5(true);
                        boolean SeatsRestricted = SeatAdder.AddSeat(Viewholder.col5Text.getText().toString());

                        if (SeatsRestricted) {
                            onItemUnClick(Viewholder.col5Text);
                        }
                    } else {
                        model.setIsClickedSeatCol5(false);
                        onItemUnClick(Viewholder.col5Text);
                    }

                }
            });
        } else {
            Viewholder.col5Text.setTextColor(Color.parseColor("#ff0000"));
            Viewholder.col5Text.setBackgroundColor(Color.BLACK);
        }
    }

    private void onItemUnClick(TextView text) {
        SeatAdder.AddSeat(text.getText().toString());
        text.setTextColor(Color.WHITE);
        text.setBackgroundColor(Color.parseColor("#242230"));
    }


    // Action if per-seat is clicked
    private void onItemClick(TextView text) {
        text.setTextColor(Color.parseColor("#1cab50"));
        text.setBackgroundColor(Color.BLACK);
    }

    // Set visibility for "X" seats
    private String checkNameForX(String seat, TextView colText) {

        if (seat.equals("X")) {
            colText.setVisibility(View.INVISIBLE);
            return " ";
        } else {
            return seat;
        }

    }

    @Override
    public int getItemCount() {
        return BusSeatNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView col1Text, col2Text, col3Text, col4Text, col5Text;
        IBusSeat SeatAdder;

        public ViewHolder(@NonNull View itemView, IBusSeat SeatAdder) {
            super(itemView);
            this.SeatAdder = SeatAdder;
            col1Text = itemView.findViewById(R.id.col1Text);
            col2Text = itemView.findViewById(R.id.col2Text);
            col3Text = itemView.findViewById(R.id.col3Text);
            col4Text = itemView.findViewById(R.id.col4Text);
            col5Text = itemView.findViewById(R.id.col5Text);
        }
    }

    public interface IBusSeat {
        boolean AddSeat(String seatname);
    }
}
