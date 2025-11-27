package com.example.themusicshorizons.adapters;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import androidx.annotation.NonNull; // Anotación para indicar que un parámetro o método nunca puede ser nulo.
import androidx.fragment.app.Fragment; // La clase base para todos los fragmentos.
import androidx.fragment.app.FragmentActivity; // Una clase base para actividades que usan fragmentos de soporte.
import androidx.viewpager2.adapter.FragmentStateAdapter; // El tipo de adaptador necesario para conectar un ViewPager2 con Fragments.

import com.example.themusicshorizons.fragments.SavedEventsFragment; // El fragment que muestra la lista de eventos guardados.
import com.example.themusicshorizons.fragments.SavedSongsFragment; // El fragment que muestra la lista de canciones guardadas.

// Este adaptador es el "director de orquesta" de las pestañas de "Mi Horizonte".
// Su única misión es decirle al ViewPager qué fragmento debe mostrar en cada posición.
public class MyHorizonPagerAdapter extends FragmentStateAdapter {

    public MyHorizonPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // Este método se llama para crear el fragmento de una posición específica.
    // Es el corazón del adaptador.
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: // Para la primera pestaña (posición 0)
                return new SavedSongsFragment(); // Devuelve una nueva instancia del fragment de canciones.
            case 1: // Para la segunda pestaña (posición 1)
                return new SavedEventsFragment(); // Devuelve una nueva instancia del fragment de eventos.
            default:
                // Como medida de seguridad, si por alguna razón se pide una posición inválida,
                // siempre devolverá el fragment de canciones para evitar un crash.
                return new SavedSongsFragment();
        }
    }

    // Este método le dice al ViewPager cuántas pestañas o páginas va a haber en total.
    @Override
    public int getItemCount() {
        return 2; // Tenemos 2 pestañas: Canciones y Eventos.
    }
}
