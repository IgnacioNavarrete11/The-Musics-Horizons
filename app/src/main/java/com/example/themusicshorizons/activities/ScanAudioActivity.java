package com.example.themusicshorizons.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.models.AudDResponse;
import com.example.themusicshorizons.models.AudDResult;
import com.example.themusicshorizons.services.AudDManager;
import com.example.themusicshorizons.utils.PermissionManager;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanAudioActivity extends AppCompatActivity {

    private static final String TAG = "ScanAudioActivity";
    private static final int RECORDING_DURATION_MS = 10000; // 10 segundos

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

        listenButton.setOnClickListener(v -> {
            if (PermissionManager.checkAudioPermission(this)) {
                startAudioScan();
            } else {
                PermissionManager.requestAudioPermission(this);
            }
        });
    }

    private void startAudioScan() {
        if (isRecognizing) return;

        isRecognizing = true;
        listenButton.setEnabled(false);
        listenButton.setText("Escuchando...");
        timerTextView.setText("Grabando...");

        try {
            audioFile = File.createTempFile("recording", ".mp3", getCacheDir());
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();

            handler.postDelayed(this::stopRecordingAndRecognize, RECORDING_DURATION_MS);

        } catch (IOException e) {
            Log.e(TAG, "Error al iniciar la grabación", e);
            resetUI();
        }
    }

    private void stopRecordingAndRecognize() {
        if (mediaRecorder == null) return;

        try {
            mediaRecorder.stop();
            mediaRecorder.release();
        } catch (Exception e) {
            Log.e(TAG, "Error al detener la grabación", e);
        }
        mediaRecorder = null;
        timerTextView.setText("Analizando...");

        audDManager.recognize(audioFile, new Callback<AudDResponse>() {
            @Override
            public void onResponse(Call<AudDResponse> call, Response<AudDResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().result != null) {
                    AudDResult result = response.body().result;
                    Intent intent = new Intent(ScanAudioActivity.this, AudioResultsActivity.class);
                    intent.putExtra("ARTIST", result.artist);
                    intent.putExtra("TITLE", result.title);
                    intent.putExtra("ALBUM", result.album);
                    startActivity(intent);
                } else {
                    Toast.makeText(ScanAudioActivity.this, "No se pudo reconocer la canción. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
                }
                resetUI();
            }

            @Override
            public void onFailure(Call<AudDResponse> call, Throwable t) {
                Log.e(TAG, "Error en la llamada a la API", t);
                Toast.makeText(ScanAudioActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                resetUI();
            }
        });
    }

    private void resetUI() {
        isRecognizing = false;
        listenButton.setEnabled(true);
        listenButton.setText("ESCUCHAR");
        timerTextView.setText("10");
    }

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

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
