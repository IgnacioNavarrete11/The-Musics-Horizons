package com.example.themusicshorizons.activities;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.content.Intent; // Herramienta para abrir otras pantallas o aplicaciones.
import android.net.Uri; // Herramienta para manejar direcciones web (URLs).
import android.os.Bundle; // Herramienta para pasar datos entre pantallas.
import android.view.View; // Es la base de todos los componentes visuales.
import android.widget.Button; // Específicamente, el componente de botón.
import android.widget.ImageView; // Específicamente, el componente para mostrar imágenes.
import android.widget.TextView; // Específicamente, el componente para mostrar texto.
import android.widget.Toast; // Herramienta para mostrar mensajes flotantes.

import androidx.appcompat.app.AppCompatActivity; // Clase base para crear pantallas.

import com.bumptech.glide.Glide; // La librería externa para descargar y mostrar imágenes de internet.
import com.example.themusicshorizons.R; // Conecta el código con los recursos.
import com.example.themusicshorizons.database.DatabaseHelper; // Nuestro propio "fontanero" de la base de datos.

// Esta Activity es la pantalla que muestra los detalles de una canción que ya ha sido guardada en "Mi Horizonte".
public class SongDetailActivity extends AppCompatActivity {

    // Se declaran las variables para los componentes visuales.
    private ImageView albumArtImageView;
    private TextView titleTextView, artistTextView;
    private Button spotifyButton, deleteButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        // Se inicializa la herramienta para hablar con la base de datos.
        dbHelper = new DatabaseHelper(this);

        // Se asocian las variables con los componentes del layout XML.
        albumArtImageView = findViewById(R.id.albumArtImageViewDetail);
        titleTextView = findViewById(R.id.titleTextViewDetail);
        artistTextView = findViewById(R.id.artistTextViewDetail);
        spotifyButton = findViewById(R.id.spotifyButtonDetail);
        deleteButton = findViewById(R.id.deleteButton);

        // --- RECEPCIÓN Y VISUALIZACIÓN DE DATOS ---

        // La pantalla anterior (la lista de "Mi Horizonte") usó un Intent para enviar los datos de la canción pulsada.
        // Aquí se recogen esos datos usando las claves correspondientes.
        long songId = getIntent().getLongExtra("SONG_ID", -1); // El -1 es un valor por defecto si no se encuentra el ID.
        String artist = getIntent().getStringExtra("ARTIST");
        String title = getIntent().getStringExtra("TITLE");
        String artworkUrl = getIntent().getStringExtra("ARTWORK_URL");
        String spotifyUrl = getIntent().getStringExtra("SPOTIFY_URL");

        // Se usan los datos recogidos para rellenar los componentes de texto de la pantalla.
        titleTextView.setText(title);
        artistTextView.setText(artist);

        // Se comprueba si existe una URL para la carátula y se le pide a Glide que la muestre.
        if (artworkUrl != null) {
            Glide.with(this).load(artworkUrl).into(albumArtImageView);
        }

        // --- LÓGICA DE LOS BOTONES ---

        // Prepara el botón "ABRIR EN SPOTIFY".
        if (spotifyUrl != null) {
            spotifyButton.setVisibility(View.VISIBLE);
            // Se le asigna la acción de abrir esa URL en el navegador.
            spotifyButton.setOnClickListener(v -> {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUrl)));
            });
        } else {
            spotifyButton.setVisibility(View.GONE);
        }

        // Define lo que pasa al pulsar el botón "ELIMINAR".
        deleteButton.setOnClickListener(v -> {
            // Comprueba que el ID de la canción sea válido antes de hacer nada.
            if (songId != -1) {
                // Llama al DatabaseHelper para que borre la canción de la base de datos usando su ID.
                dbHelper.deleteDiscoveredSong(songId);
                Toast.makeText(this, "Canción eliminada de tu horizonte", Toast.LENGTH_SHORT).show();
                // Cierra esta pantalla de detalles para volver a la lista de "Mi Horizonte" (que ahora se refrescará y mostrará la canción eliminada).
                finish();
            }
        });
    }
}
