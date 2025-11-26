package com.example.themusicshorizons.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.utils.PermissionManager;

public class ExploreEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_events);

        if (PermissionManager.checkLocationPermission(this)) {
            startMapAndEvents();
        } else {
            PermissionManager.requestLocationPermission(this);
        }
    }

    private void startMapAndEvents() {
        // Aquí irá la lógica para obtener la ubicación, inicializar el mapa y buscar eventos con Ticketmaster
        Toast.makeText(this, "Ubicación obtenida. Cargando eventos...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionManager.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show();
                startMapAndEvents();
            } else {
                Toast.makeText(this, "El permiso de ubicación es necesario para encontrar eventos cercanos.", Toast.LENGTH_LONG).show();
                // Opcionalmente, podrías cerrar la activity o mostrar una vista sin mapa
                finish(); 
            }
        }
    }
}
