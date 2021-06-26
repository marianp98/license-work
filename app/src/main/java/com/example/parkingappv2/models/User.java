package com.example.parkingappv2.models;

import com.google.gson.annotations.SerializedName;

public class User {

    //  @SerializedName("name") // used to prevent the violation of naming convention

    private String name, email, username, phone, access_token, password, id;


    public User(String id, String email, String access_token, String password, String name) {
        this.id = id;
        this.email = email;
        this.access_token = access_token;
        this.password = password;
        this.name = name;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return access_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
