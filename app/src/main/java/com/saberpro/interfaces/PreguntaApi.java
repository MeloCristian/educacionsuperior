package com.saberpro.interfaces;

import com.saberpro.models.Pregunta;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PreguntaApi {

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
    })
    @FormUrlEncoded
    @POST("api/pregunta/random/{id_area}")
    Call<Pregunta> getPregunta(
            @Path("id_area") String id_area,
            @Header("Authorization") String auth,
            @Field("id_usuario") String id_usuario
    );
}
