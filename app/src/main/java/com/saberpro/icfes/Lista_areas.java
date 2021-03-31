package com.saberpro.icfes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.saberpro.icfes.opciones_hacer.Preicfes;
import com.saberpro.icfes.opciones_hacer.Simulacros;

import java.util.ArrayList;
import java.util.List;

public class Lista_areas extends AppCompatActivity{

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_areas);
        getSupportActionBar()
                .hide();

        List<Fragment>  list = new ArrayList<>();
        list.add(new Preicfes());
        list.add(new Simulacros());

        pager = findViewById(R.id.pager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(),list);
        pager.setAdapter(pagerAdapter);
    }
}