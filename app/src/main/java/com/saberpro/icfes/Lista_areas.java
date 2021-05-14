package com.saberpro.icfes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saberpro.icfes.opciones_hacer.Preicfes;
import com.saberpro.icfes.opciones_hacer.Simulacros;

import java.util.ArrayList;
import java.util.List;

public class Lista_areas extends AppCompatActivity{

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private LinearLayout linearLayout_puntos;
    private TextView[] puntosSlide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_areas);;

        List<Fragment>  list = new ArrayList<>();
        list.add(new Preicfes());
        list.add(new Simulacros());

        pager = findViewById(R.id.pager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(),list);
        pager.setAdapter(pagerAdapter);

        linearLayout_puntos = findViewById(R.id.lista_puntos);
        agregarIndPuntos(0);
        pager.addOnPageChangeListener(this.viewListener);
    }

    private void agregarIndPuntos(int pos) {
        puntosSlide = new TextView[2];
        linearLayout_puntos.removeAllViews();
        for(int i=0;i<puntosSlide.length;i++){
            puntosSlide[i] = new TextView(this);
            System.out.println(puntosSlide[i]);
            puntosSlide[i].setText(Html.fromHtml("&#8226;"));
            puntosSlide[i].setTextSize(getResources().getDimension(R.dimen.font_size_title2));
            puntosSlide[i].setTextColor(getResources().getColor(R.color.azul_fondo3));
            linearLayout_puntos.addView(puntosSlide[i]);
        }

        if(puntosSlide.length>0){
            puntosSlide[pos].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new  ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            agregarIndPuntos(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}