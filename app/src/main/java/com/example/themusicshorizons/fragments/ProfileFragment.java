package com.example.themusicshorizons.fragments;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.content.Intent; // Herramienta para abrir otras pantallas.
import android.os.Bundle; // Herramienta para pasar datos.
import android.view.LayoutInflater; // Herramienta para "inflar" o cargar un layout XML en una vista de Java.
import android.view.View; // La base de todos los componentes visuales.
import android.view.ViewGroup; // Representa un contenedor de vistas.
import android.widget.Button; // El componente de botón.
import android.widget.TextView; // El componente para mostrar texto.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // La clase base para todos los fragmentos.

import com.example.themusicshorizons.R; // Conecta el código con los recursos.
import com.example.themusicshorizons.activities.LoginActivity; // La pantalla de Login, para poder volver a ella.
import com.example.themusicshorizons.database.DatabaseHelper; // Nuestro propio "fontanero" de la base de datos.
import com.example.themusicshorizons.utils.SessionManager; // Nuestra propia "memoria" del usuario.

// Este Fragment representa la pantalla de "Perfil" del usuario.
public class ProfileFragment extends Fragment {

    // Se declaran las variables para los componentes y herramientas.
    private TextView usernameTextView, emailTextView;
    private Button logoutButton;
    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Se carga el diseño XML de este fragment.
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Se inicializan las herramientas.
        sessionManager = new SessionManager(getContext());
        dbHelper = new DatabaseHelper(getContext());

        // Se asocian las variables con los componentes del layout.
        usernameTextView = view.findViewById(R.id.usernameProfileTextView);
        emailTextView = view.findViewById(R.id.emailProfileTextView);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Se llama al método que carga y muestra la información del usuario.
        loadUserProfile();

        // Se define la acción que ocurre al pulsar el botón de "Cerrar Sesión".
        logoutButton.setOnClickListener(v -> {
            // Se le pide al SessionManager que borre todos los datos de la sesión actual.
            sessionManager.logoutUser();
            
            // Se crea la intención de volver a la pantalla de Login.
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            // Se añaden "banderas" especiales al Intent. Esto le dice al sistema que borre todas las pantallas anteriores,
            // para que el usuario no pueda pulsar "atrás" y volver a la app sin iniciar sesión.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish(); // Se cierra la HomeActivity para liberar memoria.
        });

        return view;
    }

    // Este método se encarga de obtener y mostrar los datos del usuario en la pantalla.
    private void loadUserProfile() {
        // Se obtiene el ID y el nombre del usuario desde la sesión guardada.
        long currentUserId = sessionManager.getUserId();
        String currentUsername = sessionManager.pref.getString(SessionManager.KEY_USERNAME, "Usuario"); // "Usuario" es un valor por defecto.

        // Se usa el ID del usuario para pedirle su email a la base de datos.
        String currentUserEmail = dbHelper.getUserEmail(currentUserId);

        // Se muestra el nombre de usuario y el email en los campos de texto correspondientes.
        usernameTextView.setText(currentUsername);
        emailTextView.setText(currentUserEmail != null ? currentUserEmail : "Email no disponible");
    }
}
