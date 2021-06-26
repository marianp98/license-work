package com.example.parkingappv2;

import com.example.parkingappv2.models.Availability;
import com.example.parkingappv2.models.ParkingSpot;
import com.example.parkingappv2.models.User;
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


    //************************User Related
    @FormUrlEncoded
    @POST("login")
    Call<User> createUser(
            @Field("email") String email,
            @Field("password") String password
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

    @POST("logout")
    Call<MyResponse> Logout(
            @Header("Authorization") String authorization
    );

    @POST("destroyuser")
    Call<JsonObject> destroyuser(
            @Header("Authorization") String authorization
    );

    @GET("showuser")
    Call<JsonObject> getUsers(
            @Header("Authorization") String authorization
    );


    //************************ParkingSpot Related
    @FormUrlEncoded
    @POST("parking")
    Call<ParkingSpot> postParkLocation(
            @Header("Authorization") String authorization,
            @Field("town") String town,
            @Field("address") String adrs,
            @Field("parkingNumber") int parkingNumber,
            @Field("latitude") double lat,
            @Field("longitude") double lng,
            @Field("available") int available,
            @Field("claimed") int claimed,
            @Field("start_date") String start_date,
            @Field("stop_date") String stop_date
    );

    @FormUrlEncoded
    @POST("claimed")
    Call<ParkingSpot> claimSpot(
            @Header("Authorization") String authorization,
            @Field("id") String id,
            @Field("claimed") int claimed
    );

    @GET("showparking")
    Call<List<ParkingSpot>> getParking(
            @Header("Authorization") String authorization
    );

    @FormUrlEncoded
    @POST("available")
    Call<ParkingSpot> availableSpot(
            @Header("Authorization") String authorization,
            @Field("id") String id,
            @Field("available") int available
    );

    @FormUrlEncoded
    @POST("attachDate")
    Call<ParkingSpot> updateParkingDate(
            @Header("Authorization") String authorization,
            @Field("id") String id,
            @Field("start_date") String start_date,
            @Field("stop_date") String stop_date
    );

    @GET("destroy")
    Call<String> destroy(
            @Header("Authorization") String authorization,
            @Query("id") String id
    );


    //************************Availability Related
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
    Call<Availability> deleteAvailability(
            @Header("Authorization") String authorization,
            @Field("id") String id
    );
//********************************For future updates
//    @POST("showAvailableSpots")
//    Call<List<Availability>> showAvailableSpots(
//            @Header("Authorization") String authorization,
//            @Query("id") String id
//    );
//
//    @POST("showUnavailableSpots")
//    Call<List<Availability>> showUnavailableSpots(
//            @Header("Authorization") String authorization,
//            @Query("id") String id
//    );


}