package com.example.parkingappv2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingappv2.Constants;
import com.example.parkingappv2.MyApi;
import com.example.parkingappv2.R;
import com.example.parkingappv2.adapters.ParkingSpotAdapter;
import com.example.parkingappv2.models.Availability;
import com.example.parkingappv2.models.ParkingSpot;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowParkingActivity extends AppCompatActivity {
    private static final String TAG = "ShowParkingActivity";
    public FloatingActionButton add_button;
    private RecyclerView mRecyclerView;
    private ParkingSpotAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<ParkingSpot> mParkingSpots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_parking);

        //update UI
        show_parking();

        add_button = (FloatingActionButton) findViewById(R.id.add_parking_button);
        add_button.setOnClickListener(v -> {
            Log.d(TAG, "Add Parking Button Pressed");
            add_parking();
        });

    }//oc


    private void show_parking() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Call<List<ParkingSpot>> call = getRetrofit(gson).getParking(
                Constants.bearerToken
        );
        call.enqueue(new Callback<List<ParkingSpot>>() {
            @Override
            public void onResponse(@NotNull Call<List<ParkingSpot>> call, @NotNull Response<List<ParkingSpot>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().size() == 0) {
                        Toast.makeText(ShowParkingActivity.this, "You have no ParkingSpots attached", Toast.LENGTH_SHORT).show();
                    }
                    mParkingSpots = new ArrayList<>();
                    mParkingSpots.clear();
                    mParkingSpots.addAll(response.body());
                    Log.e(TAG, "onResponse: got parking slots of total: " + response.body().size());

                    final List<ParkingSpot> parkingSpots = SortSpots(response.body());

                    buildRecyclerView(parkingSpots);
                }
            }//res

            @Override
            public void onFailure(@NotNull Call<List<ParkingSpot>> call, @NotNull Throwable t) {
                Toast.makeText(ShowParkingActivity.this, Constants.fail_showparking, Toast.LENGTH_SHORT).show();
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }//end show parking


    private void buildRecyclerView(List<ParkingSpot> parkingList) {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ShowParkingActivity.this);
        mAdapter = new ParkingSpotAdapter(parkingList, ShowParkingActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ParkingSpotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mAdapter.notifyItemChanged(position);
            }

            @Override
            public void finish() {
                ShowParkingActivity.this.finish();
            }

            @Override
            public void refreshActivity() {
                Toast.makeText(getApplicationContext(), Constants.success_parking_delete, Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });
    }

    private void add_parking() {
        Intent coordinates_intent = new Intent(this, CoordinatesActivity.class);
        finish();
        startActivity(coordinates_intent);
    }

    //______________________________________________________________________________________________
    private MyApi getRetrofit(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(MyApi.class);
    }//end get

    private List<ParkingSpot> SortSpots(List<ParkingSpot> timeSlots) {
        Log.e(TAG, "isParkingAvailable: validating parking slots...");

        final int availableSlots = timeSlots.size();
        final int availableParkingSpots = mParkingSpots.size();
        List<ParkingSpot> filteredParkingSpots = new ArrayList<>();
        filteredParkingSpots.clear();

        for (int i = 0; i < availableParkingSpots; i++)
            if (timeSlots.get(i).getClaimed() == 1) {
                if (!compareTimes(timeSlots.get(i).getStop_date(), getCurrentDateTime())) {
                    Log.e(TAG, "SortSpots1: " + timeSlots.get(i).getStop_date() + " VS " + getCurrentDateTime() + " = " + compareTimes(timeSlots.get(i).getStop_date(), getCurrentDateTime()));
                    unclaim_spot(timeSlots.get(i).getId(), filteredParkingSpots);
                    //delete_availability(timeSlots.get(i).getId());
                }
            }
        for (int i = 0; i < availableParkingSpots; i++) {
            if (timeSlots.get(i).getAvailable() == 1) {
                if (!compareTimes(timeSlots.get(i).getStop_date(), getCurrentDateTime())) {
                    Log.e(TAG, "SortSpots2: " + timeSlots.get(i).getStop_date() + " VS " + getCurrentDateTime() + " = " + compareTimes(timeSlots.get(i).getStop_date(), getCurrentDateTime()));
                    set_parking_available(timeSlots.get(i).getId());
                    unclaim_spot(timeSlots.get(i).getId(), filteredParkingSpots);
                    //delete_availability(timeSlots.get(i).getId());
                    Log.e(TAG, "SortSpots: Availability expirat");
                }
            }
        }

        for (int i = 0; i < availableParkingSpots; i++) {
            if (timeSlots.get(i).getAvailable() == 1) {

                //   finally we add a Parking Spot marked as unposted, posted(=available) or claimed.
                final String currentTime = getCurrentDateTime();
                final String endTime = timeSlots.get(i).getStop_date();
                Log.e(TAG, "isParkingAvailable: currentTime: " + currentTime + "\n End Time: " + endTime);
                final boolean isSpotAvailable = compareTimes(currentTime, endTime);
                int available = isSpotAvailable ? 0 : 1;

                final ParkingSpot currentSpot = mParkingSpots.get(i);
                filteredParkingSpots.add(new ParkingSpot
                        (
                                currentSpot.getId(),
                                currentSpot.getAddress(),
                                currentSpot.getParkingNumber(),
                                available,
                                currentSpot.getClaimed()

                        ));
                Log.e(TAG, "SortSpots: getAvailabi() == 1!");

            } else {
                final ParkingSpot currentSpot = mParkingSpots.get(i);
                filteredParkingSpots.add(new ParkingSpot
                        (
                                currentSpot.getId(),
                                currentSpot.getAddress(),
                                currentSpot.getParkingNumber(),
                                0,
                                0));
                Log.e(TAG, "SortSpots: getClaimed() == 0 & availability=0!");
            }

            Log.e(TAG, "isParkingAvailable: " + filteredParkingSpots.toString());
        }
        //end
        return filteredParkingSpots;
    }


    private boolean compareTimes(String currentTime, String endTime) {
        boolean avail = false;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        try {
            Date now = formatter.parse(currentTime);
            Date end = formatter.parse(endTime);

            if (now != null && end != null) {
                if (now.after(end)) {
                    System.out.println("-->   Time has passed");
                    //means avail
                    avail = true;
                } else {
                    System.out.println("-->   Time will come");
                    //means not avail
                    avail = false;
                }
            }//not null
        } catch (ParseException ex) {
            Log.e(TAG, "compareTimes: ", ex);
        }

        return avail;
    }//end compare

    private String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    void set_parking_available(String parkingspot_id) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //MyApi api = retrofit.create(MyApi.class);
        Call<ParkingSpot> call = retrofit.create(MyApi.class).availableSpot(
                Constants.bearerToken,
                parkingspot_id,
                0
        );

        call.enqueue(new Callback<ParkingSpot>() {
            @Override
            public void onResponse(@NotNull Call<ParkingSpot> call, @NotNull retrofit2.Response<ParkingSpot> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.println(Log.DEBUG, TAG, response.toString() + "available from 0 to 1 !");
                }
            }

            @Override
            public void onFailure(Call<ParkingSpot> call, Throwable t) {
                Toast.makeText(ShowParkingActivity.this, Constants.error_availability, Toast.LENGTH_SHORT).show();
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void unclaim_spot(String id, List<ParkingSpot> filteredList) {
        SharedPreferences preferences_token = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String token = preferences_token.getString("token", null);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MyApi api = retrofit.create(MyApi.class);
        Call<ParkingSpot> call = api.claimSpot(
                Constants.bearerToken,
                id,
                0
        );
        call.enqueue(new Callback<ParkingSpot>() {
            @Override
            public void onResponse(Call<ParkingSpot> call, retrofit2.Response<ParkingSpot> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e(TAG, "onResponse: The Expired ParkingSpot was successfully unclaimed");
                }
            }

            @Override
            public void onFailure(Call<ParkingSpot> call, Throwable t) {
                Toast.makeText(ShowParkingActivity.this, Constants.error_availability, Toast.LENGTH_SHORT).show();
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }


    public void delete_availability(String id) {
        Log.d(TAG, " delete_parking availability in process");
        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<Availability> call = retrofit.create(MyApi.class).deleteAvailability(
                Constants.bearerToken,
                id
        );

        call.enqueue(new Callback<Availability>() {

            @Override
            public void onResponse(Call<Availability> call, retrofit2.Response<Availability> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Availability successfully deleted ---> " + response.body().toString());
                    //mListener.refreshActivity();
                }
            }

            @Override
            public void onFailure(Call<Availability> call, Throwable t) {
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
                Log.d(TAG, Constants.fail_general);
            }
        });
        Log.d(TAG, " delete_parking function executed");
    }
}
