package com.example.themusicshorizons.activities;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.os.Bundle; // Herramienta para pasar datos entre pantallas.

import androidx.appcompat.app.AppCompatActivity; // Clase base para crear pantallas.
import androidx.navigation.NavController; // El "cerebro" que gestiona la navegación entre fragments.
import androidx.navigation.fragment.NavHostFragment; // El contenedor visual donde se muestran los fragments.
import androidx.navigation.ui.NavigationUI; // Herramienta que conecta la barra de navegación con el NavController.

import com.example.themusicshorizons.R; // Conecta el código con los recursos.
import com.google.android.material.bottomnavigation.BottomNavigationView; // Específicamente, el componente de barra de navegación inferior.

// Esta Activity es la pantalla principal de la aplicación después de iniciar sesión.
// Su única misión es contener la barra de navegación inferior y el espacio donde se mostrarán los diferentes fragments (Home, Mi Horizonte, Perfil).
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Se asocia la barra de navegación inferior del layout con su variable en Java.
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Se busca el contenedor de fragmentos (el "lienzo") definido en el layout.
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        // Se obtiene el controlador de navegación desde ese contenedor.
        NavController navController = navHostFragment.getNavController();

        // Esta es la línea mágica que conecta todo. Le dice al NavigationUI que use el navController para
        // cambiar de fragment automáticamente cuando el usuario pulse un icono en el bottomNavigationView.
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
