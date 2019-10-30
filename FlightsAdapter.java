package com.hadar.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.FlightsViewHolder> {
    private ArrayList<FlightDataItem> flightsList;

    FlightsAdapter(ArrayList<FlightDataItem> list) {
        flightsList = list;
    }

    @NonNull
    @Override
    public FlightsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View oneItemLayout = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.one_item_layout, parent, false);
        return new FlightsViewHolder(oneItemLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightsViewHolder holder, int position) {
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        FlightDataItem currentFlight = flightsList.get(position);

        holder.expectedLandingTime.setText(currentFlight.getExpectedLandingTime());
        holder.city.setText(String.valueOf(currentFlight.getCity()));
        holder.airport.setText(currentFlight.getAirport());
        holder.flightLogo.setImageResource(currentFlight.getLogoID());
        holder.flightNumber.setText(currentFlight.getFlightNumber());

        if (MainActivity.compareTime(MainActivity.getTime(), currentFlight.getRealLandingTime())) {
            holder.landingTimeText.setText(R.string.landed);
            holder.realLandingTime.setText(currentFlight.getRealLandingTime());
        } else {
            holder.landingTimeText.setText("");
            holder.realLandingTime.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return flightsList.size();
    }

    class FlightsViewHolder extends RecyclerView.ViewHolder {

        TextView expectedLandingTime;
        TextView city;
        TextView airport;
        ImageView flightLogo;
        TextView flightNumber;
        TextView landingTimeText;
        TextView realLandingTime;

        FlightsViewHolder(@NonNull View itemView) {
            super(itemView);
            expectedLandingTime = itemView.findViewById(R.id.flight_expected_landing_time);
            city = itemView.findViewById(R.id.flight_city);
            airport = itemView.findViewById(R.id.flight_airport_name);
            flightLogo = itemView.findViewById(R.id.flight_logo);
            flightNumber = itemView.findViewById(R.id.flight_number);
            landingTimeText = itemView.findViewById(R.id.landed);
            realLandingTime = itemView.findViewById(R.id.flight_real_landing_time);
        }
    }
}
