package com.saberpro.opcionesHacer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.saberpro.icfes.Materias;
import com.saberpro.icfes.R;


public class Preicfes extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_preicfes, container,false);

        Button btn = (Button)  rootView.findViewById(R.id.btn_pre_icfes);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Materias.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return rootView;
    }


}