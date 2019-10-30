package com.hadar.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class ConnectivityReceiver extends BroadcastReceiver {

    private MainActivity _ma;

    public ConnectivityReceiver() {
    }

    public ConnectivityReceiver(MainActivity ma) {
        _ma = ma;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {

//        Toast.makeText(context, "Flight mode Changed!", Toast.LENGTH_SHORT).show();
        boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
//
        if (isAirplaneModeOn) {
//            Toast.makeText(context, "Flight mode on!", Toast.LENGTH_SHORT).show();
            // handle Airplane Mode on
            _ma.flightsRecyclerView.setAlpha(0.75f);
            _ma.flightsRecyclerView.setBackgroundColor(Color.GRAY);
            _ma.flightsRecyclerView.setEnabled(false);
        } else {
//            Toast.makeText(context, "Flight mode off!", Toast.LENGTH_SHORT).show();
            // handle Airplane Mode off
            _ma.flightsRecyclerView.setAlpha(1f);
            _ma.flightsRecyclerView.setBackgroundColor(Color.WHITE);
            _ma.flightsRecyclerView.setEnabled(true);
        }
    }
}
