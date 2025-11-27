package com.example.themusicshorizons.fragments;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // La clase base para todos los fragmentos.
import androidx.viewpager2.widget.ViewPager2; // El componente moderno para crear swipes entre pantallas.

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.adapters.MyHorizonPagerAdapter; // Nuestro propio adaptador para gestionar las pestañas.
import com.google.android.material.tabs.TabLayout; // El componente que muestra las pestañas en sí.
import com.google.android.material.tabs.TabLayoutMediator; // El "pegamento" que une las pestañas con el contenido deslizable.

// Este Fragment actúa como el contenedor principal para la sección "Mi Horizonte".
// Su única misión es configurar y gestionar las pestañas de "Canciones" y "Eventos".
public class MyHorizonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Se "infla" o carga el diseño XML que contiene el TabLayout y el ViewPager2.
        View view = inflater.inflate(R.layout.fragment_my_horizon, container, false);

        // Se asocian las variables con los componentes del layout.
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        // Se crea una instancia del adaptador que se encargará de gestionar los fragments de cada pestaña.
        MyHorizonPagerAdapter pagerAdapter = new MyHorizonPagerAdapter(requireActivity());
        // Se asigna el adaptador al ViewPager.
        viewPager.setAdapter(pagerAdapter);

        // Este objeto es el que realmente une las pestañas (TabLayout) con el contenido deslizable (ViewPager).
        // También se encarga de poner el título a cada pestaña según su posición.
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: // La primera pestaña (posición 0)
                    tab.setText("Canciones");
                    break;
                case 1: // La segunda pestaña (posición 1)
                    tab.setText("Eventos");
                    break;
            }
        }).attach(); // Esta llamada activa la conexión y hace que todo funcione.

        // Devuelve la vista ya configurada para que se muestre en pantalla.
        return view;
    }
}
