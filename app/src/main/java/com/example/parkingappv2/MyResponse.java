package com.example.parkingappv2;

import com.google.gson.annotations.SerializedName;

public class MyResponse {

    @SerializedName("message")
    private String message = "Parking Spot registered successfully. Thank you!";

    public String getMessage() {
        return message;
    }

}
