
        package com.saberpro.icfes;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.saberpro.Funciones.Funciones;
import com.saberpro.Interfaces.AuthApi;
import com.saberpro.Models.Auth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class login extends AppCompatActivity {


    private EditText correo;
    private EditText pass;
    private Funciones funciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.correo = findViewById(R.id.et_correo_login);
        this.pass = findViewById(R.id.et_contrasena_login);
        funciones = new Funciones();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        listen_email();
    }

    public void inciar_usuario(View view){
        String email = this.correo.getText().toString();
        if(validaremail(email)){
            String pass = this.pass.getText().toString();
            login(email, pass);
        }else {
            Toast.makeText(getApplicationContext(),"Email no valido",Toast.LENGTH_SHORT).show();
        }

    }

    private void login(String email, String pass ){
        String url = Funciones.url;
        try{
            Retrofit retro = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AuthApi authApi = retro.create(AuthApi.class);

            Call<Auth> call = authApi.sigin(email,pass);

            call.enqueue(new Callback<Auth>() {
                @Override
                public void onResponse(Call<Auth> call, Response<Auth> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Fallo inicio de sesión", Toast.LENGTH_SHORT).show();
                        System.out.println("errr "+response.errorBody().charStream());
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

    protected boolean validaremail(String email){
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }

    protected void listen_email(){
        TextWatcher text = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validaremail(charSequence.toString())){
                    correo.setTextColor(getResources().getColor(R.color.white));
                }else{
                    correo.setTextColor(getResources().getColor(R.color.teal_200));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        correo.addTextChangedListener(text);
    }
}