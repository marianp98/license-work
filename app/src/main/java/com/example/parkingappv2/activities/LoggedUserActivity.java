package com.example.parkingappv2.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.parkingappv2.Constants;
import com.example.parkingappv2.MyApi;
import com.example.parkingappv2.MyResponse;
import com.example.parkingappv2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoggedUserActivity extends AppCompatActivity {
    private static final String TAG = "LoggedUserActivity";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_user);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusCheck();
            checkLocationPermission();
        }
        Button show_parkings=findViewById(R.id.show_parkings_button);
        show_parkings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowParkingsAction();
            }
        });
        Button show_user=findViewById(R.id.view_info_button);
        show_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowUserAction();
            }
        });

        Button mark_button=findViewById(R.id.mark_button);
        mark_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationAction();

            }
        });

//        Button  get_coordinates = findViewById(R.id.logged_user_coordinates_button);
//        get_coordinates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CoordinatesAction();
//            }
//        });

        Button  logout = findViewById(R.id.logged_user_logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutAction();
            }
        });

//        Button set_availability=findViewById(R.id.logged_user_share_parking_button);
//        set_availability.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SetAvailabilityAction();
//            }
//        });

    }

    private void ShowUserAction() {
        Intent show_user=new Intent(this, ShowUserActivity.class);
        startActivity(show_user);
    }

    private void ShowParkingsAction() {
        Intent show_parkings=new Intent(this, ShowParkingActivity.class);
        startActivity(show_parkings);
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
                        LoggedUserActivity.this.finish();
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

//    private void SetAvailabilityAction() {
//        Intent set_availability_intent=new Intent(this, SetAvailabilityActivity.class);
//        startActivity(set_availability_intent);
//    }

    private void LogoutAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoggedUserActivity.this);
        builder.setTitle("Logout Confirmation").
                setMessage("You sure that you want to logout?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(),
                                LoginActivity.class);
                      //  Log.d(TAG,"Token Revoked1");
                        Logout();
                        SharedPreferences preferences=getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("remember", "false");
                        editor.apply();
                        finishAffinity();
                        startActivity(i);
                       // Log.d(TAG,"Token Revoked2");
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    private void LocationAction()
    {
        Intent location_intent=new Intent(this, MapsActivity.class);
        startActivity(location_intent);
    }

//    private void CoordinatesAction()
//    {
//        Intent coordinates_intent=new Intent(this, CoordinatesActivity.class);
//        startActivity(coordinates_intent);
//    }

    private void Logout()
    {
        //Calling the Logout API in order to revoke the User's token

        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<MyResponse> call = retrofit.create(MyApi.class).Logout
                (
                        Constants.bearerToken
                );
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(LoggedUserActivity.this, response.body().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });

    }
}

/*
package com.example.parkingappv2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoggedUserActivity extends AppCompatActivity {
    private static final String TAG = "LoggedUserActivity";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_user);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusCheck();
            checkLocationPermission();
        }
        Button show_parkings=findViewById(R.id.show_parkings_button);
        show_parkings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowParkingsAction();
            }
        });
        Button show_user=findViewById(R.id.view_info_button);
        show_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowUserAction();
            }
        });

        Button mark_button=findViewById(R.id.mark_button);
        mark_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationAction();

            }
        });

        Button  get_coordinates = findViewById(R.id.logged_user_coordinates_button);
        get_coordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoordinatesAction();
            }
        });

        Button  logout = findViewById(R.id.logged_user_logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutAction();
            }
        });

        Button set_availability=findViewById(R.id.logged_user_share_parking_button);
        set_availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAvailabilityAction();
            }
        });

    }

    private void ShowUserAction() {
        Intent show_user=new Intent(this, ShowUserActivity.class);
        startActivity(show_user);
    }

    private void ShowParkingsAction() {
        Intent show_parkings=new Intent(this, ShowParkingActivity.class);
        startActivity(show_parkings);
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
                        LoggedUserActivity.this.finish();
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

    private void SetAvailabilityAction() {
        Intent set_availability_intent=new Intent(this, SetAvailabilityActivity.class);
        startActivity(set_availability_intent);
    }

    private void LogoutAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoggedUserActivity.this);
        builder.setTitle("Logout Confirmation").
                setMessage("You sure that you want to logout?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(),
                                LoginActivity.class);
                      //  Log.d(TAG,"Token Revoked1");
                        Logout();
                        SharedPreferences preferences=getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("remember", "false");
                        editor.apply();
                        finishAffinity();
                        startActivity(i);
                       // Log.d(TAG,"Token Revoked2");
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    private void LocationAction()
    {
        Intent location_intent=new Intent(this, MapsActivity.class);
        startActivity(location_intent);
    }

    private void CoordinatesAction()
    {
        Intent coordinates_intent=new Intent(this, CoordinatesActivity.class);
        startActivity(coordinates_intent);
    }

    private void Logout()
    {
        //Calling the Logout API in order to revoke the User's token

        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<MyResponse> call = retrofit.create(MyApi.class).Logout
                (
                        Constants.bearerToken
                );
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(LoggedUserActivity.this, response.body().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });

    }
}
 */