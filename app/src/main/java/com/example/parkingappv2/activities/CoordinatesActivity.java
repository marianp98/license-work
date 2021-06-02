package com.example.parkingappv2.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.parkingappv2.Constants;
import com.example.parkingappv2.MyApi;
import com.example.parkingappv2.MyResponse;
import com.example.parkingappv2.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoordinatesActivity extends Activity {
    protected LocationManager locationManager;
    protected Context context;// this is a memory leak!
    private double lat;
    private double lng;
    private int parkingNumber;
    private Button save_button;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private TextView textview_address;
    private TextView textview_city;
    private TextView textview_parkingNumber;
    private static final String TAG = "CoordinatesActivity";

    public CoordinatesActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinates);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusCheck();
            checkLocationPermission();
        }
        textview_address = findViewById(R.id.coordinates_address);
        textview_city = findViewById(R.id.coordinates_city);
        textview_parkingNumber = findViewById(R.id.coordinates_parkingNumber);
        save_button = findViewById(R.id.save_button);

        textview_address.addTextChangedListener(loginTextWatcher);
        textview_city.addTextChangedListener(loginTextWatcher);
        textview_city.addTextChangedListener(loginTextWatcher);
        textview_parkingNumber.addTextChangedListener(loginTextWatcher);


        popupMessage();

        //init location provider client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        //get last known location (current location if available)
        getCurrentLocation();

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveParkInfo();
            }
        });
    }
    public void popupMessage(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(Constants.message1+"\n"+Constants.message2);
       // alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
        alertDialogBuilder.setTitle("Attention!");
        alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG,"Acknowledge Displayed");
                // add these two lines, if you wish to close the app:
                // finishAffinity();
               // System.exit(0);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        CoordinatesActivity.this.finish();
                        System.exit(0);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        //here is location, we can now get lat & lng
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        Log.e("LOCATION", "lat: " + lat + "lng: " + lng);

                        updateUIWithLocationInfo(); //if location is found only then we will geocode it
                    } else {
                        Log.e("LOCATION", "getCurrentLocation  is empty!! ");
                    }
                }).addOnFailureListener(this, e -> Log.e("LOCATION", "onFailure: " + e.getMessage()));
    }

    private void updateUIWithLocationInfo() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();

            textview_address = (TextView) findViewById(R.id.coordinates_address);
            textview_address.setText(address);

            TextView textview_city = (TextView) findViewById(R.id.coordinates_city);
            textview_city.setText(city);

            TextView textview_lat = (TextView) findViewById(R.id.lat);
            textview_lat.setText(String.valueOf(lat));

            TextView textview_lng = (TextView) findViewById(R.id.lng);
            textview_lng.setText(String.valueOf(lng));

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("EROR", "onLocationChanged: " + e.getMessage());
        }
    }


    private void saveParkInfo() {


        SharedPreferences preferences_token = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String token=preferences_token.getString("token", null);

        String city = textview_city.getText().toString();
        int parkingNumber = Integer.parseInt(textview_parkingNumber.getText().toString());
        String address = textview_address.getText().toString();



        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Call<MyResponse> call = retrofit.create(MyApi.class).postParkLocation
                (
                        Constants.bearerToken,
                        city,
                        address,
                        parkingNumber,
                        lat,
                        lng,
                        0
                );
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CoordinatesActivity.this, response.body().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    new CountDownTimer(1000, 1000) {
                        public void onFinish() {
                            // When timer is finished

                           // CoordinatesActivity.super.onBackPressed();

                            Intent i = new Intent(getApplicationContext(), ShowParkingActivity.class);
                            finish();
                            startActivity(i);
                        }

                        public void onTick(long millisUntilFinished) {
                            // millisUntilFinished    The amount of time until finished.
                        }
                    }.start();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });

    }


    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String addressInput = textview_address.getText().toString().trim();
            String cityInput = textview_city.getText().toString().trim();
            String parkingNumberInput = textview_parkingNumber.getText().toString().trim();

            save_button.setEnabled(!addressInput.isEmpty() && !cityInput.isEmpty() && !parkingNumberInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}