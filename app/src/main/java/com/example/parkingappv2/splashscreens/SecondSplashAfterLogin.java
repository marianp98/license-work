package com.example.parkingappv2.splashscreens;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.parkingappv2.R;
import com.example.parkingappv2.activities.LoggedUserActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.tomer.fadingtextview.FadingTextView;

public class SecondSplashAfterLogin extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener {

    private FadingTextView fadingTextView;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_splash_after_login);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.invisible_map_splash);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        View fragmentMap = findViewById(R.id.invisible_map_splash);
        fragmentMap.setVisibility(View.GONE);

        fadingTextView = findViewById(R.id.fading_text_view);
        int width = getResources().getDisplayMetrics().widthPixels;
        int hei = getResources().getDisplayMetrics().heightPixels / 2;
        fadingTextView.setLayoutParams(new RelativeLayout.LayoutParams(width, hei));
        Typeface myfont = Typeface.createFromAsset(this.getAssets(), "fonts/SlateForOnePlus-Regular.ttf");
        fadingTextView.setTypeface(myfont);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String name = preferences.getString("name", null);
        if (name != null) {
            displayTextDelayed(name);
        }


        //countdown timer for the time the splash-screen is displayed
        new CountDownTimer(4000, 1000) {
            public void onFinish() {
                // When timer is finished

                Intent intent = new Intent(getApplicationContext(), LoggedUserActivity.class);
                //finishAffinity();
                startActivity(intent);
                finishAffinity();
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }

    public void displayTextDelayed(String name) {
        //countdown timer for the time the splash-screen is displayed
        new CountDownTimer(2000, 1000) {
            public void onFinish() {
                // When timer is finished
                displayWelcomeMessage(name);
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }

    public void displayWelcomeMessage(String name) {
        String welcomeMessage1 = "Welcome to the ParkingApp, " + name;
        fadingTextView.setTexts(new String[]{welcomeMessage1});
    }


    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

}