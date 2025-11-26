package com.example.themusicshorizons.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.utils.SessionManager;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameProfileTextView;
    private Button logoutButton;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);

        usernameProfileTextView = findViewById(R.id.usernameProfileTextView);
        logoutButton = findViewById(R.id.logoutButton);

        // Obtener y mostrar el nombre de usuario
        HashMap<String, String> user = sessionManager.getUserDetails();
        String username = user.get(SessionManager.KEY_USERNAME);
        usernameProfileTextView.setText(username);

        // Configurar el botón de cerrar sesión
        logoutButton.setOnClickListener(v -> {
            sessionManager.logoutUser();
        });
    }
}
