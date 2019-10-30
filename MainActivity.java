package com.hadar.finalproject;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static RecyclerView flightsRecyclerView;
    public static FlightsAdapter myAdapter;
    private RecyclerView.LayoutManager linearLayoutManager;
    public static ArrayList<FlightDataItem> myAllFlightsList = new ArrayList<>();
    public static ArrayList<FlightDataItem> myLandedFlightsList = new ArrayList<>();
    public static ArrayList<FlightDataItem> myFlyingFlightsList = new ArrayList<>();
    public static final int REQUEST_CODE = 1;
    public static String landingTime;
    public static DataSnapshot dataSnapshot;
    private boolean landedOnly;
    private boolean flyingOnly;
    private FirebaseDatabase myDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // change the menu icon to something else...
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.ic_menu_black_24dp));

        setSupportActionBar(toolbar);

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        landedOnly = getPrefs.getBoolean("onlyLandedFlights", false);
        flyingOnly = getPrefs.getBoolean("onlyFlyingFlights", false);

        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        getBaseContext().registerReceiver(new ConnectivityReceiver(this), intentFilter);

        bringDataFromFirebase();
        buildRecyclerView();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                TextView realTime = findViewById(R.id.realTime);
                                long date = System.currentTimeMillis();

                                String pattern = "HH:mm:ss";
                                SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);

                                String timeString = sdf.format(date);
//                                realTime.setText(timeString);

                                landingTime = "";
                                for (int i = 0; i < myFlyingFlightsList.size(); i++) {
                                    landingTime = myFlyingFlightsList.get(i).getRealLandingTime();
                                    landingTime = landingTime + ":00";
                                    if (timeString.equals(landingTime)) {
                                        String expectedCity = myFlyingFlightsList.get(i).getCity();
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String currentCity = child.child("city").getValue().toString();
                                            if (currentCity.equals(expectedCity)) {
                                                String currentKey = child.getKey();
                                                myDatabase.getReference().child("landings").child(currentKey).child("status").setValue("landed");
                                                buildRecyclerView();
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                } catch (InterruptedException ignored) {

                }
            }
        };
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent gotoSettings = new Intent(this, MyPreferencesActivity.class);
            startActivityForResult(gotoSettings, REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                landedOnly = data.getBooleanExtra(MyPreferencesActivity.LANDED_ONLY, false);
                flyingOnly = data.getBooleanExtra(MyPreferencesActivity.FLYING_ONLY, false);

                buildRecyclerView();
            }
        }
    }

    public void buildRecyclerView() {

        flightsRecyclerView = findViewById(R.id.flights_recycle_view);
        linearLayoutManager = new LinearLayoutManager(this);
        flightsRecyclerView.setLayoutManager(linearLayoutManager);

        if (landedOnly) {
            myAdapter = new FlightsAdapter(myLandedFlightsList);
        } else if (flyingOnly) {
            myAdapter = new FlightsAdapter(myFlyingFlightsList);
        } else {
            //both landed and flying
            myAdapter = new FlightsAdapter(myAllFlightsList);
        }

        flightsRecyclerView.setAdapter(myAdapter);
    }

    private void bringDataFromFirebase() {
        myDatabase.getReference().child("landings").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        MainActivity.dataSnapshot = dataSnapshot;
                        takeUpdatedData(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("=========== ", "Firebase error " + databaseError);
                    }
                }
        );
    }

    public static String getTime() {
        String time;
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm", Locale.US);
        time = mdformat.format(calender.getTime());
        return time;
    }

    public static boolean compareTime(String time1, String time2) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);

        Date thisTime = null;
        try {
            thisTime = sdf.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date landingTime = null;
        try {
            landingTime = sdf.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert thisTime != null;
        return !landingTime.after(thisTime);
    }

    private void takeUpdatedData(DataSnapshot dataSnapshot) {
        clearLists();
        setData(dataSnapshot);
        myAdapter.notifyDataSetChanged();
    }

    private void clearLists() {
        myAllFlightsList.clear();
        myLandedFlightsList.clear();
        myFlyingFlightsList.clear();
    }

    private void setData(DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            FlightDataItem flightDataItem = new FlightDataItem();

            String expectedLandingTime = child.child("expectedLandingTime").getValue().toString();
            String city = child.child("city").getValue().toString();
            String airport = child.child("airport").getValue().toString();
            String flightNumber = child.child("flightNumber").getValue().toString();
            String realLandingTime = child.child("realLandingTime").getValue().toString();
            String landingStatus = child.child("status").getValue().toString();

            flightDataItem.setExpectedLandingTime(expectedLandingTime);
            flightDataItem.setCity(city);
            flightDataItem.setAirport(airport);
            flightDataItem.setFlightNumber(flightNumber);
            flightDataItem.setRealLandingTime(realLandingTime);
            flightDataItem.setLandingStatus(landingStatus);

            myAllFlightsList.add(flightDataItem);

            if (compareTime(getTime(), flightDataItem.getRealLandingTime())) {
                myLandedFlightsList.add(flightDataItem);
            } else {
                myFlyingFlightsList.add(flightDataItem);
            }
        }
    }
}
