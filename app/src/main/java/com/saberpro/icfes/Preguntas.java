package com.saberpro.icfes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PreguntasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        this.id_area = getIntent().getStringExtra("id_area");
        this.nPregunta = 0;
        this.progressDialog = ProgressDialog.show(this, "Cargando...", "", true);
        setPregunta();
        setBackActions();
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

        String url = Funciones.url;
        SharedPreferences preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "DEFAULT");
        try {
            Retrofit retro = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PreguntaApi preguntaApi = retro.create(PreguntaApi.class);
            SharedPreferences sharedPref = getSharedPreferences("tokens", Context.MODE_PRIVATE);
            int id_usuario = sharedPref.getInt("id_usuario", 0);
            Call<Pregunta> call = preguntaApi.getPregunta(this.id_area, "Bearer " + token, String.valueOf(id_usuario));

            call.enqueue(new Callback<Pregunta>() {
                @Override
                public void onResponse(Call<Pregunta> call, Response<Pregunta> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Sesion caducada", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (response.body().getRespuestas().size()==0){
                        respuestaList = new ArrayList<>();
                    }
                    respuestaList = response.body().getRespuestas();
                    listener = new ListAnswersAdapter.ItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            binding.recyclerViewAnswers.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyItemRangeChanged(0,respuestaList.size());
                                }
                            });
                        }
                    };
                    adapter = new ListAnswersAdapter(respuestaList,listener);
                    binding.recyclerViewAnswers.setAdapter(adapter);
                    binding.recyclerViewAnswers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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

/*    public void siguiente(View view) {
        RadioButton selected = (RadioButton) findViewById(rg_respuestas.getCheckedRadioButtonId());
        int index = rg_respuestas.indexOfChild(selected);
        System.out.println(index);
        if (index >= 0) {
            if (respuestas[index].isCorrecta()) {
                ResCorrectaFragment resCorrectaFragment = new ResCorrectaFragment(pregunta.getClave());
                resCorrectaFragment.show(getSupportFragmentManager(), "Siguiente");
            } else {
                Respuesta correcta = null;
                for (Respuesta res : respuestas) {
                    if (res.isCorrecta()) {
                        correcta = res;
                    }
                }
                ResIncorrectaFragment resIncorrectaFragment = new ResIncorrectaFragment(pregunta.getClave(), correcta.getDescripcion());
                resIncorrectaFragment.show(getSupportFragmentManager(), "Respuesta incorrecta");
            }
            setPregunta();
            this.rg_respuestas.clearCheck();
        } else {
            Toast.makeText(getApplicationContext(), "Seleccione una opcion", Toast.LENGTH_SHORT).show();
        }

    }*/

    public void mostrarInfo(View view) {
        TeoriaFragment teoriaFragment = new TeoriaFragment(this.info);
        teoriaFragment.show(getSupportFragmentManager(), "Teoria");
    }

    public void closeProgres() {
        this.progressDialog.dismiss();
    }

}
