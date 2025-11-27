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
import com.example.themusicshorizons.adapters.EventAdapter; // Nuestro propio adaptador para la lista de eventos.
import com.example.themusicshorizons.database.DatabaseHelper; // Nuestro propio "fontanero" de la base de datos.
import com.example.themusicshorizons.database.MusicHorizonsContract; // Nuestro propio "contrato" que define los nombres de las tablas y columnas.
import com.example.themusicshorizons.models.ticketmaster.Event; // Nuestro propio modelo que representa un evento.
import com.example.themusicshorizons.utils.SessionManager; // Nuestra propia "memoria" del usuario.
import com.google.gson.Gson; // La librería externa que usamos para convertir texto JSON a objetos Java y viceversa.

import java.util.ArrayList; // Herramienta para crear listas de objetos que pueden crecer dinámicamente.
import java.util.List; // Representa una colección ordenada de objetos.

// Este Fragment representa el contenido de la pestaña "Eventos" dentro de la sección "Mi Horizonte".
public class SavedEventsFragment extends Fragment {

    // Se declaran las variables para los componentes y herramientas.
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private TextView noEventsTextView;
    private List<Event> eventList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Se carga el diseño XML de este fragment.
        View view = inflater.inflate(R.layout.fragment_saved_events, container, false);

        // Se inicializan las herramientas.
        dbHelper = new DatabaseHelper(getContext());
        sessionManager = new SessionManager(getContext());

        // Se asocian las variables con los componentes del layout.
        recyclerView = view.findViewById(R.id.savedEventsRecyclerView);
        noEventsTextView = view.findViewById(R.id.noEventsTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    // Se ejecuta cada vez que el fragment se vuelve visible para el usuario.
    @Override
    public void onResume() {
        super.onResume();
        // Se llama al método para cargar (o recargar) la lista de eventos.
        loadEvents();
    }

    // Este método se encarga de pedir los eventos a la base de datos y mostrarlos en la lista.
    private void loadEvents() {
        long currentUserId = sessionManager.getUserId();
        // Se le pide al DatabaseHelper un "Cursor" con todos los eventos guardados por el usuario.
        Cursor cursor = dbHelper.getSavedEventsForUser(currentUserId);
        // Se inicializa una nueva lista vacía para almacenar los objetos Event.
        eventList = new ArrayList<>();
        
        if (cursor != null) {
            // Se obtiene el índice de la columna que contiene el JSON del evento, para no tener que buscarlo en cada iteración.
            int eventDataIndex = cursor.getColumnIndexOrThrow(MusicHorizonsContract.SavedEventEntry.COLUMN_EVENT_DATA);
            // Se prepara la herramienta Gson para la conversión.
            Gson gson = new Gson();
            // Se recorre el cursor fila por fila.
            while (cursor.moveToNext()) {
                // Se extrae el texto JSON de la columna.
                String eventDataString = cursor.getString(eventDataIndex);
                // Se usa Gson para convertir ese texto de vuelta a un objeto Event de Java.
                Event event = gson.fromJson(eventDataString, Event.class);
                // Se añade el objeto Event a nuestra lista.
                eventList.add(event);
            }
            cursor.close(); // Se cierra el cursor para liberar la memoria.
        }

        // Si la lista de eventos está vacía después de consultar la base de datos...
        if (eventList.isEmpty()) {
            // ...se oculta la lista y se muestra el mensaje "Aún no has guardado ningún evento".
            recyclerView.setVisibility(View.GONE);
            noEventsTextView.setVisibility(View.VISIBLE);
        } else {
            // Si hay eventos, se muestra la lista, se crea el adaptador y se conecta al RecyclerView.
            recyclerView.setVisibility(View.VISIBLE);
            noEventsTextView.setVisibility(View.GONE);
            adapter = new EventAdapter(getContext(), eventList);
            recyclerView.setAdapter(adapter);
        }
    }
}
