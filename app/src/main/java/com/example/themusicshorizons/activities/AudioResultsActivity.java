package com.example.themusicshorizons.activities;

// --- IMPORTACIONES DE HERRAMIENTAS ---
// Se importan las "herramientas" que esta pantalla necesita para funcionar.

import android.content.Intent; // Herramienta para abrir otras pantallas o aplicaciones (como el navegador).
import android.net.Uri; // Herramienta para manejar direcciones web (URLs).
import android.os.Bundle; // Herramienta para pasar datos entre pantallas y guardar el estado de la actividad.
import android.view.View; // Es la base de todos los componentes visuales.
import android.widget.Button; // Específicamente, el componente de botón.
import android.widget.ImageView; // Específicamente, el componente para mostrar imágenes.
import android.widget.TextView; // Específicamente, el componente para mostrar texto.
import android.widget.Toast; // Herramienta para mostrar mensajes flotantes en la pantalla.

import androidx.appcompat.app.AppCompatActivity; // Es la clase base para crear pantallas en Android con soporte para versiones antiguas.

import com.bumptech.glide.Glide; // La librería externa que se usa para descargar y mostrar imágenes de internet de forma eficiente.
import com.example.themusicshorizons.R; // El archivo que conecta el código Java con los recursos (layouts, strings, colores, etc.).
import com.example.themusicshorizons.database.DatabaseHelper; // La clase que actúa como el "fontanero" de la base de datos.
import com.example.themusicshorizons.utils.SessionManager; // La clase que actúa como la "memoria" del usuario.

import org.json.JSONException; // Herramienta para manejar posibles errores al trabajar con texto en formato JSON.
import org.json.JSONObject; // Herramienta para crear y leer objetos JSON, que es una forma de empaquetar datos en texto.

// Esta Activity es la pantalla que muestra los detalles de una canción reconocida.
public class AudioResultsActivity extends AppCompatActivity {

    // Se declaran las variables que representarán a los componentes visuales de la pantalla.
    private ImageView albumArtImageView;
    private TextView titleTextView, artistTextView, albumTextView;
    private Button saveButton, spotifyButton;
    // Se declaran las variables para las herramientas de base de datos y sesión.
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    // Este es el método principal que se ejecuta cuando se crea la pantalla.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Conecta esta clase de Java con su diseño visual definido en el archivo XML.
        setContentView(R.layout.activity_audio_results);

        // Se inicializan las herramientas, listas para ser usadas.
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Se asocian las variables de Java con los componentes específicos del archivo XML a través de su ID.
        albumArtImageView = findViewById(R.id.albumArtImageView);
        titleTextView = findViewById(R.id.titleTextView);
        artistTextView = findViewById(R.id.artistTextView);
        albumTextView = findViewById(R.id.albumTextView);
        saveButton = findViewById(R.id.saveButton);
        spotifyButton = findViewById(R.id.spotifyButton);

        // --- RECEPCIÓN Y VISUALIZACIÓN DE DATOS ---

        // La pantalla anterior (ScanAudioActivity) usó un Intent para enviar datos. Aquí se recogen esos datos usando las claves correspondientes.
        String artist = getIntent().getStringExtra("ARTIST");
        String title = getIntent().getStringExtra("TITLE");
        String album = getIntent().getStringExtra("ALBUM");
        String artworkUrl = getIntent().getStringExtra("ARTWORK_URL");
        String spotifyUrl = getIntent().getStringExtra("SPOTIFY_URL");

        // Una vez recogidos los datos, se usan para rellenar los componentes de texto en la pantalla, haciéndolos visibles al usuario.
        titleTextView.setText(title);
        artistTextView.setText(artist);
        // Se comprueba si el nombre del álbum existe; si no, se muestra un texto por defecto.
        albumTextView.setText(album != null ? album : "Álbum Desconocido");

        // Se comprueba si se recibió una URL para la carátula.
        // Si es así, se le pide a la librería Glide que la descargue y la muestre en el ImageView.
        // .placeholder() muestra una imagen temporal mientras se descarga la real.
        // .error() muestra una imagen de error si la descarga falla.
        if (artworkUrl != null) {
            Glide.with(this)
                .load(artworkUrl)
                .placeholder(R.color.gris_oscuro)
                .error(R.drawable.ic_person) 
                .into(albumArtImageView);
        }

        // --- LÓGICA DE LOS BOTONES ---

        // Prepara el botón "ABRIR EN SPOTIFY". Por defecto está oculto.
        // Si se recibió una URL de Spotify, se hace visible el botón.
        if (spotifyUrl != null) {
            spotifyButton.setVisibility(View.VISIBLE);
            // Se le asigna la acción de crear un Intent para abrir esa URL en el navegador del teléfono.
            spotifyButton.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUrl));
                startActivity(browserIntent);
            });
        } else {
            // Si no hay URL, el botón permanece oculto.
            spotifyButton.setVisibility(View.GONE);
        }

        // Define lo que pasa al pulsar el botón "GUARDAR".
        saveButton.setOnClickListener(v -> {
            // Se obtiene el ID del usuario actual desde el SessionManager.
            long currentUserId = sessionManager.getUserId();
            if (currentUserId == 0) { return; } // Si no hay usuario, no se hace nada.

            try {
                // Se crea un objeto JSON para empaquetar los datos de la canción en un solo texto.
                // Esto es útil para no tener que crear muchas columnas en la base de datos.
                JSONObject songData = new JSONObject();
                songData.put("artist", artist);
                songData.put("title", title);
                songData.put("album", album);

                // Se llama al DatabaseHelper para que escriba en la base de datos, pasándole todos los datos.
                boolean isSaved = dbHelper.addDiscoveredSong(currentUserId, songData.toString(), artworkUrl, spotifyUrl);

                // Si la base de datos confirma que la operación fue exitosa...
                if (isSaved) {
                    // ...se muestra un mensaje de éxito al usuario.
                    Toast.makeText(this, "¡Canción guardada en tu horizonte!", Toast.LENGTH_SHORT).show();
                    // Y se deshabilita el botón para evitar que se guarde la misma canción varias veces.
                    saveButton.setEnabled(false);
                    saveButton.setText("GUARDADO");
                } else {
                    Toast.makeText(this, "Error al guardar la canción", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // Si hay un error al crear el objeto JSON, se imprime en el log para depuración.
                e.printStackTrace();
            }
        });
    }
}
