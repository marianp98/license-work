package com.example.parkingappv2.models;

public class Availability {


    //  @SerializedName("start_date") // used to prevent the violation of naming convention
    private String start_date, stop_date, start_time, stop_time, id, user_id, parkingspot_id, claimed;

    public Availability(String user_id, String id, String parkingspot_id, String start_date, String stop_date) {
        this.user_id = user_id;
        this.id = id;
        this.parkingspot_id = parkingspot_id;
        this.start_date = start_date;
        this.stop_date = stop_date;
    }

    public Availability(String user_id, String id, String parkingspot_id, String start_date, String stop_date, String claimed) {
        this.user_id = user_id;
        this.id = id;
        this.parkingspot_id = parkingspot_id;
        this.start_date = start_date;
        this.stop_date = stop_date;
        this.claimed = claimed;
    }

    public String getParkingspot_id() {
        return parkingspot_id;
    }

    public void setParkingspot_id(String parkingspot_id) {
        this.parkingspot_id = parkingspot_id;
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

}
