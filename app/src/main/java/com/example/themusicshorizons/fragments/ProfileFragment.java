package com.example.themusicshorizons.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.database.DatabaseHelper;
import com.example.themusicshorizons.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private TextView usernameProfileTextView, emailProfileTextView;
    private Button logoutButton;
    private FloatingActionButton editProfileImageButton;
    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(getContext());
        dbHelper = new DatabaseHelper(getContext());

        usernameProfileTextView = view.findViewById(R.id.usernameProfileTextView);
        emailProfileTextView = view.findViewById(R.id.emailProfileTextView);
        logoutButton = view.findViewById(R.id.logoutButton);
        editProfileImageButton = view.findViewById(R.id.editProfileImageButton);

        // Cargar datos del usuario
        long userId = sessionManager.getUserId();
        String username = sessionManager.getUserDetails().get(SessionManager.KEY_USERNAME);
        String email = dbHelper.getUserEmail(userId);

        usernameProfileTextView.setText(username != null ? username : "Usuario");
        emailProfileTextView.setText(email != null ? email : "email@ejemplo.com");

        // Listeners
        logoutButton.setOnClickListener(v -> sessionManager.logoutUser());
        editProfileImageButton.setOnClickListener(v -> {
            // Aquí irá la lógica para seleccionar una nueva foto de perfil
            Toast.makeText(getContext(), "Función para cambiar foto de perfil próximamente", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
