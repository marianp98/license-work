package com.example.parkingappv2.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingappv2.Constants;
import com.example.parkingappv2.MyApi;
import com.example.parkingappv2.R;
import com.example.parkingappv2.updates.EmailUpdate;
import com.example.parkingappv2.updates.PhoneUpdate;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowUserActivity extends AppCompatActivity {

    private TextView name_tv;
    private TextView username_tv;
    private TextView email_tv;
    private TextView phone_tv;
    private Button delete_button;
    private Button phone_button;
    private Button email_button;
    private static final String TAG = "ShowUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        show_user();

        delete_button = findViewById(R.id.delete_user_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_popup();
            }
        });
        phone_button = findViewById(R.id.change_phone_button);
        phone_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update_phone = new Intent(getApplicationContext(), PhoneUpdate.class);
                finish();
                startActivity(update_phone);
            }
        });

        email_button = findViewById(R.id.change_email_button);
        email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update_email = new Intent(getApplicationContext(), EmailUpdate.class);
                finish();
                startActivity(update_email);
            }
        });
    }

    private void alert_popup() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this account?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        delete_user();
                        Toast.makeText(getApplicationContext(), "Account successfully deleted", Toast.LENGTH_SHORT).show();
                        finishAffinity();
                        startActivity(i);
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

    private void delete_user() {

        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<JsonObject> call = retrofit.create(MyApi.class).destroyuser(
                Constants.bearerToken
        );
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ShowUserActivity.this, "Account successfully deleted!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(ShowUserActivity.this, Constants.fail_general, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void show_user() {
        name_tv = (TextView) findViewById(R.id.user_name);
        username_tv = (TextView) findViewById(R.id.user_username);
        email_tv = (TextView) findViewById(R.id.user_email);
        phone_tv = (TextView) findViewById(R.id.user_phone);

        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<JsonObject> call = retrofit.create(MyApi.class).getUsers(
                Constants.bearerToken
        );
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject user_info = response.body();
                    name_tv.setText(user_info.get("name").getAsString());
                    username_tv.setText(user_info.get("username").getAsString());
                    email_tv.setText(user_info.get("email").getAsString());
                    phone_tv.setText(user_info.get("phone").getAsString());
                    Log.d(TAG, user_info.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}