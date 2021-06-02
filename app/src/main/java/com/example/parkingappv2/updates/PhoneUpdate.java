package com.example.parkingappv2.updates;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingappv2.activities.ShowUserActivity;
import com.example.parkingappv2.Constants;
import com.example.parkingappv2.MyApi;
import com.example.parkingappv2.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhoneUpdate extends ShowUserActivity {

    private static final String TAG = "PhoneUpdate";
    protected Button save_button;
    protected TextView old;
    protected TextView updated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_update);

        save_button=findViewById(R.id.phone_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_phone();
            }
        });
    }

    private void update_phone() {

        SharedPreferences preferences_token = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String token=preferences_token.getString("token", null);

        updated=findViewById(R.id.editTextPhone);
        String phone=updated.getText().toString();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        MyApi api = retrofit.create(MyApi.class);
        Call<JsonObject> call = api.phoneUpdate(
                Constants.bearerToken,
                phone
                );
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.println(Log.DEBUG, TAG, response.body().toString() + "TEST");
                    //Log.d(TAG, response.body().getEmail().toString());
                    Toast.makeText(PhoneUpdate.this, Constants.success_phone_update, Toast.LENGTH_SHORT).show();
                    updated.setText("");

                    new CountDownTimer(1000, 1000) {
                        public void onFinish() {
                            // When timer is finished
                            Intent intent=new Intent(getApplicationContext(), ShowUserActivity.class);
                            finish();
                            startActivity(intent);
                        }

                        public void onTick(long millisUntilFinished) {
                            // millisUntilFinished    The amount of time until finished.
                        }
                    }.start();

                }
            }
            @Override
            public void onFailure(Call<JsonObject>call, Throwable t) {
                Toast.makeText(PhoneUpdate.this, Constants.error_phone_update, Toast.LENGTH_SHORT).show();
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
                }
        });
    }
}