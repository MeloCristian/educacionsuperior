package com.saberpro.icfes.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.saberpro.icfes.R;


public class TeoriaFragment extends DialogFragment {

    private Button entendido;
    private TextView tv_teoria;
    private String info;

    public TeoriaFragment(String info){
        this.info = info;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return createDialogTeoria();
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }

    private AlertDialog createDialogTeoria(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_teoria,null);
        builder.setView(v);
        entendido = (Button) v.findViewById(R.id.btn_entendido);
        tv_teoria = (TextView) v.findViewById(R.id.tv_teoria);
        tv_teoria.setText(this.info);
        entendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }
}