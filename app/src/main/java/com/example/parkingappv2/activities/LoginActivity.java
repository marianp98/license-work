package com.example.parkingappv2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkingappv2.Constants;
import com.example.parkingappv2.MyApi;
import com.example.parkingappv2.R;
import com.example.parkingappv2.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String TAG = "MainActivity";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker LocationMarker;
    String[] longitudes;
    String[] latitudes;
    public String db_longitude;
    public String db_latitude;
    public String db_address;
    public String url;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private EditText password;
    private EditText email;
    private Button login_button;
    private CheckBox remember;
    public static final String SHARED_PREFS="sharedPrefs";
    public static final String TEXT="text";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusCheck();
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.first_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        email = findViewById(R.id.loginpage_email);
        password = findViewById(R.id.loginpage_password);
        login_button=findViewById(R.id.loginpage_loginbutton);
        email.addTextChangedListener(loginTextWatcher);
        password.addTextChangedListener(loginTextWatcher);
        remember=findViewById(R.id.checkBox_login);



        Button login_button = findViewById(R.id.loginpage_loginbutton);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginAction();
            }
        });
        Button register_button = findViewById(R.id.loginpage_signupbutton);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RegisterAction();
            }
        });
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked())
                {
                    SharedPreferences preferences=getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                }
                else if (!buttonView.isChecked())
                {
                    SharedPreferences preferences=getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                }
            }
        });
    }
    public void saveData(String name, String token, String user_id)
    {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("token", token);
        editor.putString("user_id", user_id);

        Log.e(TAG, "saveData: name: "+name );
        Log.e(TAG, "saveData: token: "+token );
        Log.e(TAG, "saveData: user_id: "+user_id );

        editor.apply();
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
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void RegisterAction()
    {
        Intent register_page_intent=new Intent(this, RegisterActivity.class);
        startActivity(register_page_intent);
    }

    private void LoginAction() {
        //loadingProgressBar.setVisibility(View.VISIBLE);
        //loginViewModel.login(usernameEditText.getText().toString(),
        //  passwordEditText.getText().toString());

        String introduced_email = email.getText().toString();
        String introduced_password = password.getText().toString();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MyApi api = retrofit.create(MyApi.class);
        Call<User> call = api.createUser(
                introduced_email,
                introduced_password
               // Constants.bearerToken
        );
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> userResponse) {
                if (userResponse.isSuccessful() && userResponse.body() != null) {
                    Toast.makeText(LoginActivity.this, Constants.success_login, Toast.LENGTH_SHORT).show();
                    Log.println(Log.DEBUG, TAG, userResponse.body().getToken() + "TEST");
                    Constants.bearerToken="Bearer "+ userResponse.body().getToken();
                    Constants.user_id=userResponse.body().getId();
                    Log.e(TAG, "onResponse: "+ userResponse.body().getName().toString());
                    saveData(userResponse.body().getName(),userResponse.body().getToken(), userResponse.body().getId());
                    login_success();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, Constants.error_login, Toast.LENGTH_SHORT).show();
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });

    }

    private void login_success() {
        Intent register_page_intent=new Intent(this, LoggedUserActivity.class);
        finish();
        startActivity(register_page_intent);
    }


    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameInput = email.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();
            boolean email_empty=usernameInput.isEmpty();
            boolean password_empty=passwordInput.isEmpty();
            if(!usernameInput.isEmpty() && !passwordInput.isEmpty() )
            {
                login_button.setEnabled(true);
                login_button.setBackgroundResource(R.drawable.signup_button_shape);

            }
            else
            {
                login_button.setEnabled(false);
                login_button.setBackgroundResource(R.drawable.login_button_shape);

            }



        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // getData();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
//Showing Current Location Marker on Map
        LatLng latLng1 = new LatLng(getIntent().getDoubleExtra("latitudes", 0.0), getIntent().getDoubleExtra("longitudes", 0.0));
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(latLng1);

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        markerOptions.draggable(true);
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
                    markerOptions.title("" + latLng + "," + subLocality + "," + state
                            + "," + country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        mCurrLocationMarker = mMap.addMarker(markerOptions1);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));


        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,7));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }
    public boolean checkLocationPermission() {
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
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}