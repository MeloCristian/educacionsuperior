package com.saberpro.icfes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saberpro.adapters.ListAreasAdapter;
import com.saberpro.funciones.Funciones;
import com.saberpro.icfes.databinding.ActivityMateriasBinding;
import com.saberpro.interfaces.AreaApi;
import com.saberpro.models.Area;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Materias extends AppCompatActivity {

    private ActivityMateriasBinding binding;
    private Retrofit retrofit;
    private ListAreasAdapter adapter;
    private List<Area> areaList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMateriasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setBackActions();
        retrofit = new Retrofit.Builder()
                .baseUrl(Funciones.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getAllAreas();
    }

    private void getAllAreas() {

        try {
            AreaApi areaApi = retrofit.create(AreaApi.class);
            Call<List<Area>> call = areaApi.getAll();

            call.enqueue(new Callback<List<Area>>() {

                @Override
                public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Error al obtener areas", Toast.LENGTH_SHORT).show();
                        Log.e("getAllAreas", "errr " + response.errorBody().charStream());
                        return;
                    }
                    System.out.println(response.body().toString());
                    areaList = response.body();
                    adapter = new ListAreasAdapter(areaList);
                    binding.recyclerView.setAdapter(adapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }

                @Override
                public void onFailure(Call<List<Area>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            Log.e("getAllAreas", e.toString());
        }
    }

    private void setBackActions() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(getApplicationContext(), Lista_areas.class);
                startActivity(i);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void ciudadanas(View view) {
        Intent i = new Intent(getApplicationContext(), Preguntas.class);
        i.putExtra("id_area", "1007");
        i.putExtra("nombre_area", "Sociales y ciudadanas");
        startActivity(i);
        finish();
    }
}
