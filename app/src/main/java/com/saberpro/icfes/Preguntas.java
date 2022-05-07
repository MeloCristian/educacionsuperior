package com.saberpro.icfes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.saberpro.adapters.ListAnswersAdapter;
import com.saberpro.dialogs.ResCorrectaFragment;
import com.saberpro.dialogs.ResIncorrectaFragment;
import com.saberpro.dialogs.TeoriaFragment;
import com.saberpro.funciones.Funciones;
import com.saberpro.icfes.databinding.PreguntasBinding;
import com.saberpro.interfaces.PreguntaApi;
import com.saberpro.models.Pregunta;
import com.saberpro.models.Respuesta;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Preguntas extends AppCompatActivity {
    private PreguntasBinding binding;
    private String info;
    private int nPregunta;
    private ProgressDialog progressDialog;
    private String id_area;
    private ListAnswersAdapter adapter;
    private List<Respuesta> respuestaList;
    private ListAnswersAdapter.ItemClickListener listener;
    private Pregunta pregunta;
    private Retrofit retrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PreguntasBinding.inflate(getLayoutInflater());
        setTitle(getIntent().getStringExtra("nombre_area"));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        this.id_area = getIntent().getStringExtra("id_area");
        this.nPregunta = 1;
        retrofit = new Retrofit.Builder()
                .baseUrl(Funciones.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        setPregunta();
        setBackActions();
        setListeners();
    }

    private void setListeners() {
        binding.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarInfo();
            }
        });

        binding.btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguientePregunta();
            }
        });
    }

    private void setBackActions() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(getApplicationContext(), Materias.class);
                startActivity(i);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setPregunta() {
        this.progressDialog = ProgressDialog.show(this, "Cargando...", "", true);
        binding.scrollView.scrollTo(0, 0);
        SharedPreferences preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "DEFAULT");
        try {
            PreguntaApi preguntaApi = retrofit.create(PreguntaApi.class);
            SharedPreferences sharedPref = getSharedPreferences("tokens", Context.MODE_PRIVATE);
            int id_usuario = sharedPref.getInt("id_usuario", 0);
            Call<Pregunta> call = preguntaApi.getPregunta(this.id_area, "Bearer " + token, String.valueOf(id_usuario));

            call.enqueue(new Callback<Pregunta>() {
                @Override
                public void onResponse(Call<Pregunta> call, Response<Pregunta> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Error 500", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (response.body() == null) {
                        respuestaList = new ArrayList<>();
                        return;
                    }
                    pregunta = response.body();
                    respuestaList = response.body().getRespuestas();
                    listener = new ListAnswersAdapter.ItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            binding.recyclerViewAnswers.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    };
                    adapter = new ListAnswersAdapter(respuestaList, listener);
                    binding.dvPregunta.setText(pregunta.getDescripcion());
                    binding.recyclerViewAnswers.setAdapter(adapter);
                    binding.recyclerViewAnswers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    info = response.body().getClave();
                    if (response.body().getImg() != null) {
                        String url = Funciones.generateUrl(pregunta.getImg());
                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getApplicationContext());
                        circularProgressDrawable.setStrokeWidth(5f);
                        circularProgressDrawable.setCenterRadius(30f);
                        circularProgressDrawable.start();
                        Glide.with(getApplicationContext())
                                .load(url)
                                .placeholder(circularProgressDrawable)
                                .into(binding.imgCardPregunta);

                    }
                    closeProgres();
                }

                @Override
                public void onFailure(Call<Pregunta> call, Throwable t) {
                    closeProgres();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void siguientePregunta() {
        Respuesta respuesta = adapter.getAnswer();
        if (respuesta != null) {
            if (respuesta.isCorrecta()) {
                ResCorrectaFragment resCorrectaFragment = new ResCorrectaFragment(pregunta.getClave());
                resCorrectaFragment.show(getSupportFragmentManager(), "Siguiente");
            } else {
                Respuesta correcta = null;
                for (Respuesta res : respuestaList) {
                    if (res.isCorrecta()) {
                        correcta = res;
                    }
                }
                ResIncorrectaFragment resIncorrectaFragment = new ResIncorrectaFragment(pregunta.getClave(), correcta.getDescripcion());
                resIncorrectaFragment.show(getSupportFragmentManager(), "Respuesta incorrecta");
            }
            setPregunta();
            this.respuestaList = new ArrayList<>();
            nPregunta++;
            binding.titlePregunta.setText("Pregunta " + nPregunta);
        } else {
            Toast.makeText(getApplicationContext(), "Seleccione una opcion", Toast.LENGTH_SHORT).show();
        }
    }

    public void mostrarInfo() {
        TeoriaFragment teoriaFragment = new TeoriaFragment(this.info);
        teoriaFragment.show(getSupportFragmentManager(), "Teoria");
    }

    public void closeProgres() {
        this.progressDialog.dismiss();
    }

}
