package com.saberpro.icfes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.saberpro.funciones.Funciones;
import com.saberpro.interfaces.AuthApi;
import com.saberpro.models.Auth;
import com.saberpro.icfes.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        validar_sesionActiva();
        setListeners();
        setBackActions();
    }

    private void setListeners() {
        binding.btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciar_sesion();
            }
        });

        binding.btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarce();
            }
        });
    }

    private void setBackActions() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle(getResources().getString(R.string.advertencia))
                        .setMessage(getResources().getString(R.string.sms_salir))
                        .setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }


    public void iniciar_sesion() {
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
        finish();
    }

    public void registrarce() {
        Intent i = new Intent(getApplicationContext(), MainRegistro.class);
        startActivity(i);
        finish();
    }

    protected void validar_sesionActiva() {
        String url = Funciones.url;
        SharedPreferences preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "DEFAULT");
        try {
            Retrofit retro = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AuthApi authApi = retro.create(AuthApi.class);

            Call<Auth> call = authApi.verificar_sesion("Bearer " + token);

            call.enqueue(new Callback<Auth>() {
                @Override
                public void onResponse(Call<Auth> call, Response<Auth> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Sesion caducada", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SharedPreferences preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.commit();
                    Intent i = new Intent(getApplicationContext(), Lista_areas.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onFailure(Call<Auth> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}