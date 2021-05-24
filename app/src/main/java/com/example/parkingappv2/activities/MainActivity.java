package com.example.parkingappv2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.parkingappv2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

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

            if (d1.after(d2)) {
                System.out.println("-->   Time has passed");
            } else {
                System.out.println("-->   Time will come");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new CountDownTimer(2000, 1000) {
            public void onFinish() {
                // When timer is finished

                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                String checkbox = preferences.getString("remember", "");
                if (checkbox.equals("true")) {
                    Intent intent = new Intent(getApplicationContext(), LoggedUserActivity.class);
                    startActivity(intent);
                    finishAffinity();

                } else if (checkbox.equals("false")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    //finishAffinity();
                    startActivity(intent);
                    finishAffinity();

                }
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }

}
//if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
//        {
//        Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
//        finish();
//        startActivity(intent);
//
//        }
//        else
//        {
//        Intent intent=new Intent(getApplicationContext(), LoggedUserActivity.class);
//        finish();
//        startActivity(intent);
//        }