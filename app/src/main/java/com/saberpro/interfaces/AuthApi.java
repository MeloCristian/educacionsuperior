package com.saberpro.interfaces;

import com.saberpro.models.Auth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthApi {
    @FormUrlEncoded
    @POST("api/auth/sigin")
    Call<Auth> sigin(@Field("email_us") String email,@Field("pass_us") String pass);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/auth/verifica_sesion")
    Call<Auth> verificar_sesion(@Header("Authorization") String auth);
}
