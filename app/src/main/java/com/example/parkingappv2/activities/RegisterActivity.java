package com.example.parkingappv2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkingappv2.Constants;
import com.example.parkingappv2.R;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private TextView register_name;
    private TextView register_email;
    private TextView register_password;
    private TextView register_username;
    private TextView register_phone_number;
    private Button register_button;

    //int success=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_name=findViewById(R.id.registerpage_name);
        register_email=findViewById(R.id.registerpage_email);
        register_password=findViewById(R.id.registerpage_password);
        register_username=findViewById(R.id.registerpage_username);
        register_phone_number=findViewById(R.id.registerpage_phone_number);
        register_button=findViewById(R.id.registerpage_registerbutton);

        register_name.addTextChangedListener(loginTextWatcher);
        register_email.addTextChangedListener(loginTextWatcher);
        register_password.addTextChangedListener(loginTextWatcher);
        register_username.addTextChangedListener(loginTextWatcher);
        register_phone_number.addTextChangedListener(loginTextWatcher);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinalRegisterAction();
            }
        });

    }

    public void FinalRegisterAction() {
        Context context = getApplicationContext();
        TextView register_name = findViewById(R.id.registerpage_name);
        TextView register_email = findViewById(R.id.registerpage_email);
        TextView register_password = findViewById(R.id.registerpage_password);
        TextView register_username = findViewById(R.id.registerpage_username);
        TextView register_phone_number = findViewById(R.id.registerpage_phone_number);

        String name = register_name.getText().toString();
        String email = register_email.getText().toString();
        String password = register_password.getText().toString();
        String username = register_username.getText().toString();
        String phone = register_phone_number.getText().toString();


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String URL = Constants.BaseUrl+ "register?name=" + name +"&email=" + email + "&password=" + password+
                "&username=" + username + "&phone=" + phone;

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //success=1;
                        Log.e("Rest Response", response.toString());
                        // TODO : initiate successful registered in experience
                        Toast.makeText(getApplicationContext(), "You have successfully registered.", Toast.LENGTH_LONG).show();
                        Intent login_after_register_intent = new Intent(context, LoginActivity.class);
                        startActivity(login_after_register_intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                        Toast.makeText(getApplicationContext(), "Something is wrong, try again", Toast.LENGTH_LONG).show();


                    }
                }

        );
//        if(success == 1)
//        {
//            // TODO : initiate successful registered in experience
//            Toast.makeText(getApplicationContext(), "You have successfully registered.", Toast.LENGTH_LONG).show();
//            Intent login_after_register_intent = new Intent(context, MainActivity.class);
//            startActivity(login_after_register_intent);
//        }
//        else
//        {
//            Toast.makeText(getApplicationContext(), "Something is wrong, try again", Toast.LENGTH_LONG).show();
//
//        }

        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(objectRequest);
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nameInput = register_name.getText().toString().trim();
            String usernameInput = register_username.getText().toString().trim();
            String passwordInput = register_password.getText().toString().trim();
            String emailInput = register_email.getText().toString().trim();
            String phoneInput = register_phone_number.getText().toString().trim();

            register_button.setEnabled(!nameInput.isEmpty() && !usernameInput.isEmpty() && !passwordInput.isEmpty()
                    && !emailInput.isEmpty() && !phoneInput.isEmpty()
            );
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}