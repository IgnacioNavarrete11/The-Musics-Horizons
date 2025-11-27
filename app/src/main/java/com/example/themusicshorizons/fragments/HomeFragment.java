package com.example.themusicshorizons.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.activities.ExploreEventsActivity;
import com.example.themusicshorizons.activities.ScanAudioActivity;
import com.example.themusicshorizons.activities.ScanVisualActivity;

// Este Fragment representa la pantalla principal o "Home" de la aplicación.
public class HomeFragment extends Fragment {

    // Este método se ejecuta para crear la vista del fragment.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // "Infla" o carga el diseño XML del fragment en una vista de Java para poder manipularla.
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Asocia las variables con los botones definidos en el archivo XML.
        Button scanAudioButton = view.findViewById(R.id.scanAudioButton);
        Button scanVisualButton = view.findViewById(R.id.scanVisualButton);
        Button exploreEventsButton = view.findViewById(R.id.exploreEventsButton);

        // Asigna la acción de abrir la pantalla de escaneo de audio al pulsar el botón correspondiente.
        scanAudioButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), ScanAudioActivity.class)));
        // Asigna la acción de abrir la pantalla de escaneo visual (QR) al pulsar el botón correspondiente.
        scanVisualButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), ScanVisualActivity.class)));
        // Asigna la acción de abrir la pantalla de exploración de eventos al pulsar el botón correspondiente.
        exploreEventsButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), ExploreEventsActivity.class)));

        // Devuelve la vista ya configurada para que se muestre en pantalla.
        return view;
    }
}
