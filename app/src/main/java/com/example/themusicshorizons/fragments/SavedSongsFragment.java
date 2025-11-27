package com.example.themusicshorizons.fragments;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.database.Cursor; // Herramienta para leer los resultados de la base de datos.
import android.os.Bundle; // Herramienta para pasar datos.
import android.view.LayoutInflater; // Herramienta para "inflar" o cargar un layout XML en una vista de Java.
import android.view.View; // La base de todos los componentes visuales.
import android.view.ViewGroup; // Representa un contenedor de vistas.
import android.widget.TextView; // El componente para mostrar texto.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // La clase base para todos los fragmentos.
import androidx.recyclerview.widget.LinearLayoutManager; // Define la organización de la lista.
import androidx.recyclerview.widget.RecyclerView; // El componente para mostrar listas.

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.adapters.SongCursorAdapter; // Nuestro propio adaptador para la lista de canciones.
import com.example.themusicshorizons.database.DatabaseHelper; // Nuestro propio "fontanero" de la base de datos.
import com.example.themusicshorizons.utils.SessionManager; // Nuestra propia "memoria" del usuario.

// Este Fragment representa el contenido de la pestaña "Canciones" dentro de la sección "Mi Horizonte".
public class SavedSongsFragment extends Fragment {

    // Se declaran las variables para los componentes y herramientas.
    private RecyclerView recyclerView;
    private SongCursorAdapter adapter;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private TextView noSongsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Se carga el diseño XML de este fragment, que contiene la lista y el texto de "no hay canciones".
        View view = inflater.inflate(R.layout.fragment_saved_songs, container, false);

        // Se inicializan las herramientas.
        dbHelper = new DatabaseHelper(getContext());
        sessionManager = new SessionManager(getContext());

        // Se asocian las variables con los componentes del layout.
        recyclerView = view.findViewById(R.id.savedSongsRecyclerView);
        noSongsTextView = view.findViewById(R.id.noSongsTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    // Este método se ejecuta cada vez que el fragment se vuelve visible para el usuario (ej: al abrir la app o al deslizar a esta pestaña).
    // Se usa onResume() en lugar de onStart() para asegurar que la lista se refresque siempre que el usuario vuelva a ella.
    @Override
    public void onResume() {
        super.onResume();
        // Se llama al método para cargar (o recargar) la lista de canciones, asegurando que siempre esté actualizada.
        loadSongs();
    }

    // Este método se encarga de pedir las canciones a la base de datos y mostrarlas en la lista.
    private void loadSongs() {
        long currentUserId = sessionManager.getUserId();
        // Se le pide al DatabaseHelper que devuelva un "Cursor" con todas las canciones del usuario actual.
        Cursor cursor = dbHelper.getSongsForUser(currentUserId);

        // Si el cursor está vacío (no hay canciones guardadas)... 
        if (cursor == null || cursor.getCount() == 0) {
            // ...se oculta la lista y se muestra el mensaje "Aún no has guardado ninguna canción".
            recyclerView.setVisibility(View.GONE);
            noSongsTextView.setVisibility(View.VISIBLE);
        } else {
            // Si hay canciones, se muestra la lista y se oculta el mensaje.
            recyclerView.setVisibility(View.VISIBLE);
            noSongsTextView.setVisibility(View.GONE);
            // Se comprueba si el adaptador ya existe.
            if (adapter == null) {
                // Si es la primera vez, se crea el adaptador, se le pasa el cursor y se conecta al RecyclerView.
                adapter = new SongCursorAdapter(getContext(), cursor);
                recyclerView.setAdapter(adapter);
            } else {
                // Si ya existía (porque el usuario ha vuelto a esta pestaña), simplemente se le pasan los nuevos datos para que actualice la lista.
                adapter.swapCursor(cursor);
            }
        }
    }
}
