package com.saberpro.icfes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.saberpro.icfes.databinding.ActivityListaAreasBinding;
import com.saberpro.opcionesHacer.Preicfes;
import com.saberpro.opcionesHacer.Simulacros;

import java.util.ArrayList;
import java.util.List;

public class Lista_areas extends AppCompatActivity {

    private PagerAdapter pagerAdapter;
    private ActivityListaAreasBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaAreasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setTitle(R.string.app_name);
        List<Fragment> list = new ArrayList<>();
        list.add(new Preicfes());
        list.add(new Simulacros());

        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);
        binding.pager.setAdapter(pagerAdapter);
        agregarIndPuntos();

    }

    private void agregarIndPuntos() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(binding.pager, true);
    }

}