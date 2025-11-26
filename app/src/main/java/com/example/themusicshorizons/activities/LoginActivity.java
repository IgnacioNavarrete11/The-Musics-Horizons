package com.example.themusicshorizons.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.database.DatabaseHelper;
import com.example.themusicshorizons.database.MusicHorizonsContract;
import com.example.themusicshorizons.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        // Si el usuario ya ha iniciado sesión, ir directamente a Home
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return; // Evitar que se muestre la pantalla de login
        }

        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(v -> loginUser());
        registerButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        try (Cursor cursor = dbHelper.checkUser(username, password)) {
            if (cursor != null && cursor.moveToFirst()) {
                long userId = cursor.getLong(cursor.getColumnIndexOrThrow(MusicHorizonsContract.UserEntry._ID));
                String fetchedUsername = cursor.getString(cursor.getColumnIndexOrThrow(MusicHorizonsContract.UserEntry.COLUMN_USERNAME));

                // Crear la sesión
                sessionManager.createLoginSession(userId, fetchedUsername);

                Toast.makeText(this, "¡Bienvenido, " + fetchedUsername + "!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
