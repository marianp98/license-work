package com.example.parkingappv2.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.appolica.interactiveinfowindow.InfoWindow;
import com.example.parkingappv2.Constants;
import com.example.parkingappv2.MyApi;
import com.example.parkingappv2.R;
import com.example.parkingappv2.adapters.CustomInfoWindowAdapter;
import com.example.parkingappv2.adapters.CustomInfoWindowAdapter_claimed;
import com.example.parkingappv2.adapters.OnInfoWindowElemTouchListener;
import com.example.parkingappv2.models.Availability;
import com.example.parkingappv2.models.ParkingSpot;
import com.example.parkingappv2.updates.PhoneUpdate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String TAG = "MapsActivity";
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker LocationMarker;
    String[] longitudes;
    String[] latitudes;
    public String db_longitude;
    public String db_latitude;
    public String db_user_id;
    public String db_address;
    public String db_parkingNumber;
    public String db_name;
    public String db_availability;
    public String db_parkingspotID;
    public String db_claimed;
    public String db_phone;
    public String db_startTime;
    public String db_stopTime;
    private OnInfoWindowElemTouchListener infoButtonListener;
    private View.OnClickListener listener1;
    private DialogInterface.OnClickListener listener2;
    protected boolean ver;

    protected TextView user_id_tv;
    protected Button claim_button;

    protected View info_view;
    public String url;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private View v;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusCheck();
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map2);

        mapFragment.getMapAsync(this);
        v = getLayoutInflater().inflate(R.layout.marker_window_layout, null);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        getData();
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
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
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
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

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

        //first_function(markerOptions, latLng);
        first_function(markerOptions, latLng);
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
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


    public void add_marker(String user_id, String availabilityy, String parkingspotID, String latitude1, String longitude1, String address, String parkingNumber,
                           String name, String phone, String startTime, String stopTime, String claimed) {
        double latitudine = Double.parseDouble(latitude1.toString());
        double longitudine = Double.parseDouble(longitude1.toString());

        Availability availability = new Availability(user_id, availabilityy, parkingspotID, startTime, stopTime, claimed);
        final String currentTime = getCurrentDateTime();
        Log.d(TAG, "isParkingAvailable: currentTime: " + currentTime + "\n End Time: " + db_stopTime);
        final boolean isSpotAvailable = compareTimes(currentTime, db_stopTime);
        // Add a marker  and move the camera
        LatLng marker = new LatLng(latitudine, longitudine);

        MarkerOptions markerOptions = new MarkerOptions();
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

        Marker mark = null;
        if (user_id.equals(Constants.user_id)) {
            mark = mMap.addMarker(markerOptions.position(marker).title("Parking Owner: You")
                    .snippet("Address: " + address +
                            '\n' + "Parking Number: " + db_parkingNumber +
                            "\n" + "Phone Number: " + db_phone +
                            "\n" + "Available until " + db_stopTime +
                            "\n" + "                 UNAVAILABLE")

            );
            //mark.setTag(db_user_id);
            mark.setTag(availability);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));//here you can change the color
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
        } else {
            if (Integer.parseInt(claimed) == 1) {
              //**************************************************************************************  boolean isSpotClaimed = compareTimes(currentTime, stopTime);

                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter_claimed(MapsActivity.this));

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mark = mMap.addMarker(markerOptions.position(marker).title("Parking Owner: " + db_name)
                        .snippet("Address: " + address +
                                '\n' + "Parking Number: " + db_parkingNumber +
                                "\n" + "Phone Number: " + db_phone +
                                "\n" + "Available until " + db_stopTime +
                                "\n" + "                 CLAIMED")
                );

                // mark.setTag(db_user_id);
                mark.setTag(availability);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
            } else {
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mark = mMap.addMarker(markerOptions.position(marker).title("Parking Owner: " + db_name)
                        .snippet("Address: " + address +
                                '\n' + "Parking Number: " + db_parkingNumber +
                                "\n" + "Phone Number: " + db_phone +
                                "\n" + "Available until " + db_stopTime +
                                "\n" + "                 AVAILABLE")
                );

                mark.setTag(availability);

                // mark.setTag(db_user_id);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
            }


        }


        //after adding a marker we will make the button un-click able
    }//end add marker

    private void openDialog(Marker marker, MarkerOptions markerOptions) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MapsActivity.this);
        builder1.setTitle("You are about to mark this spot as claimed");
        builder1.setMessage("You sure?");
        builder1.setCancelable(true);

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        marker.setIcon(getMarkerIcon(getResources().getColor(R.color.red)));
                        Boolean claimed = true;
                        claim_spot(db_parkingspotID);

                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(MapsActivity.this, "Spot Claimed UNsuccessfully!", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void getData() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
        // in this case the data we are getting is in the form
        // of array so we are making a json array request.
        // below is the line where we are making an json array
        // request and then extracting data from each json object.
        String url = Constants.BaseUrl + "markers";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    // creating a new json object and
                    // getting each object from our json array.
                    try {
                        // we are getting each json object.
                        JSONObject responseObj = response.getJSONObject(i);

                        // now we get our response from API in json object format.
                        // in below line we are extracting a string with
                        // its key value from our json object.
                        // similarly we are extracting all the strings from our json object.
                        db_user_id = responseObj.getString("id_user");
                        db_latitude = responseObj.getString("latitude");
                        db_longitude = responseObj.getString("longitude");
                        db_address = responseObj.getString("address");
                        db_parkingNumber = responseObj.getString("parkingNumber");
                        db_name = responseObj.getString("name");
                        db_phone = responseObj.getString("phone");
                        db_startTime = responseObj.getString("start_date");
                        db_stopTime = responseObj.getString("stop_date");
                        db_availability = responseObj.getString("id");
                        db_parkingspotID = responseObj.getString("parkingspotID");
                        db_claimed = responseObj.getString("claimed");

                        add_marker(db_user_id, db_availability, db_parkingspotID, db_latitude, db_longitude, db_address, db_parkingNumber, db_name, db_phone, db_startTime, db_stopTime, db_claimed);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    public void delete_availability(Marker marker, MarkerOptions markerOptions) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<JsonObject> call = retrofit.create(MyApi.class).deleteAvailability
                (
                        Constants.bearerToken,
                        Objects.requireNonNull(marker.getTag()).toString()

                );
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MapsActivity.this, "Spot Claimed successfully!", Toast.LENGTH_SHORT).show();

                    //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                    marker.remove();
                    Calendar rightNow = Calendar.getInstance();
                    int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
                    Toast.makeText(MapsActivity.this, currentHourIn24Format, Toast.LENGTH_SHORT).show();
                    // return true;


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    public BitmapDescriptor getMarkerIcon(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
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


    private void first_function(MarkerOptions markerOptions, LatLng latLng) {

        mCurrLocationMarker = mMap.addMarker(markerOptions.visible(false));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d(TAG, "InfoWindow was clicked!");
                Log.d(TAG, "\n ID of the parking owner = " + marker.getTag() + "\n ID of the logged user= " + Constants.user_id);
                Log.d(TAG, "onInfoWindowClick: " + ver);
                ver = false;
                if (marker.getTag() != null) {
                    ver = false;
                    Availability availability = (Availability) marker.getTag();

                    int owner = Integer.parseInt(availability.getUser_id());
                    int user = Integer.parseInt((String) Constants.user_id);
                    if (owner - user == 0)
                        ver = true;

//                    if(ver == false)
//                    {
//                        openDialog(marker, markerOptions);
//                    }

                    if (ver == false) {
                        if (availability.getClaimed().equals("0"))
                            openDialog(marker, markerOptions);
                        else {
                            Toast.makeText(getApplicationContext(), "This parking is already claimed", Toast.LENGTH_LONG).show();
                        }
                    }


                    if (ver == true) {
                        Toast.makeText(getApplicationContext(), "You can't claim your own parking", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

    }


    private void second_function(MarkerOptions markerOptions, LatLng latLng) {

        mCurrLocationMarker = mMap.addMarker(markerOptions.visible(false));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
        String text = (String) mCurrLocationMarker.getTag();
        Log.d(TAG, "onLocationChanged: " + text);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d(TAG, "InfoWindow was clicked!");
                Log.d(TAG, "\n ID of the parking owner = " + marker.getTag() + "\n ID of the logged user= " + Constants.user_id);
                Log.d(TAG, "onInfoWindowClick: " + ver);
                ver = false;
                if (marker.getTag() != null) {
                    ver = false;
                    int owner = Integer.parseInt((String) marker.getTag());
                    int user = Integer.parseInt((String) Constants.user_id);
                    if (owner - user == 0)
                        ver = true;

                    if (ver == false) {
                        openDialog(marker, markerOptions);
                    }


                    if (ver == true) {
                        Toast.makeText(getApplicationContext(), "You can't claim your own parking", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

    }

    private void claim_spot(String id) {

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
                "1"
        );
        call.enqueue(new Callback<ParkingSpot>() {
            @Override
            public void onResponse(Call<ParkingSpot> call, retrofit2.Response<ParkingSpot> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e(TAG, "onResponse: ");
                }
            }

            @Override
            public void onFailure(Call<ParkingSpot> call, Throwable t) {
                Toast.makeText(MapsActivity.this, Constants.error_availability, Toast.LENGTH_SHORT).show();
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }


    private void unclaim_spot(String id) {

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
                "0"
        );
        call.enqueue(new Callback<ParkingSpot>() {
            @Override
            public void onResponse(Call<ParkingSpot> call, retrofit2.Response<ParkingSpot> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e(TAG, "onResponse: ");
                }
            }

            @Override
            public void onFailure(Call<ParkingSpot> call, Throwable t) {
                Toast.makeText(MapsActivity.this, Constants.error_availability, Toast.LENGTH_SHORT).show();
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

}
