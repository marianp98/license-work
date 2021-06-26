package com.example.parkingappv2.splashscreens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingappv2.Constants;
import com.example.parkingappv2.MyApi;
import com.example.parkingappv2.R;
import com.example.parkingappv2.activities.LoggedUserActivity;
import com.example.parkingappv2.activities.LoginActivity;
import com.example.parkingappv2.activities.MapsActivity;
import com.example.parkingappv2.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.security.auth.login.LoginException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String Checkbox_Shared_Pref = "checkbox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = formatter.format(new Date(System.currentTimeMillis()));
        String endTime = "2021-05-17 03:40:00";
        try {
            Date d1 = formatter.parse(currentTime);
            Date d2 = formatter.parse(endTime);

            assert d1 != null;
            if (d1.after(d2)) {
                System.out.println("-->   Time has passed");
            } else {
                System.out.println("-->   Time will come");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        //the upper bar is hidden in this screen due to design reasons
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //countdown timer for the time the splash-screen is displayed
        new CountDownTimer(2000, 1000) {
            public void onFinish() {
                // When timer is finished
                checkbox();


            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }

        }.start();
    }


    public void checkbox() {
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");
        if (checkbox.equals("true")) {
            String email = preferences.getString("email", "");
            String pass = preferences.getString("pass", "");

            LoginActionRemembered(email, pass);
//            Intent intent = new Intent(this, SecondSplashAfterLogin.class);
//            startActivity(intent);
        } else if (checkbox.equals("false")) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finishAffinity();
//            Toast.makeText(this, "Please Sign in", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoginActionRemembered(String email, String pass) {


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
                email,
                pass
        );
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> userResponse) {
                if (userResponse.isSuccessful() && userResponse.body() != null) {
                    Toast.makeText(MainActivity.this, Constants.success_login, Toast.LENGTH_SHORT).show();
                    Log.println(Log.DEBUG, TAG, "Token = " + userResponse.body().getToken());
                    Constants.bearerToken = "Bearer " + userResponse.body().getToken();
                    Constants.user_id = userResponse.body().getId();
                    Log.e(TAG, "onResponse: " + userResponse.body().getName().toString());
                    //saveData(userResponse.body().getName(), userResponse.body().getToken(), userResponse.body().getId());
                    login_success();


                } else {
                    Toast toast = Toast.makeText(MainActivity.this, Constants.error_login, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }

        });

    }

    private void login_success() {
        Intent register_page_intent = new Intent(this, SecondSplashAfterLogin.class);
        finish();
        startActivity(register_page_intent);
    }

}
