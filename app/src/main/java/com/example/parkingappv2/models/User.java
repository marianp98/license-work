package com.example.parkingappv2.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

//    @SerializedName("username")
//    private String username;
//
//    @SerializedName("phone")
//    private String phone;
//
    @SerializedName("access_token")
    private String access_token;

    @SerializedName("password")
    private String password;

    @SerializedName("id")
    private String id;


    public User(String id, String email, String access_token, String password, String name) {
        this.id=id;
        this.email = email;
        this.access_token = access_token;
        this.password = password;
        this.name= name;

    }

//    public User(String email, String access_token, String password) {
//        this.email = email;
//        this.access_token = access_token;
//        this.password = password;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) { this.password = password; }

    public String getPassword() { return password; }
    public String getToken() { return access_token; }
//
//    public void setToken(String token) { this.token = token; }
//
//    public String getUsername() {
//        return username;

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
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
