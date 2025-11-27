package com.example.themusicshorizons.activities;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.content.Intent; // Herramienta para abrir otras pantallas.
import android.database.Cursor; // Herramienta para leer los resultados de una consulta a la base de datos, fila por fila.
import android.os.Bundle; // Herramienta para pasar datos entre pantallas.
import android.widget.Button; // Específicamente, el componente de botón.
import android.widget.EditText; // Específicamente, el componente de campo de texto editable.
import android.widget.Toast; // Herramienta para mostrar mensajes flotantes.

import androidx.appcompat.app.AppCompatActivity; // Clase base para crear pantallas.

import com.example.themusicshorizons.R; // Conecta el código con los recursos.
import com.example.themusicshorizons.database.DatabaseHelper; // El "fontanero" de la base de datos.
import com.example.themusicshorizons.database.MusicHorizonsContract; // El "contrato" que define los nombres de las tablas y columnas.
import com.example.themusicshorizons.utils.SessionManager; // La "memoria" del usuario.

// Esta Activity es la pantalla que permite al usuario iniciar sesión.
public class LoginActivity extends AppCompatActivity {

    // Se declaran las variables para los componentes visuales y las herramientas.
    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se inicializa el gestor de sesión.
        sessionManager = new SessionManager(this);
        
        // Esta es la lógica de "recuérdame". Comprueba si el usuario ya tiene una sesión activa.
        // Si es así, no muestra la pantalla de login y lo lleva directamente a la pantalla principal.
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Cierra esta pantalla para que el usuario no pueda volver a ella con el botón "atrás".
            return; // Detiene la ejecución del resto del método.
        }

        // Si el usuario no ha iniciado sesión, se carga el layout de la pantalla de login.
        setContentView(R.layout.activity_login);

        // Se inicializa la herramienta para hablar con la base de datos.
        dbHelper = new DatabaseHelper(this);

        // Se asocian las variables con los componentes del layout XML.
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Se asigna la acción que ocurre al pulsar el botón de "Login".
        loginButton.setOnClickListener(v -> loginUser());
        // Se asigna la acción que ocurre al pulsar el botón de "Registrarse": abrir la pantalla de registro.
        registerButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    // Este método contiene toda la lógica para verificar las credenciales del usuario.
    private void loginUser() {
        // Se recoge el texto que el usuario ha escrito, quitando espacios en blanco al principio y al final.
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Se comprueba que ninguno de los campos esté vacío.
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Se le pide al DatabaseHelper que busque en la tabla de usuarios si existe una fila con ese nombre y contraseña.
        // Se usa un "try-with-resources" para asegurar que el Cursor se cierre automáticamente y no consuma memoria.
        try (Cursor cursor = dbHelper.checkUser(username, password)) {
            // Si el cursor encuentra al menos un resultado, el login es correcto.
            if (cursor != null && cursor.moveToFirst()) {
                // Se extraen los datos del usuario de la fila encontrada.
                long userId = cursor.getLong(cursor.getColumnIndexOrThrow(MusicHorizonsContract.UserEntry._ID));
                String fetchedUsername = cursor.getString(cursor.getColumnIndexOrThrow(MusicHorizonsContract.UserEntry.COLUMN_USERNAME));

                // Se llama al SessionManager para que guarde los datos del usuario y lo marque como "logueado".
                sessionManager.createLoginSession(userId, fetchedUsername);

                Toast.makeText(this, "¡Bienvenido, " + fetchedUsername + "!", Toast.LENGTH_SHORT).show();

                // Se abre la pantalla principal de la aplicación.
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Se cierra la pantalla de login.
            } else {
                // Si no se encuentra ningún usuario, se muestra un mensaje de error.
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
