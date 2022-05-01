package com.saberpro.icfes;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.saberpro.icfes.Funciones.Funciones;
import com.saberpro.icfes.Interfaces.UsuarioApi;
import com.saberpro.icfes.Models.Usuario;
import com.saberpro.icfes.databinding.ActivityMainRegistroBinding;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainRegistro extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainRegistroBinding binding;
    private EditText correo;
    private EditText pass;
    private EditText conf_pass;
    private Funciones funciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        binding = ActivityMainRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_registro);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_registro);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
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
                        try {
                            Toast.makeText(getApplicationContext(),response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(),"Error al registro", Toast.LENGTH_SHORT).show();
                        }
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