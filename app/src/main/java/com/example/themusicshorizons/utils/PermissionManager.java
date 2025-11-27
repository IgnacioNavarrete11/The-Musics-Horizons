package com.example.themusicshorizons.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

// Esta clase es una "caja de herramientas" que centraliza toda la lógica para manejar los permisos de Android.
// Su propósito es hacer que el código de las Activities sea más limpio y fácil de leer.
public class PermissionManager {

    // Códigos de solicitud únicos para cada permiso. Se usan para saber qué respuesta de permiso estamos recibiendo.
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    public static final int AUDIO_PERMISSION_REQUEST_CODE = 101;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 102;

    // --- MÉTODOS PARA EL PERMISO DE CÁMARA ---

    // Comprueba si la aplicación ya tiene el permiso para usar la cámara.
    public static boolean checkCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    // Muestra al usuario el diálogo del sistema para solicitar el permiso de cámara.
    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    // --- MÉTODOS PARA EL PERMISO DE AUDIO ---

    // Comprueba si la aplicación ya tiene el permiso para grabar audio.
    public static boolean checkAudioPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    // Muestra al usuario el diálogo del sistema para solicitar el permiso de grabación de audio.
    public static void requestAudioPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_REQUEST_CODE);
    }

    // --- MÉTODOS PARA EL PERMISO DE UBICACIÓN ---

    // Comprueba si la aplicación ya tiene el permiso para acceder a la ubicación del dispositivo.
    public static boolean checkLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Muestra al usuario el diálogo del sistema para solicitar el permiso de ubicación.
    public static void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }
}
