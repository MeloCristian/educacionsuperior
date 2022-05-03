package com.saberpro.icfes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.saberpro.icfes.databinding.FragmentFirstRegistroBinding;
import com.saberpro.pickers.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FirstFragmentRegistro extends Fragment {

    private FragmentFirstRegistroBinding binding;
    private String email;
    private String contrasena;
    private String confirmarContransena;
    private String nombres = "";
    private String apellidos = "";
    private long fechaNacimiento = 0;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstRegistroBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString("email", "");
            contrasena = getArguments().getString("contrasena", "");
            confirmarContransena = getArguments().getString("confirmarContrasena", "");
            nombres = getArguments().getString("nombres", "");
            apellidos = getArguments().getString("apellidos", "");
            fechaNacimiento = getArguments().getLong("fechaNacimiento", 0);
        }
        binding.tiNombres.setText(nombres);
        binding.tiApellidos.setText(apellidos);
        if (fechaNacimiento > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            binding.tiFecha.setText(format.format(new Date(fechaNacimiento)));
        }
        String date = fechaNacimiento > 0 ? new Date(fechaNacimiento).toString() : new Date(System.currentTimeMillis()).toString();

        binding.btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nombres = binding.tiNombres.getText().toString();
                apellidos = binding.tiApellidos.getText().toString();

                Bundle argumentos = new Bundle();
                argumentos.putString("nombres", nombres);
                argumentos.putString("apellidos", apellidos);
                argumentos.putLong("fechaNacimiento", fechaNacimiento);
                argumentos.putString("email", email);
                argumentos.putString("contrasena", contrasena);
                argumentos.putString("confirmarContrasena", confirmarContransena);

                if (validarFormilario()) {
                    NavHostFragment.findNavController(FirstFragmentRegistro.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment, argumentos);
                } else {
                    Toast.makeText(getContext(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
                }


            }
        });

        binding.tiFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean validarFormilario() {
        boolean valido = true;
        if (nombres.equals("") || apellidos.equals("") || fechaNacimiento == 0) {
            valido = false;
        }
        return valido;
    }

    private void showDatePicker() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String s_dia, s_mes, s_anio;
                s_dia = (day < 10) ? "0" + day : "" + day;
                s_mes = ((month + 1) < 10) ? "0" + (month + 1) : "" + (month + 1);
                s_anio = String.valueOf(year);
                final String selectedDate = s_anio + "-" + s_mes + "-" + s_dia;
                binding.tiFecha.setText(selectedDate);
                Calendar calendar = new GregorianCalendar(year, month, day);
                fechaNacimiento = calendar.getTimeInMillis();
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

}