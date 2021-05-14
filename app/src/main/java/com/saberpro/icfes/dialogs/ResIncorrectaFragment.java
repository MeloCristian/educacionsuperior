package com.saberpro.icfes.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.saberpro.icfes.R;

public class ResIncorrectaFragment extends DialogFragment {

    private String justificacion;
    private String res_correcta;
    private Button siguiente;
    private TextView tv_res_correcta;
    private TextView tv_justificacion;

    public ResIncorrectaFragment(String justificacion, String res_correcta) {
        this.justificacion =  justificacion;
        this.res_correcta = res_correcta;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return createdialogResIncorrecta();
    }

    private Dialog createdialogResIncorrecta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_res_incorrecta,null);
        builder.setView(v);
        siguiente = (Button) v.findViewById(R.id.btn_siguiente_preguntaRI);
        tv_justificacion = (TextView) v.findViewById(R.id.tv_justificacion);
        tv_res_correcta = (TextView) v.findViewById(R.id.tv_res_correcta);
        tv_justificacion.setText(this.justificacion);
        tv_res_correcta.setText(this.res_correcta);

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}