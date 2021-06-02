package com.example.parkingappv2;

import com.example.parkingappv2.models.Availability;
import com.example.parkingappv2.models.ParkingSpot;
import com.example.parkingappv2.models.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyApi {

    @FormUrlEncoded
    @POST("parking")
    Call<MyResponse> postParkLocation(
            @Header("Authorization") String authorization,
            @Field("town") String town,
            @Field("address") String adrs,
            @Field("parkingNumber") int parkingNumber,
            @Field("latitude") double lat,
            @Field("longitude") double lng,
            @Field("claimed") int claimed

    );

    @FormUrlEncoded
    @POST("claimed")
    Call<ParkingSpot> claimSpot(
            @Header("Authorization") String authorization,
            @Field("id") String id,
            @Field("claimed") int claimed
    );

    @FormUrlEncoded
    @POST("login")
    Call<User> createUser(
            @Field("email") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("displayavailability")
    Call<JsonArray> showAvailabilities(
            @Header("Authorization") String authorization
//            @Query("id") String id,
//            @Query("parkingspot_id") String parkingspot_id,
//            @Query("start_date") String start_date,
//            @Query("stop_date") String stop_date
    );

//    @POST("availabilitybutton")
//    Call<List<Availability>> getAvailableSlots(@Header("Authorization") String authorization);



    @POST("availabilitybutton1")
    Call<List<Availability>> getAvailableSlots1(
            @Header("Authorization") String authorization,
            @Query("id") String id
    );

    @GET("updatephone")
    Call<JsonObject> phoneUpdate(
            @Header("Authorization") String authorization,
            @Query("phone") String phone
    );


    @FormUrlEncoded
    @POST("updateemail")
    Call<JsonObject> emailUpdate(
            @Header("Authorization") String authorization,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("availability")
    Call<Availability> createAvailability(
            @Field("start_date") String start_date,
            @Field("stop_date") String stop_date,
            @Field("id") String id,
            @Header("Authorization") String authorization
    );


    @FormUrlEncoded
    @POST("availabilitydestroy")
    Call<JsonObject> deleteAvailability(
            @Header("Authorization") String authorization,
            @Field("id") String id

    );

    @POST("logout")
    Call<MyResponse> Logout(
            @Header("Authorization") String authorization
    );

//    @GET("showparking")
//    Call<JsonArray> postShowParkings(
//            @Header("Authorization") String authorization
//    );

    @GET("showparking")
    Call<List<ParkingSpot>> getParking(
            @Header("Authorization") String authorization
    );

    @GET("showuser")
    Call<JsonObject> getUsers(
            @Header("Authorization") String authorization
    );

    @GET("destroy")
    Call<String> destroy(
            @Header("Authorization") String authorization,
            @Query("id") String id

    );

    @FormUrlEncoded
    @POST("phone")
    Call<String> updatePhone(
            @Field("phoneNumber") String phoneNumber,
            @Header("Authorization") String authorization
    );

    @FormUrlEncoded
    @POST("email")
    Call<String> updateEmail(
            @Field("email") String email,
            @Header("Authorization") String authorization
    );

    @POST("destroyuser")
    Call<JsonObject> destroyuser(
            @Header("Authorization") String authorization
    );



//    @Headers({"Accept: application/json"})
//    @FormUrlEncoded
//    @POST("availability")
//    Call<Availability> createAvailability(
//            @Field("start_date") String start_date,
//            @Field("stop_date") String stop_date,
//            @Header("Authorization") String authorization
//    );
}