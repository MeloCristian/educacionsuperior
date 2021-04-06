package com.saberpro.icfes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.saberpro.icfes.Funciones.Funciones;
import com.saberpro.icfes.Interfaces.AuthApi;
import com.saberpro.icfes.Models.Auth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Funciones funciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        funciones = new Funciones();
        validar_sesionActiva();

    }

    public void iniciar_sesion(View view){
        Intent i = new Intent(getApplicationContext(),login.class);
        startActivity(i);
        finish();
    }

    public void registrarce(View view){
        Intent i = new Intent(getApplicationContext(),registro.class);
        startActivity(i);
        finish();
    }

    protected void validar_sesionActiva(){
        String url = funciones.getUrl();
        SharedPreferences preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        String token = preferences.getString("token","DEFAULT");
        try{
            Retrofit retro = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AuthApi authApi = retro.create(AuthApi.class);

            Call<Auth> call = authApi.verificar_sesion("Bearer "+token);

            call.enqueue(new Callback<Auth>() {
                @Override
                public void onResponse(Call<Auth> call, Response<Auth> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Sesion caducada", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SharedPreferences preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("token",response.body().getToken());
                    editor.commit();
                    Intent i = new Intent(getApplicationContext(),Lista_areas.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onFailure(Call<Auth> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}