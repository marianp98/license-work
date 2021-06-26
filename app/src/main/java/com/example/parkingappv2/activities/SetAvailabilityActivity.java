package com.example.parkingappv2.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingappv2.Constants;
import com.example.parkingappv2.MyApi;
import com.example.parkingappv2.R;
import com.example.parkingappv2.models.Availability;
import com.example.parkingappv2.models.ParkingSpot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SetAvailabilityActivity extends AppCompatActivity {

    private static final String TAG = "SetAvailabilityActivity";
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    private TextView mDisplayTime;
    private TextView mDisplayTime_end;
    private Button save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_availability);

        //find views
        save_button = findViewById(R.id.available_marking_button);
        mDisplayTime = (TextView) findViewById(R.id.start_date);
        mDisplayTime_end = (TextView) findViewById(R.id.end_date);

        //listeners
        mDisplayTime.setOnClickListener(v -> datePicker());
        mDisplayTime_end.setOnClickListener(v -> datePicker_end());
        save_button.setOnClickListener(v -> {
            save_button.setEnabled(false);
            SaveAvailability();
        });

        mDisplayTime.addTextChangedListener(availabilityIntervalTextWatcher);
        mDisplayTime_end.addTextChangedListener(availabilityIntervalTextWatcher);

    }

    private void SaveAvailability() {

        TextView textview_startTime = (TextView) findViewById(R.id.start_date);
        String startTime = textview_startTime.getText().toString();

        TextView textview_endTime = (TextView) findViewById(R.id.end_date);
        String endTime = textview_endTime.getText().toString();

        Intent intent_made_for_id = getIntent();
        String id = intent_made_for_id.getStringExtra("id");
        update_parking_date(startTime, endTime);

        Log.d(TAG, startTime + " SI " + endTime);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //MyApi api = retrofit.create(MyApi.class);
        Call<Availability> call = retrofit.create(MyApi.class).createAvailability(
                startTime,
                endTime,
                id,
                Constants.bearerToken
        );

        call.enqueue(new Callback<Availability>() {
            @Override
            public void onResponse(@NotNull Call<Availability> call, @NotNull retrofit2.Response<Availability> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.println(Log.DEBUG, TAG, response.toString() + "TEST");

                    Toast.makeText(SetAvailabilityActivity.this, Constants.success_availability, Toast.LENGTH_SHORT).show();
                    set_parking_available();
                    update_parking_date(startTime, endTime);
                    textview_startTime.setText("");
                    textview_endTime.setText("");

                    //delay for a sec
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        finish();
                        startActivity(new Intent(SetAvailabilityActivity.this, ShowParkingActivity.class));
                    }, 1000);

                }//res
                else {
                    Toast.makeText(SetAvailabilityActivity.this, Constants.error_availability, Toast.LENGTH_SHORT).show();

                }
            }//res

            @Override
            public void onFailure(Call<Availability> call, Throwable t) {
                Toast.makeText(SetAvailabilityActivity.this, Constants.error_availability, Toast.LENGTH_SHORT).show();
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void datePicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        //date_time = correct_time_display(dayOfMonth) + "-" +correct_time_display( (monthOfYear + 1)) + "-" + year;
                        date_time = +year + "-" + correct_time_display((monthOfYear + 1)) + "-" + correct_time_display(dayOfMonth);

                        //*************Call Time Picker Here ********************
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        mDisplayTime.setText(date_time + " " + correct_time_display(hourOfDay) + ":" + correct_time_display(minute));
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void datePicker_end() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        //date_time = correct_time_display(dayOfMonth) + "-" +correct_time_display( (monthOfYear + 1)) + "-" + year;
                        date_time = +year + "-" + correct_time_display((monthOfYear + 1)) + "-" + correct_time_display(dayOfMonth);

                        //*************Call Time Picker Here ********************
                        timePicker_end();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    private void timePicker_end() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        mDisplayTime_end.setText(date_time + " " + correct_time_display(hourOfDay) + ":" + correct_time_display(minute));
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    public String correct_time_display(int minute) {
        String str = "";

        if (minute >= 10) {

            str = Integer.toString(minute);
        } else {
            str = "0" + Integer.toString(minute);

        }
        return str;
    }

    private TextWatcher availabilityIntervalTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String startDate = mDisplayTime.getText().toString().trim();
            String stopDate = mDisplayTime_end.getText().toString().trim();

            if (!startDate.isEmpty() && !stopDate.isEmpty()) {
                save_button.setEnabled(true);
                save_button.setBackgroundResource(R.drawable.signup_button_shape);

            } else {
                save_button.setEnabled(false);
                save_button.setBackgroundResource(R.drawable.login_button_shape);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    void set_parking_available() {
        String parkingspot_id = getIntent().getStringExtra("id");

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
                1
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
                Toast.makeText(SetAvailabilityActivity.this, Constants.error_availability, Toast.LENGTH_SHORT).show();
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }


    void update_parking_date(String startTime, String endTime) {
        String parkingspot_id = getIntent().getStringExtra("id");

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //MyApi api = retrofit.create(MyApi.class);
        Call<ParkingSpot> call = retrofit.create(MyApi.class).updateParkingDate(
                Constants.bearerToken,
                parkingspot_id,
                startTime,
                endTime
        );

        call.enqueue(new Callback<ParkingSpot>() {
            @Override
            public void onResponse(@NotNull Call<ParkingSpot> call, @NotNull retrofit2.Response<ParkingSpot> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.println(Log.DEBUG, TAG, response.toString() + "Availability Interval updated");
                }
            }

            @Override
            public void onFailure(Call<ParkingSpot> call, Throwable t) {
                Toast.makeText(SetAvailabilityActivity.this, Constants.error_availability, Toast.LENGTH_SHORT).show();
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}