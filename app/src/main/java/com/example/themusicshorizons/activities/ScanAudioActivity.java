package com.example.themusicshorizons.activities;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.content.Intent; // Herramienta para abrir otras pantallas.
import android.content.pm.PackageManager; // Herramienta para comprobar si un permiso ha sido concedido.
import android.media.MediaRecorder; // La herramienta principal de Android para grabar audio y video.
import android.os.Bundle; // Herramienta para pasar datos entre pantallas.
import android.os.Handler; // Herramienta para programar tareas que se ejecuten en el futuro (como una cuenta atrás).
import android.os.Looper; // Herramienta para asociar un Handler al hilo principal de la aplicación.
import android.util.Log; // Herramienta para imprimir mensajes en la consola de depuración (Logcat).
import android.widget.Button; // Específicamente, el componente de botón.
import android.widget.TextView; // Específicamente, el componente para mostrar texto.
import android.widget.Toast; // Herramienta para mostrar mensajes flotantes.

import androidx.annotation.NonNull; // Anotación para indicar que un parámetro nunca puede ser nulo.
import androidx.appcompat.app.AppCompatActivity; // Clase base para crear pantallas.

import com.example.themusicshorizons.R; // Conecta el código con los recursos.
import com.example.themusicshorizons.models.AudDResponse; // Nuestro propio modelo para la respuesta de AudD.
import com.example.themusicshorizons.models.AudDResult; // Nuestro propio modelo para el resultado de AudD.
import com.example.themusicshorizons.services.AudDManager; // Nuestro propio "manager" para hablar con la API de AudD.
import com.example.themusicshorizons.utils.PermissionManager; // Nuestra propia "caja de herramientas" para los permisos.

import java.io.File; // Herramienta para manejar archivos en el sistema del teléfono.
import java.io.IOException; // Herramienta para manejar posibles errores al leer o escribir archivos.

import retrofit2.Call; // Representa una llamada a la API que está lista para ser ejecutada.
import retrofit2.Callback; // Es el "receptor" que se queda esperando la respuesta de la API.
import retrofit2.Response; // Contiene la respuesta completa del servidor.

// Esta Activity es la pantalla que permite al usuario reconocer una canción grabando audio con el micrófono.
public class ScanAudioActivity extends AppCompatActivity {

    private static final String TAG = "ScanAudioActivity";
    // Define que la grabación de audio durará 8 segundos (8000 milisegundos).
    private static final int RECORDING_DURATION_MS = 8000;

    private Button listenButton;
    private TextView timerTextView;
    private boolean isRecognizing = false;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private AudDManager audDManager;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_audio);

        listenButton = findViewById(R.id.listenButton);
        timerTextView = findViewById(R.id.timerTextView);
        audDManager = new AudDManager();

        // Se define la acción que ocurre al pulsar el botón "ESCUCHAR".
        listenButton.setOnClickListener(v -> {
            if (PermissionManager.checkAudioPermission(this)) {
                startAudioScan();
            } else {
                PermissionManager.requestAudioPermission(this);
            }
        });
    }

    // Este método contiene la lógica para iniciar la grabación de audio.
    private void startAudioScan() {
        if (isRecognizing) return; // Evita que se inicie una nueva grabación si ya hay una en curso.

        isRecognizing = true;
        listenButton.setEnabled(false);
        listenButton.setText("Escuchando...");
        timerTextView.setText("Grabando...");

        try {
            // Crea un archivo temporal en la caché de la app para guardar el audio grabado.
            audioFile = File.createTempFile("recording", ".amr", getCacheDir());

            // Configura el MediaRecorder, la herramienta de Android para grabar audio.
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();

            // Inicia una cuenta atrás. Cuando pasen los 8 segundos, se ejecutará el método stopRecordingAndRecognize.
            handler.postDelayed(this::stopRecordingAndRecognize, RECORDING_DURATION_MS);

        } catch (IOException | IllegalStateException e) {
            Log.e(TAG, "Error al iniciar la grabación", e);
            resetUI();
        }
    }

    // Este método se ejecuta cuando termina la grabación.
    private void stopRecordingAndRecognize() {
        if (mediaRecorder == null) return;

        try {
            mediaRecorder.stop();
            mediaRecorder.release();
        } catch (RuntimeException e) { 
            Log.e(TAG, "Error al detener la grabación", e);
        }
        mediaRecorder = null;

        // Comprueba si el archivo de audio se creó correctamente.
        if (audioFile == null || audioFile.length() == 0) {
            Toast.makeText(this, "No se pudo crear el archivo de audio.", Toast.LENGTH_SHORT).show();
            resetUI();
            return;
        }

        timerTextView.setText("Analizando...");

        // Llama al AudDManager para que envíe el archivo de audio a la API.
        audDManager.recognize(audioFile, new Callback<AudDResponse>() {
            // Se ejecuta cuando AudD responde.
            @Override
            public void onResponse(Call<AudDResponse> call, Response<AudDResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().result != null) {
                    // Si la respuesta es exitosa y contiene un resultado, se extraen los datos.
                    AudDResult result = response.body().result;
                    String artworkUrl = null;
                    String spotifyUrl = null;

                    if (result.spotify != null) {
                        if (result.spotify.album != null && result.spotify.album.images != null && result.spotify.album.images.length > 0) {
                            artworkUrl = result.spotify.album.images[0].url;
                        }
                        if (result.spotify.external_urls != null) {
                            spotifyUrl = result.spotify.external_urls.spotify;
                        }
                    }
                    if (artworkUrl == null && result.artwork != null) {
                        artworkUrl = result.artwork;
                    }

                    // Se prepara y abre la pantalla de resultados, pasándole todos los datos encontrados.
                    Intent intent = new Intent(ScanAudioActivity.this, AudioResultsActivity.class);
                    intent.putExtra("ARTIST", result.artist);
                    intent.putExtra("TITLE", result.title);
                    intent.putExtra("ALBUM", result.album);
                    intent.putExtra("ARTWORK_URL", artworkUrl);
                    intent.putExtra("SPOTIFY_URL", spotifyUrl);
                    startActivity(intent);
                } else {
                    Toast.makeText(ScanAudioActivity.this, "No se pudo reconocer la canción. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
                }
                resetUI();
            }

            // Se ejecuta si hay un error de red.
            @Override
            public void onFailure(Call<AudDResponse> call, Throwable t) {
                Toast.makeText(ScanAudioActivity.this, "Error de red. Revisa tu conexión.", Toast.LENGTH_SHORT).show();
                resetUI();
            }
        });
    }

    // Restaura la interfaz a su estado inicial.
    private void resetUI() {
        isRecognizing = false;
        listenButton.setEnabled(true);
        listenButton.setText("ESCUCHAR");
        timerTextView.setText("10");
    }

    // Recibe la respuesta del usuario al diálogo de permisos.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionManager.AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startAudioScan();
            } else {
                Toast.makeText(this, "El permiso de audio es necesario para esta función", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Se asegura de que la grabación se detenga si el usuario sale de la pantalla.
    @Override
    protected void onStop() {
        super.onStop();
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                // Se ignora el error porque la grabación podría estar ya detenida.
            }
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
