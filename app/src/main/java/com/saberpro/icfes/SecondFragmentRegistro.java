package com.saberpro.icfes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.saberpro.Funciones.Funciones;
import com.saberpro.Interfaces.UsuarioApi;
import com.saberpro.Models.Usuario;
import com.saberpro.icfes.databinding.FragmentSecondRegistroBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondFragmentRegistro extends Fragment {

    private FragmentSecondRegistroBinding binding;
    private String email="";
    private String contrasena="";
    private String confirmarContrasena="";
    private String nombres;
    private String apellidos;
    private long fechaNacimiento;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondRegistroBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString("email", "");
            contrasena = getArguments().getString("contrasena", "");
            confirmarContrasena = getArguments().getString("confirmarContrasena", "");
            nombres = getArguments().getString("nombres", "");
            apellidos = getArguments().getString("apellidos", "");
            fechaNacimiento = getArguments().getLong("fechaNacimiento", 0);
        }
        setBackNavigation();
        registrar();
        binding.tiEmail.setText(email);
        binding.tiContrasena.setText(contrasena);
        binding.tiConfContrasena.setText(confirmarContrasena);
    }

    private boolean validarFormulario() {
        if (email.equals("") || contrasena.equals("") || confirmarContrasena.equals("")) {
            Toast.makeText(getContext(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!confirmarContrasena.equals(contrasena)) {
            Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setBackNavigation() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Bundle argumentos = new Bundle();
                argumentos.putString("nombres", nombres);
                argumentos.putString("apellidos", apellidos);
                argumentos.putLong("fechaNacimiento", fechaNacimiento);
                argumentos.putString("email", email);
                argumentos.putString("contrasena", contrasena);
                argumentos.putString("confirmarContrasena", confirmarContrasena);
                NavHostFragment.findNavController(SecondFragmentRegistro.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment, argumentos);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }

    private void registrar() {
        binding.btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.tiEmail.getText().toString();
                contrasena = binding.tiContrasena.getText().toString();
                confirmarContrasena = binding.tiConfContrasena.getText().toString();
                if (validarFormulario()) {
                    createUser();
                }
            }
        });
    }

    private void createUser() {

        try {
            Retrofit retro = new Retrofit.Builder()
                    .baseUrl(Funciones.url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UsuarioApi user = retro.create(UsuarioApi.class);
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
            Call<Usuario> call = user.addUser(email, contrasena, nombres, apellidos, 1, format.format(new Date(fechaNacimiento)));
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (!response.isSuccessful()) {
                        try {
                            Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), "Error al regitrar el usuario", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    Usuario usuario = response.body();
                    Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getContext(), login.class);
                    startActivity(i);
                    getActivity().finish();
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}