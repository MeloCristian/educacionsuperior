
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
import android.widget.Toast;

import com.saberpro.funciones.Funciones;
import com.saberpro.interfaces.AuthApi;
import com.saberpro.models.Auth;
import com.saberpro.icfes.databinding.ActivityLoginBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setBackActions();
        setListeners();
        listen_email();
    }

    private void setListeners() {
        binding.btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });
    }

    private void setBackActions() {
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

    public void iniciarSesion(){
        String email = binding.tiEmail.getText().toString();
        if(validaremail(email)){
            String pass = binding.tiContrasena.getText().toString();
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
                    editor.putInt("id_usuario", response.body().getId());
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
                    binding.tiEmail.setTextColor(getResources().getColor(R.color.white));
                }else{
                    binding.tiEmail.setTextColor(getResources().getColor(R.color.teal_200));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        binding.tiEmail.addTextChangedListener(text);
    }
}