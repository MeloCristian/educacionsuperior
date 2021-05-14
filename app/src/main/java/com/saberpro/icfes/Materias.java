package com.saberpro.icfes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Materias extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias);


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(getApplicationContext(),Lista_areas.class);
                startActivity(i);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

    }

    public void ciudadanas(View view){
        Intent i = new Intent(getApplicationContext(),Preguntas.class);
        i.putExtra("id_area","1007");
        i.putExtra("nombre_area","Sociales y ciudadanas");
        startActivity(i);
        finish();
    }
}
