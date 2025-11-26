package com.example.themusicshorizons.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.database.DatabaseHelper;
import com.example.themusicshorizons.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class AudioResultsActivity extends AppCompatActivity {

    private TextView titleTextView, artistTextView, albumTextView;
    private Button saveButton;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_results);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        titleTextView = findViewById(R.id.titleTextView);
        artistTextView = findViewById(R.id.artistTextView);
        albumTextView = findViewById(R.id.albumTextView);
        saveButton = findViewById(R.id.saveButton);

        String artist = getIntent().getStringExtra("ARTIST");
        String title = getIntent().getStringExtra("TITLE");
        String album = getIntent().getStringExtra("ALBUM");

        titleTextView.setText(title);
        artistTextView.setText(artist);
        albumTextView.setText(album != null ? album : "Álbum Desconocido");

        saveButton.setOnClickListener(v -> {
            long currentUserId = sessionManager.getUserId();

            if (currentUserId == 0) {
                Toast.makeText(this, "Error: No se ha podido identificar al usuario.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject songData = new JSONObject();
                songData.put("artist", artist);
                songData.put("title", title);
                songData.put("album", album);

                boolean isSaved = dbHelper.addDiscoveredSong(currentUserId, songData.toString());

                if (isSaved) {
                    Toast.makeText(this, "¡Canción guardada en tu horizonte!", Toast.LENGTH_SHORT).show();
                    saveButton.setEnabled(false);
                    saveButton.setText("GUARDADO");
                } else {
                    Toast.makeText(this, "Error al guardar la canción", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al formatear los datos de la canción", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
