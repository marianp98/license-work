package com.example.parkingappv2.models;

import com.google.gson.annotations.SerializedName;

public class Availability {

    

    @SerializedName("start_date") // used to prevent the voilation of naming convention
    private String start_date;

    @SerializedName("stop_date")
    private String stop_date;

    @SerializedName("start_time")
    private String start_time;

    @SerializedName("stop_time")
    private String stop_time;

    @SerializedName("id")
    private String id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("parkingspot_id")
    private String parkingspot_id;

    private String claimed;
//    public Availability(String id, String start_date, String stop_date, String start_time, String stop_time) {
//        this.id = id;
//        this.start_date = start_date;
//        this.stop_date = stop_date;
//        this.start_time = start_time;
//        this.stop_time = stop_time;
//    }

//    public Availability(String id, String start_date, String stop_date) {
//        this.id = id;
//        this.start_date = start_date;
//        this.stop_date = stop_date;
//    }

    public Availability(String user_id, String id, String parkingspot_id, String start_date, String stop_date) {
        this.user_id=user_id;
        this.id = id;
        this.parkingspot_id = parkingspot_id;
        this.start_date = start_date;
        this.stop_date = stop_date;
    }
    public Availability(String user_id, String id, String parkingspot_id, String start_date, String stop_date, String claimed) {
        this.user_id=user_id;
        this.id = id;
        this.parkingspot_id = parkingspot_id;
        this.start_date = start_date;
        this.stop_date = stop_date;
        this.claimed = claimed;

    }

    public String getParkingspot_id() {
        return parkingspot_id;
    }

    public String getClaimed() {
        return claimed;
    }

    public void setClaimed(String claimed) {
        this.claimed = claimed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStop_date() {
        return stop_date;
    }

    public void setStop_date(String stop_date) {
        this.stop_date = stop_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getStop_time() {
        return stop_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setParkingspot_id(String parkingspot_id) {
        this.parkingspot_id = parkingspot_id;
    }
}
