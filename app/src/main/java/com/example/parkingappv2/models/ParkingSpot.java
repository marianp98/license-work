package com.example.parkingappv2.models;

public class ParkingSpot {
    private String id, user_id, town, address, parkingNumber, latitude, longitude;
    private boolean isAvailable;
    private boolean isClaimed;



    public ParkingSpot(String id, String user_id, String town, String address, String parkingNumber, String latitude, String longitude) {
        this.id = id;
        this.user_id = user_id;
        this.town = town;
        this.address = address;
        this.parkingNumber = parkingNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ParkingSpot(String id, String address, String parkingNumber , boolean isAvailable, boolean isClaimed) {
        this.id = id;
        this.address = address;
        this.parkingNumber = parkingNumber;
        this.isAvailable = isAvailable;
        this.isClaimed = isClaimed;

    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isClaimed() {
        return isClaimed; }

    public void setClaimed(boolean claimed) {
        isClaimed = claimed;
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
