package com.saberpro.interfaces;

import com.saberpro.models.Area;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface AreaApi {
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/area/getAll")
    Call<List<Area>> getAll();
}
