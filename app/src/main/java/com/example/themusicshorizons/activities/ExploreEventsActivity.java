package com.example.themusicshorizons.activities;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.Manifest; // Contiene la lista de todos los permisos que una app puede solicitar.
import android.content.pm.PackageManager; // Herramienta para comprobar si un permiso ha sido concedido.
import android.location.Location; // Representa una ubicación geográfica (latitud y longitud).
import android.os.Bundle; // Herramienta para pasar datos entre pantallas.
import android.util.Log; // Herramienta para imprimir mensajes en la consola de depuración (Logcat).
import android.view.View; // Es la base de todos los componentes visuales.
import android.widget.ProgressBar; // Específicamente, el componente de barra de progreso circular.
import android.widget.TextView; // Específicamente, el componente para mostrar texto.
import android.widget.Toast; // Herramienta para mostrar mensajes flotantes.

import androidx.annotation.NonNull; // Anotación para indicar que un parámetro nunca puede ser nulo.
import androidx.appcompat.app.AppCompatActivity; // Clase base para crear pantallas.
import androidx.appcompat.widget.SearchView; // Específicamente, el componente de barra de búsqueda.
import androidx.core.app.ActivityCompat; // Herramienta de compatibilidad para manejar permisos en versiones antiguas de Android.
import androidx.recyclerview.widget.RecyclerView; // El componente avanzado para mostrar listas largas y eficientes.
import androidx.recyclerview.widget.LinearLayoutManager; // Define cómo se organizan los elementos en la lista (en este caso, una línea vertical).

import com.example.themusicshorizons.R; // Conecta el código con los recursos.
import com.example.themusicshorizons.adapters.EventAdapter; // El adaptador para la lista de eventos.
import com.example.themusicshorizons.models.ticketmaster.TicketmasterResponse; // El modelo para la respuesta de Ticketmaster.
import com.example.themusicshorizons.services.TicketmasterManager; // El "manager" para hablar con la API de Ticketmaster.
import com.example.themusicshorizons.utils.PermissionManager; // La "caja de herramientas" para los permisos.
import com.google.android.gms.location.FusedLocationProviderClient; // La herramienta principal de Google para obtener la ubicación del dispositivo.
import com.google.android.gms.location.LocationServices; // Punto de entrada para acceder a los servicios de ubicación de Google.

import retrofit2.Call; // Representa una llamada a la API que está lista para ser ejecutada.
import retrofit2.Callback; // Es el "receptor" que se queda esperando la respuesta de la API.
import retrofit2.Response; // Contiene la respuesta completa del servidor (código, datos, errores, etc.).

// Esta Activity es la pantalla que permite al usuario explorar eventos musicales.
public class ExploreEventsActivity extends AppCompatActivity {

    private static final String TAG = "ExploreEventsActivity";
    // El cliente de Google para obtener la ubicación.
    private FusedLocationProviderClient fusedLocationClient;
    // El manager para hablar con Ticketmaster.
    private TicketmasterManager ticketmasterManager;

    // Variables para los componentes visuales.
    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;
    private TextView noEventsTextView;
    private ProgressBar eventsProgressBar;
    private SearchView eventSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_events);

        // Se asocian las variables con los componentes del layout XML.
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        noEventsTextView = findViewById(R.id.noEventsTextView);
        eventsProgressBar = findViewById(R.id.eventsProgressBar);
        eventSearchView = findViewById(R.id.eventSearchView);
        
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Se inicializan las herramientas de ubicación y de la API.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ticketmasterManager = new TicketmasterManager();

        // Se configura la barra de búsqueda.
        setupSearchView();

        // Al iniciar, se intenta la búsqueda automática por GPS.
        if (PermissionManager.checkLocationPermission(this)) {
            findLocationAndEvents();
        } else {
            PermissionManager.requestLocationPermission(this);
        }
    }

    // Este método configura la barra de búsqueda para que sea funcional.
    private void setupSearchView() {
        eventSearchView.setSubmitButtonEnabled(true); // Hace visible el botón de "enviar" (flecha) dentro de la barra.

        // Define qué hacer cuando el usuario pulsa el botón de buscar.
        eventSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: Buscando por '" + query + "'");
                if (query != null && !query.isEmpty()) {
                    searchEventsByKeyword(query);
                }
                eventSearchView.clearFocus(); // Oculta el teclado para que no moleste.
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) { return false; } // No se usa.
        });
    }

    // Este método obtiene la ubicación GPS y, si tiene éxito, inicia la búsqueda de eventos.
    private void findLocationAndEvents() {
        showLoading(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, location -> {
                if (location != null) {
                    ticketmasterManager.findMusicEventsByLocation(location.getLatitude(), location.getLongitude(), getEventsCallback());
                } else {
                    showLoading(false);
                    noEventsTextView.setVisibility(View.VISIBLE);
                }
            });
    }

    // Este método inicia una búsqueda de eventos usando una palabra clave.
    private void searchEventsByKeyword(String keyword) {
        showLoading(true);
        ticketmasterManager.findMusicEventsByKeyword(keyword, getEventsCallback());
    }

    // Este es un "receptor" genérico para las respuestas de Ticketmaster.
    // Se usa tanto para la búsqueda por GPS como por palabra clave para no repetir código.
    private Callback<TicketmasterResponse> getEventsCallback() {
        return new Callback<TicketmasterResponse>() {
            @Override
            public void onResponse(Call<TicketmasterResponse> call, Response<TicketmasterResponse> response) {
                showLoading(false);
                // Si la respuesta es exitosa y contiene eventos...
                if (response.isSuccessful() && response.body() != null && response.body()._embedded != null && !response.body()._embedded.events.isEmpty()) {
                    // ...se hace visible la lista, se crea el adaptador con los eventos y se conecta al RecyclerView.
                    eventsRecyclerView.setVisibility(View.VISIBLE);
                    noEventsTextView.setVisibility(View.GONE);
                    eventAdapter = new EventAdapter(ExploreEventsActivity.this, response.body()._embedded.events);
                    eventsRecyclerView.setAdapter(eventAdapter);
                } else {
                    // Si no, se oculta la lista y se muestra el mensaje de "no hay resultados".
                    eventsRecyclerView.setVisibility(View.GONE);
                    noEventsTextView.setText("No se encontraron eventos para esta búsqueda.");
                    noEventsTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TicketmasterResponse> call, Throwable t) {
                showLoading(false);
                noEventsTextView.setText("Error de red. No se pudieron cargar los eventos.");
                noEventsTextView.setVisibility(View.VISIBLE);
            }
        };
    }

    // Un método simple para gestionar la visibilidad de la barra de progreso y los textos.
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            eventsProgressBar.setVisibility(View.VISIBLE);
            noEventsTextView.setVisibility(View.GONE);
            eventsRecyclerView.setVisibility(View.GONE);
        } else {
            eventsProgressBar.setVisibility(View.GONE);
        }
    }

    // Recibe la respuesta del usuario al diálogo de permisos.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionManager.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findLocationAndEvents();
            } else {
                showLoading(false);
                noEventsTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
