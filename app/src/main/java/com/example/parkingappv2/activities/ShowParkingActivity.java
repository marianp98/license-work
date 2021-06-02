package com.example.parkingappv2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.parkingappv2.Constants;
import com.example.parkingappv2.MyApi;
import com.example.parkingappv2.adapters.ParkingSpotAdapter;
import com.example.parkingappv2.R;
import com.example.parkingappv2.models.Availability;
import com.example.parkingappv2.models.ParkingSpot;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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

public class ShowParkingActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = "ShowParkingActivity";
    public FloatingActionButton add_button;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
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

    public void onLocationChanged(Location location) {
        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);


        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }
    }

    private void show_parking() {
        SharedPreferences preferences_token = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String token=preferences_token.getString("token", null);
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
                    Log.println(Log.DEBUG, TAG, response.body().toString() + "TEST");


                    mParkingSpots = new ArrayList<>();
                    mParkingSpots.clear();
                    mParkingSpots.addAll(response.body());
                    Log.e(TAG, "onResponse: got parking slots of total: " + response.body().size());
//                    Log.e(TAG, "onResponse v1: "+response.body().get(0).isClaimed());

                    //before building the recycler view, you have to check
                    //if the Parking is available or not
                    //so that we can update the "Make Available" button

                    //for this we have to get Availability Dates from server
                    //so we will hit a new API
                    getAvailabilitySlots1();
                }
            }//res

            @Override
            public void onFailure(@NotNull Call<List<ParkingSpot>> call, @NotNull Throwable t) {
                Toast.makeText(ShowParkingActivity.this, Constants.fail_showparking, Toast.LENGTH_SHORT).show();
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }//end show parking


    private void buildParkingList(Response<JsonArray> response, ArrayList<ParkingSpot> parkingList, int array_size) {
        for (int i = 0; i < array_size; i++) {
//            String town=response.body().get(i).getAsJsonObject().get("town").getAsString();
            String final_address = response.body().get(i).getAsJsonObject().get("address").getAsString();
            String final_parking_Number = response.body().get(i).getAsJsonObject().get("parkingNumber").getAsString();
            String id = response.body().get(i).getAsJsonObject().get("id").getAsString();
//            String latitude=response.body().get(i).getAsJsonObject().get("latitude").getAsString();
            //           String longitude=response.body().get(i).getAsJsonObject().get("longitude").getAsString();
//            String user_id=response.body().get(i).getAsJsonObject().get("user_id").getAsString();

//            parkingList.add(new ParkingSpot(R.drawable.ic_android, final_address, "Parking Number: "+final_parking_Number));

            //parkingList.add(new ParkingSpot(id, final_address, "Parking Number: " + final_parking_Number));

        }

    }

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
                Toast.makeText(ShowParkingActivity.this, "TEST", Toast.LENGTH_SHORT).show();
                //  parkingList.get(position).changeText1("test");
                mAdapter.notifyItemChanged(position);//
            }

            @Override
            public void finish() {
                ShowParkingActivity.this.finish();
            }

            @Override
            public void refreshActivity() {
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

    private void getAvailabilitySlots1() {
        SharedPreferences preferences_token = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String token=preferences_token.getString("token", null);

        SharedPreferences preferences_user_id = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String user_id=preferences_user_id.getString("user_id", null);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Call<List<Availability>> call = getRetrofit(gson).getAvailableSlots1(
                Constants.bearerToken,
                user_id
        );
        //execute
        call.enqueue(new Callback<List<Availability>>() {
            @Override
            public void onResponse(@NotNull Call<List<Availability>> call, @NotNull Response<List<Availability>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e(TAG, "onResponse: got list of size: " + response.body().size());

                    //pass the time slots to check the availability
                    final List<ParkingSpot> parkingSpots = isParkingAvailable(response.body());
                   // Log.d(TAG, "onResponse v2: "+response.body().get(0).getClaimed());

                    //update UI (setup recycler view)
                    buildRecyclerView(parkingSpots);
                }
            }//response

            @Override
            public void onFailure(@NotNull Call<List<Availability>> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure:", t);
                Toast.makeText(ShowParkingActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }//fail
        });
    }//end get

    private List<ParkingSpot> isParkingAvailable(List<Availability> timeSlots) {
        Log.e(TAG, "isParkingAvailable: validating parking slots...");

        //1- list of avail time slots on server & the parking spots should be of same size
        final int availableSlots = timeSlots.size();
        final int availableParkingSpots = mParkingSpots.size();
        List<ParkingSpot> filteredParkingSpots = new ArrayList<>();

//        if (availableSlots == availableParkingSpots) {

        for (int i = 0; i < availableParkingSpots; i++) {

            if (i < availableSlots) {
                //make sure we are talking about the same parking spot
                if (mParkingSpots.get(i).getId().equals(timeSlots.get(i).getParkingspot_id())) {
                    //now we check if the stop date has reached or not

                    final String currentTime = getCurrentDateTime();
                    final String endTime = timeSlots.get(i).getStop_date();
                    Log.e(TAG, "isParkingAvailable: currentTime: " + currentTime + "\n End Time: " + endTime);
                    final boolean isSpotAvailable = compareTimes(currentTime, endTime);

                    //finally we add a Parking Spot marked as avail or un-avail
                    final ParkingSpot currentSpot = mParkingSpots.get(i);
                    filteredParkingSpots.add(new ParkingSpot
                            (
                                    currentSpot.getId(),
                                    currentSpot.getAddress(),
                                    currentSpot.getParkingNumber(),
                                    isSpotAvailable,
                                    currentSpot.getClaimed()

                            ));
                    if(currentSpot.getClaimed()==1)
                        Log.e(TAG, "isParkingAvailable1: "+"ISCLAIMED = 1");
                    if(currentSpot.getClaimed() ==0)
                        Log.e(TAG, "isParkingAvailable1: "+"ISCLAIMED = 0");
                    Log.e(TAG, "isParkingAvailable1: "+"TESTTTTTTTTT = 0");


                }//end if
            }//if time slots are in range ->>
            else {
                final ParkingSpot currentSpot = mParkingSpots.get(i);
                filteredParkingSpots.add(new ParkingSpot
                        (
                                currentSpot.getId(),
                                currentSpot.getAddress(),
                                currentSpot.getParkingNumber(),
                                true,
                                currentSpot.getClaimed()
                        ));
                if(currentSpot.getClaimed() ==1)
                    Log.e(TAG, "isParkingAvailable2: "+"ISCLAIMED = 1");
            }

        }
        Log.e(TAG, "isParkingAvailable3: "+filteredParkingSpots.toString());
        return filteredParkingSpots;
    }//end

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

    //______________________________________________________________________________________________

}//end class