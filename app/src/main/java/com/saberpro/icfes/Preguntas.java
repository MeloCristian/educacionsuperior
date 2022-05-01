package com.saberpro.icfes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.saberpro.Funciones.Funciones;
import com.saberpro.Interfaces.PreguntaApi;
import com.saberpro.Models.Pregunta;
import com.saberpro.Models.Respuesta;
import com.saberpro.dialogs.ResCorrectaFragment;
import com.saberpro.dialogs.ResIncorrectaFragment;
import com.saberpro.dialogs.TeoriaFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Preguntas extends AppCompatActivity {

    private String id_area;
    private TextView tv_pregunta;
    private TextView tv_nPregunta;
    private RadioGroup rg_respuestas;
    private RadioButton rb_respuestaA;
    private RadioButton rb_respuestaB;
    private RadioButton rb_respuestaC;
    private RadioButton rb_respuestaD;
    private String info;
    private int nPregunta;
    private ScrollView scroll_preguntas;
    private ProgressDialog progressDialog;
    private Respuesta[] respuestas;
    private Pregunta pregunta;
    private Funciones funciones;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preguntas);
        funciones = new Funciones();

        this.id_area = getIntent().getStringExtra("id_area");
        this.tv_pregunta = (TextView) findViewById(R.id.tv_pregunta);
        this.tv_nPregunta = (TextView) findViewById(R.id.title_pregunta);
        this.rg_respuestas = (RadioGroup) findViewById(R.id.respuestas);
        this.rb_respuestaA = (RadioButton) findViewById(R.id.rb_respuestaA);
        this.rb_respuestaB = (RadioButton) findViewById(R.id.rb_respuestaB);
        this.rb_respuestaC = (RadioButton) findViewById(R.id.rb_respuestaC);
        this.rb_respuestaD = (RadioButton) findViewById(R.id.rb_respuestaD);
        this.scroll_preguntas = (ScrollView) findViewById(R.id.scroll_pregunta);
        this.nPregunta=0;
        this.progressDialog = ProgressDialog.show(this, "Cargando...","",true);
        setPregunta();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(getApplicationContext(),Materias.class);
                startActivity(i);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

    }

    private void setPregunta(){
        this.scroll_preguntas.fullScroll(View.FOCUS_UP);
        String url = Funciones.url;
        SharedPreferences preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        String token = preferences.getString("token","DEFAULT");
        try{
            Retrofit retro = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PreguntaApi preguntaApi = retro.create(PreguntaApi.class);

            Call<Pregunta> call = preguntaApi.getPregunta(this.id_area, "Bearer "+token);

            call.enqueue(new Callback<Pregunta>() {
                @Override
                public void onResponse(Call<Pregunta> call, Response<Pregunta> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Sesion caducada", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setPregunta(response.body());
                    setInfo(response.body().getTeroria());
                    String descripcion_pre = response.body().getDescripcion();
                    setText_tv_pregunta(descripcion_pre);
                    setRespuestas(response.body().getRespuestas());
                    setTextRA("A: "+getRespuestas()[0].getDescripcion());
                    setTextRB("B: "+getRespuestas()[1].getDescripcion());
                    setTextRC("C: "+getRespuestas()[2].getDescripcion());
                    setTextRD("D: "+getRespuestas()[3].getDescripcion());
                    setnPregunta(getnPregunta()+1);
                    setTv_nPreguntaText();
                    closeProgres();
                }

                @Override
                public void onFailure(Call<Pregunta> call, Throwable t) {
                    closeProgres();
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();

                }
            });


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void siguiente(View view){
        RadioButton selected = (RadioButton) findViewById(rg_respuestas.getCheckedRadioButtonId());
        int index = rg_respuestas.indexOfChild(selected);
        System.out.println(index);
        if (index>=0){
            if (respuestas[index].isCorrecta()){
                ResCorrectaFragment resCorrectaFragment = new ResCorrectaFragment(pregunta.getClave());
                resCorrectaFragment.show(getSupportFragmentManager(), "Siguiente");
            }else{
                Respuesta correcta=null;
                for (Respuesta res: respuestas){
                    if(res.isCorrecta()){
                        correcta = res;
                    }
                }
                ResIncorrectaFragment resIncorrectaFragment = new ResIncorrectaFragment(pregunta.getClave(),correcta.getDescripcion());
                resIncorrectaFragment.show(getSupportFragmentManager(),"Respuesta incorrecta");
            }
            setPregunta();
            this.rg_respuestas.clearCheck();
        }else{
            Toast.makeText(getApplicationContext(),"Seleccione una opcion",Toast.LENGTH_SHORT).show();
        }

    }

    public void mostrarInfo(View view){
        TeoriaFragment teoriaFragment = new TeoriaFragment(this.info);
        teoriaFragment.show(getSupportFragmentManager(), "Teoria");
    }

    public void setText_tv_pregunta(String text){
        this.tv_pregunta.setText(text);
    }

    public void setTextRA(String text){
        this.rb_respuestaA.setText(text);
    }

    public void setTextRB(String text){
        this.rb_respuestaB.setText(text);
    }

    public void setTextRC(String text){
        this.rb_respuestaC.setText(text);
    }

    public void setTextRD(String text){
        this.rb_respuestaD.setText(text);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getnPregunta() {
        return nPregunta;
    }

    public void setnPregunta(int nPregunta) {
        this.nPregunta = nPregunta;
    }

    public void setTv_nPreguntaText(){
        this.tv_nPregunta.setText("Pregunta "+this.nPregunta);
    }

    public void closeProgres(){
        this.progressDialog.dismiss();
    }

    public Respuesta[] getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(Respuesta[] respuestas) {
        this.respuestas = respuestas;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }
}
