package com.saberpro.icfes;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.saberpro.icfes.Funciones.Funciones;
import com.saberpro.icfes.Interfaces.UsuarioApi;
import com.saberpro.icfes.Models.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class registro extends AppCompatActivity {


    private EditText correo;
    private EditText pass;
    private EditText conf_pass;
    private Funciones funciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().hide();

        this.correo = findViewById(R.id.et_correo_reg);
        this.pass = findViewById(R.id.et_contrasena_reg);
        this.conf_pass = findViewById(R.id.et_conf_pass_reg);
        this.funciones = new Funciones();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void registrar(View view){
        String email = this.correo.getText().toString();
        String pass = this.pass.getText().toString();
        String conf_pass = this.conf_pass.getText().toString();
        if(conf_pass.equals(pass)){
            setUser(email,pass);
        }else{
            Toast.makeText(getApplicationContext(),"Contraseñas no coinciden",Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void setUser(String email, String pass){

        try{
            Retrofit retro = new Retrofit.Builder()
                    .baseUrl(funciones.getUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UsuarioApi user = retro.create(UsuarioApi.class);

            Call<Usuario> call = user.addUser(email,pass);

            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Fallo registro", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Usuario usuario = response.body();
                    Toast.makeText(getApplicationContext(),"Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),login.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
}