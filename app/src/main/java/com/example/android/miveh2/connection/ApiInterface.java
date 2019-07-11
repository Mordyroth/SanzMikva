package com.example.android.miveh2.connection;

import com.example.android.miveh2.model.SunsetModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    //https://api.sunrise-sunset.org/json?lat=23.0216238&lng=72.5797068&date=today

    @GET("json?")
    Call<SunsetModel> getSunsetResponse(@Query("lat") double lat, @Query("lng") double lng, @Query("date") String today);


}