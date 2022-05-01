package com.saberpro.Interfaces;

import com.saberpro.Models.Pregunta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface PreguntaApi {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/pregunta/random/{id_area}")
    Call<Pregunta> getPregunta(@Path("id_area") String id_area, @Header("Authorization") String auth);
}
