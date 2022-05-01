package com.saberpro.Interfaces;


import com.saberpro.Models.Usuario;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UsuarioApi {
    @FormUrlEncoded
    @POST("api/user/add")
    Call<Usuario> addUser(
            @Field("email_us") String email,
            @Field("pass_us") String pass,
            @Field("nombres") String nombres,
            @Field("apellidos") String apellidos,
            @Field("id_rol") int idRol,
            @Field("fecha_nacimiento") String fecha
    );
}
