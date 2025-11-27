package com.example.themusicshorizons.activities;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.database.Cursor; // Herramienta para leer los resultados de la base de datos.
import android.os.Bundle; // Herramienta para pasar datos entre pantallas.

import androidx.appcompat.app.AppCompatActivity; // Clase base para crear pantallas.
import androidx.recyclerview.widget.LinearLayoutManager; // Define la organización de la lista.
import androidx.recyclerview.widget.RecyclerView; // El componente para mostrar listas.

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.adapters.SongCursorAdapter;
import com.example.themusicshorizons.database.DatabaseHelper;
import com.example.themusicshorizons.utils.SessionManager;

// --- NOTA DE ARQUITECTURA ---
// Esta clase ya no se utiliza en la versión actual de la aplicación.
// Su funcionalidad fue reemplazada por el sistema de Fragments (MyHorizonFragment, SavedSongsFragment, etc.)
// para permitir la navegación con pestañas. Se mantiene aquí como referencia histórica del desarrollo.

// Esta Activity representaba la pantalla "Mi Horizonte" en una versión anterior.
public class MyHorizonActivity extends AppCompatActivity {

    // Se declaran las variables para los componentes y herramientas.
    private RecyclerView recyclerView;
    private SongCursorAdapter adapter;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_horizon);

        // Se inicializan las herramientas.
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Se asocian las variables con los componentes del layout.
        recyclerView = findViewById(R.id.songsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Se obtiene el ID del usuario y se piden sus canciones a la base de datos.
        long currentUserId = sessionManager.getUserId();
        Cursor cursor = dbHelper.getSongsForUser(currentUserId);

        // Se crea el adaptador con los datos obtenidos y se conecta al RecyclerView para mostrarlos.
        adapter = new SongCursorAdapter(this, cursor);
        recyclerView.setAdapter(adapter);
    }

    // Este método se ejecutaba cada vez que el usuario volvía a esta pantalla.
    @Override
    protected void onResume() {
        super.onResume();
        // Se obtenía de nuevo el ID del usuario.
        long currentUserId = sessionManager.getUserId();
        if (adapter != null) {
            // Se pedían los datos de nuevo a la base de datos y se actualizaba la lista, 
            // para reflejar posibles cambios (como una canción recién borrada).
            Cursor newCursor = dbHelper.getSongsForUser(currentUserId);
            adapter.swapCursor(newCursor);
        }
    }
}
