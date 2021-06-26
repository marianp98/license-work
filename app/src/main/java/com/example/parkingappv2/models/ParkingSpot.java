package com.example.parkingappv2.models;

public class ParkingSpot {
    private String id, user_id, town, address, parkingNumber, latitude, longitude, stop_date, start_date;
    private int available, claimed;


    public ParkingSpot(String id, String user_id, String town, String address, String parkingNumber, String latitude, String longitude) {
        this.id = id;
        this.user_id = user_id;
        this.town = town;
        this.address = address;
        this.parkingNumber = parkingNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ParkingSpot(String id, String address, String parkingNumber, int available, int claimed) {
        this.id = id;
        this.address = address;
        this.parkingNumber = parkingNumber;
        this.available = available;
        this.claimed = claimed;

    }

    public ParkingSpot(String id, String user_id, String town, String address, String parkingNumber, String latitude, String longitude, int available, int claimed, String start_date, String stop_date) {
        this.id = id;
        this.user_id = user_id;
        this.town = town;
        this.address = address;
        this.parkingNumber = parkingNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.available = available;
        this.claimed = claimed;
        this.start_date = start_date;
        this.stop_date = stop_date;
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

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getClaimed() {
        return claimed;
    }

    public void setClaimed(int claimed) {
        this.claimed = claimed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParkingNumber() {
        return parkingNumber;
    }

    public void setParkingNumber(String parkingNumber) {
        this.parkingNumber = parkingNumber;
    }

}
