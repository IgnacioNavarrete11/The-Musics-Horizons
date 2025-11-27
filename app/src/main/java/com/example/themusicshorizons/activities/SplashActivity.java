package com.example.themusicshorizons.activities;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.content.Intent; // Herramienta para abrir otras pantallas.
import android.os.Bundle; // Herramienta para pasar datos entre pantallas.
import android.os.Handler; // Herramienta para programar tareas que se ejecuten en el futuro (como una cuenta atrás).

import androidx.appcompat.app.AppCompatActivity; // Clase base para crear pantallas.

import com.example.themusicshorizons.R; // Conecta el código con los recursos.

// Esta Activity es la pantalla de bienvenida que se muestra al iniciar la aplicación.
public class SplashActivity extends AppCompatActivity {

    // Define la duración que la pantalla de bienvenida será visible, en milisegundos.
    private static final int SPLASH_TIME_OUT = 3000; // 3 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Este bloque de código crea una cuenta atrás.
        // Se usa un Handler para programar una tarea que se ejecutará en el futuro sin bloquear la interfaz de usuario.
        new Handler().postDelayed(new Runnable() {
            // El código dentro de este método "run" se ejecutará cuando termine la cuenta atrás (3 segundos).
            @Override
            public void run() {
                // Crea la intención de abrir la pantalla de Login.
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                // Cierra la pantalla de Splash para que el usuario no pueda volver a ella con el botón "atrás".
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
