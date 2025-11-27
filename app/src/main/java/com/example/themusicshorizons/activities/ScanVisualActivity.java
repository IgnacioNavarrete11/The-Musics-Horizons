package com.example.themusicshorizons.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.utils.PermissionManager;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanVisualActivity extends AppCompatActivity {

    private static final String TAG = "ScanVisualActivity";
    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    private BarcodeScanner barcodeScanner;
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_visual);

        previewView = findViewById(R.id.camera_preview);
        // Crea un hilo de ejecución separado para el análisis de la cámara, para no bloquear la interfaz de usuario.
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Comprueba si la app tiene permiso para usar la cámara.
        if (PermissionManager.checkCameraPermission(this)) {
            startCamera(); // Si tiene permiso, inicia la cámara.
        } else {
            PermissionManager.requestCameraPermission(this); // Si no, se lo pide al usuario.
        }
    }

    // Este método inicializa y configura la cámara.
    private void startCamera() {
        // Obtiene el proveedor de la cámara de forma asíncrona.
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider); // Una vez obtenida la cámara, se enlaza a la vista previa.
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error al iniciar la cámara", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    // Este método conecta la cámara a la vista en pantalla y configura el analizador de imágenes.
    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        // Configura la vista previa para mostrar lo que la cámara está viendo.
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Configura el analizador de imágenes, que es el que buscará los códigos QR.
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        // Define qué hacer con cada "fotograma" que la cámara captura.
        imageAnalysis.setAnalyzer(cameraExecutor, imageProxy -> {
            if (!isProcessing) {
                @SuppressLint("UnsafeOptInUsageError")
                InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());
                processImageForQr(image, imageProxy); // Envía el fotograma a ser procesado para buscar un QR.
            }
        });

        // Desvincula cualquier uso anterior de la cámara y la vuelve a vincular al ciclo de vida de esta Activity.
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
    }

    // Este método usa la librería ML Kit para buscar un código QR en la imagen recibida.
    private void processImageForQr(InputImage image, ImageProxy imageProxy) {
        isProcessing = true;
        if (barcodeScanner == null) {
            barcodeScanner = BarcodeScanning.getClient();
        }

        barcodeScanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    // Si se encuentra al menos un código de barras...
                    if (!barcodes.isEmpty()) {
                        cameraExecutor.shutdownNow(); // Se detiene el análisis para no seguir buscando.
                        String rawValue = barcodes.get(0).getRawValue(); // Se obtiene el texto contenido en el QR.
                        Log.d(TAG, "Contenido del QR: " + rawValue);
                        runOnUiThread(() -> handleQrCodeResult(rawValue)); // Se pasa el texto al siguiente método para decidir qué hacer con él.
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error al procesar el código de barras", e))
                .addOnCompleteListener(task -> {
                    imageProxy.close(); // Se cierra la imagen para que la cámara pueda enviar la siguiente.
                    isProcessing = false;
                });
    }

    // Decide qué hacer con el texto obtenido del código QR.
    private void handleQrCodeResult(String rawValue) {
        // Si el texto parece ser una URL de una página web...
        if (Patterns.WEB_URL.matcher(rawValue).matches()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(rawValue))); // ...la abre en el navegador.
        } else {
            Toast.makeText(this, "Texto: " + rawValue, Toast.LENGTH_LONG).show(); // Si no, simplemente muestra el texto en un mensaje flotante.
        }
        finish(); // Cierra la pantalla de escaneo.
    }

    // Recibe la respuesta del usuario cuando se le piden permisos.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionManager.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "El permiso de cámara es necesario para esta función.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    // Se asegura de que la cámara y el analizador se apaguen correctamente cuando se destruye la Activity.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null && !cameraExecutor.isShutdown()) {
            cameraExecutor.shutdown();
        }
        if (barcodeScanner != null) {
            barcodeScanner.close();
        }
    }
}
